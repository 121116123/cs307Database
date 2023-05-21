//
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//
//public class main {
//    public static List<author> authors = loader.authors;
//    public static List<post> posts = loader.posts;
//    public static List<reply> replies = loader.replies;
//    public static List<subreply> subreplies = new ArrayList<>();
//    public static List<category> categories = new ArrayList<>();
//
//    public static void main(String[] args) {
//        loader.main(args);
//        long time = System.currentTimeMillis();
//        Timestamp ts = new Timestamp(time);
//        set_new_author("000607395779522088", ts, "18931350071", "zuy");
//    }
//
//    public static void set_new_author(String id, Timestamp reg_time, String phone, String name) {
//        author author = new author();
//        author.setID(id);
//        author.setRegistration_time(reg_time);
//        author.setPhone(phone);
//        author.setName(name);
//        authors.add(author);
//
//        Properties prop = loader.loadDBUser();
//        loader.openDB(prop);
//        loader.setPrepareStatement_author();
//        try {
//            loader.loadData_author(author);//do insert command
//            loader.stmt.executeBatch();
//            loader.con.commit();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }
//}
