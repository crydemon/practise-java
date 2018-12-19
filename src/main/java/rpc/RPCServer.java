package rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPCServer {

  public static void main(String[] args) {
    HelloService helloService = new HelloServiceImpl();
    RPCServer server = new RPCServer();
    server.register(helloService, 50001);
  }

  private ExecutorService threadPool;
  private static final int DEFAULT_THREAD_NUM = 10;

  private Logger log = LoggerFactory.getLogger(RPCServer.class);

  public RPCServer() {
    threadPool = Executors.newFixedThreadPool(DEFAULT_THREAD_NUM);
  }

  public void register(Object service, int port) {
    log.info("server start... ");
    try {
      ServerSocket server = new ServerSocket(port);
      Socket socket = null;
      while ((socket = server.accept()) != null) {
        log.info("client connected...");
        threadPool.execute(new Processor(socket, service));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

class Processor implements Runnable {

  Socket socket;
  Object service;

  public Processor(Socket socket, Object service) {
    this.socket = socket;
    this.service = service;
  }


  public void run() {
    try {
      ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
      String methodName = in.readUTF();
      Class<?>[] parameterTypes = (Class<?>[]) in.readObject();
      Object[] parameters = (Object[]) in.readObject();
      Method method = service.getClass().getMethod(methodName, parameterTypes);
      try {
        Object result = method.invoke(this.service, parameters);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(result);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

  }
}


//使用了rpc框架，我们只需要定义一个接口，然后让rpc框架为我们生成客户端的代理类，同时生成服务端的serversocket类，
// 我们只需要在服务端编写真正的实现类，然后注册在服务器上就可以。其余的事情都是框架做的。
//
//    再说一遍，我们只需要：
//
//    （1）定义接口；
//
//    （2）用接口生成服务端和客户端代理的代码；
//
//    （3）编写接口实现；
//
//    （4）注册服务；

















