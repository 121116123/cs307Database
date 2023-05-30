import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static java.util.Arrays.copyOfRange;

public class Client {
    public static void main(String[] args) {
        Socket socket = client_init();
        Scanner in = new Scanner(System.in);
        while(true){
            String s1 = from_server(socket);
            if (s1.charAt(s1.length() -1) == '*'){
                String s2 = s1.substring(0,s1.length()-1);
                System.out.println("[From server]" + s2);
            }else{
                System.out.println("[From server]" + s1);
                String s = in.nextLine();
                to_server(socket, s);
            }

        }
    }

    public static Socket client_init() {
        Socket socket = null;
        System.out.println("---客户端启动---");
        try {
            //绑定到本地端口
            socket = new Socket("127.0.0.1", 7878);
            System.out.println("---客户端成功连接到服务器---");
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }

    public static String from_server(Socket socket) {
        while (true) {
            InputStream in;
            try {
                in = socket.getInputStream();
                byte[] b = new byte[8192];
                StringBuffer sb = new StringBuffer();
                String s;
                int x = in.read(b);
                if (x != -1) {
                    byte[] c = copyOfRange(b, 0, x);
                    s = new String(c);
                    sb.append(s);
                }
                //in.close();
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void to_server(Socket socket, String s) {
        while (true) {
            try {
                OutputStream out = socket.getOutputStream();
                out.write(s.getBytes());
                out.flush();
                //out.close();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
