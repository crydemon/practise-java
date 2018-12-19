package tcp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {

  public static void main(String[] args) throws IOException {
    InetAddress inetAddress = InetAddress.getLocalHost();
    Socket socket = new Socket(inetAddress, 8888);
    OutputStream outputStream = socket.getOutputStream();
    InputStream inputStream = socket.getInputStream();
    String messenger = "Hello wq";
    outputStream.write(messenger.getBytes());
    outputStream.flush();
    socket.shutdownOutput();

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    byte[] msg = new byte[1024];
    int readLen;
    for (; (readLen = inputStream.read(msg)) != -1; ) {
      byteArrayOutputStream.write(msg, 0, readLen);
    }
    byte[] receiveMsg = byteArrayOutputStream.toByteArray();
    System.out.printf("Server said: %s\n", new String(receiveMsg));
    outputStream.close();
    inputStream.close();
    socket.close();
  }
}
