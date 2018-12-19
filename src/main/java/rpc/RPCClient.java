package rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class RPCClient {

  public static void main(String[] args) {
    HelloService helloService = getClient(HelloService.class, "localhost", 50001);
    System.out.println(helloService.hello("wq"));
  }

  public static <T> T getClient(Class<T> clazz, final String ip, final int port) {
    return (T) Proxy.newProxyInstance(RPCClient.class.getClassLoader(),
        new Class<?>[]{clazz}, new InvocationHandler(){

      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Socket socket = new Socket(ip, port);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeUTF(method.getName());
        out.writeObject(method.getParameterTypes());
        out.writeObject(args);
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        return in.readObject();
      }
    });
  }

}
