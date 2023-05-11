import Util.OrdinaryUtil;
import Util.ProxoolUtil;
import dao.DataManipulation;
import dao.DatabaseManipulation;

public class Client {

    public static void main(String[] args) {

        try {
            dbRequestArrived(3);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//过了1秒之后又有5个访问请求，每次都要有个新的链接
            dbRequestArrived(5);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void dbRequestArrived(int count) {
        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                DataManipulation dm = new DatabaseManipulation(ProxoolUtil.getInstance());//这个是通过连接池创建的链接
                dm.getConnection();
                dm.findStationsByLine(1);
                dm.closeConnection();
            }).start();
        }
    }
}

