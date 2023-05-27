import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static java.util.Arrays.copyOfRange;

public class Server {
    public static void main(String[] args) {
        ServerSocket server;
        try {
            server = new ServerSocket(7878);
            System.out.println("==服务器启动成功==");
            Socket socket=server.accept();
            while(true){
                InputStream in;
                try {
                    in = socket.getInputStream();
                    byte [] b=new byte[512];
                    StringBuffer sb=new StringBuffer();
                    String s;
                    int x = in.read(b);
                    if(x !=-1){
                        byte [] c = copyOfRange(b, 0, x);
                        s=new String(c);
                        sb.append(s);
                    }
                    OutputStream out=socket.getOutputStream();
                    System.out.println("[Form Client] "+sb);
                    Scanner sc=new Scanner(System.in);
                    String str=sc.nextLine();
                    out.write(str.getBytes());
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
