import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static java.util.Arrays.copyOfRange;

public class Client {
    public static void main(String[] args) {
        OutputStream out = null;
        Socket socket = null;
        System.out.println("---客户端启动成功---");
        try {
            //绑定到本地端口
            socket = new Socket("127.0.0.1", 7878);
            System.out.println("---客户端成功连接到服务器---");
            //发送消息
            while (true) {
                out = socket.getOutputStream();
                //输入文字，从控制台输入
                Scanner san = new Scanner(System.in);
                String str = san.nextLine();
                //System.out.println("我:" + str);
                out.write(str.getBytes());
                out.flush();
                //接收信息
                InputStream in = socket.getInputStream();
                //获取输入流里面数据并存储数据
                byte[] b = new byte[512];
                StringBuffer sb = new StringBuffer();
                String s;
                int x = in.read(b);
                if(x !=-1){
                    byte [] c = copyOfRange(b, 0, x);
                    s=new String(c);
                    sb.append(s);
                }
                System.out.println("[From Server] " + sb);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
