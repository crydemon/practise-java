package tcp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {


  public static void main(String[] args) throws IOException {
    Socket client;
    OutputStream outputStream;
    InputStream inputStream;
    ByteArrayOutputStream byteArrayOutputStream;

    ServerSocket server = new ServerSocket(8888);

    for (; ; ) {
      byteArrayOutputStream = new ByteArrayOutputStream();
      client = server.accept();
      outputStream = client.getOutputStream();
      inputStream = client.getInputStream();
      System.out.printf("handing client at : %s\n", client.getRemoteSocketAddress());
      byte[] msg = new byte[1024];
      int readLen;
      for (; (readLen = inputStream.read(msg)) != -1; ) {
        byteArrayOutputStream.write(msg, 0, readLen);
      }
      byte[] receiveMsg = byteArrayOutputStream.toByteArray();
      System.out.printf("Server receive data: %s\n", new String(receiveMsg));
      String sendMsg = "hello client";
      outputStream.write(sendMsg.getBytes());
      outputStream.flush();
      inputStream.close();
      outputStream.close();
      client.close();
    }
  }
}
