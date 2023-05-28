import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static java.util.Arrays.copyOfRange;

/*
System.out.println(""); 对应于  to_client_sout(socket,"");
nextInt()  对应于  from_client_int(socket)
next()  对应于  from_client_next(socket)

服务器这边只有一个to_client_sout，客户端就必须有一个from_server
服务器这边只有一个 from_client_，客户端就必须有一个 to_server
 */


public class Server {
    public static void main(String[] args) {
        System.out.println("");
        Socket socket = server_init();
        String hello = "Welcome to Blogs system!\n";
        to_client_sout(socket, hello);
        String s = from_client_next(socket);
        System.out.println("From Client :" + s);
        to_client_sout(socket, hello);
        int i = from_client_int(socket);
        System.out.println("From Client :" + i);
    }

    public static Socket server_init() {
        ServerSocket server;
        Socket socket1 = new Socket();
        try {
            server = new ServerSocket(7878);
            System.out.println("==服务器启动成功==");
            Socket socket = server.accept();
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket1;
    }


    public static String from_client_next(Socket socket) {
        InputStream in;
        while (true) {
            try {
                in = socket.getInputStream();
                byte[] b = new byte[512];
                StringBuffer sb = new StringBuffer();
                String s;
                int x = in.read(b);
                if (x != -1) {
                    byte[] c = copyOfRange(b, 0, x);
                    s = new String(c);
                    sb.append(s);
                }
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int from_client_int(Socket socket) {
        InputStream in;
        while (true) {
            try {
                in = socket.getInputStream();
                byte[] b = new byte[512];
                StringBuffer sb = new StringBuffer();
                String s;
                int x = in.read(b);
                if (x != -1) {
                    byte[] c = copyOfRange(b, 0, x);
                    s = new String(c);
                    sb.append(s);
                }
                int i = Integer.parseInt(sb.toString());
                return i;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void to_client_sout(Socket socket, String s) {
        while (true) {
            try {
                OutputStream out = socket.getOutputStream();
                out.write(s.getBytes());
                out.flush();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
