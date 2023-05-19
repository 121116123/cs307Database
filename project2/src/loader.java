import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class loader {
    private static final int BATCH_SIZE = 1000;
    private static Connection con = null;
    private static PreparedStatement stmt = null;

    public static List<Posts> posts_0 = new ArrayList<>();
    public static List<Replies> replies_0 = new ArrayList<>();
    public static List<author> authors = new ArrayList<>();
    public static List<post>posts=new ArrayList<>();
    public static List<reply> replies=new ArrayList<>();
    public static List<subreply> subreplies=new ArrayList<>();
    public static List<category> categories=new ArrayList<>();

    private static void openDB(Properties prop) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
            System.exit(1);
        }
        String url = "jdbc:postgresql://localhost:5432/proj_1";

        try {
            con = DriverManager.getConnection(url, prop);
            if (con != null) {
                System.out.println("Successfully connected to the database "
                        + prop.getProperty("database") + " as " + prop.getProperty("user"));
                con.setAutoCommit(false);
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void setPrepareStatement_author() {
        try {
            stmt = con.prepareStatement("INSERT INTO public.author (ID, registration_time, phone, name) " +
                    "VALUES (?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static void closeDB() {
        if (con != null) {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                con.close();
                con = null;
            } catch (Exception ignored) {
            }
        }
    }

    private static Properties loadDBUser() {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream("D:\\AAAA\\study\\cs307数据库\\proj_1\\resources\\dbUser.properties")));
            System.out.println(properties);
            return properties;
        } catch (IOException e) {
            System.err.println("can not find db user file");
            throw new RuntimeException(e);

        }
    }

    private static void loadData_author(author author) {
//        String[] lineData = line.split(";");
        if (con != null) {
            try {
                stmt.setString(1, author.getID());
                stmt.setTimestamp(2, author.getRegistration_time());
                stmt.setString(3, author.getPhone());
                stmt.setString(4, author.getName());
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void clearDataInTable_author() {
        Statement stmt0;
        if (con != null) {
            try {
                stmt0 = con.createStatement();
                stmt0.executeUpdate("drop table author cascade ;");
                con.commit();
                stmt0.executeUpdate("create table author\n" +
                        "(\n" +
                        "    /*\n" +
                        "    id是已知数据中的和自己编造的\n" +
                        "    time 是已知和编造\n" +
                        "    phone是已给信息的\n" +
                        "    name是每种的author\n" +
                        "     */\n" +
                        "    ID                varchar unique primary key,\n" +
                        "    registration_time timestamp,\n" +
                        "    phone             varchar,\n" +
                        "    name              varchar not null\n" +
                        ");");
                con.commit();
                stmt0.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }



    public static void put_posts_0(){

    }

    public static void put_replies_0(){

    }
    public static void put_authors() {
        List<Posts> posts = Loader5_posts.posts;
        List<Replies> replies = Loader5_replies.replies;

        int id_length = posts.get(0).getAuthorID().length();
        String alphabet = "0123456789";
        Random random = new Random();
        Timestamp earliest = posts.get(0).getAuthorRegistrationTime();
        Timestamp latest = posts.get(0).getAuthorRegistrationTime();


        for (int i = 0; i < posts.size(); i++) {
            author author = new author();
            Posts posts_1 = posts.get(i);
            author.setName(posts_1.getAuthor());
            author.setID(posts_1.getAuthorID());
            author.setPhone(posts_1.getAuthoPhone());
            author.setRegistration_time(posts_1.getAuthorRegistrationTime());
            if (earliest.after(posts_1.getAuthorRegistrationTime())) {
                earliest = posts_1.getAuthorRegistrationTime();
            }
            if (latest.before(posts_1.getAuthorRegistrationTime())) {
                latest = posts_1.getAuthorRegistrationTime();
            }
            authors.add(author);
        }


        long offset = earliest.getTime();
        long range = latest.getTime() - offset + 1; // 时间戳范围


        //follow
        for (int i = 0; i < posts.size(); i++) {
            Posts posts1 = posts.get(i);
            for (int j = 0; j < posts1.getAuthorFollowedBy().size(); j++) {
                boolean exist=false;
                for (author value : authors) {
                    if (value.getName().equals(posts1.getAuthorFollowedBy().get(j))) {
                        exist = true;
                        break;
                    }
                }
                if (exist){
                    continue;
                }
                author author = new author();
                author.setName(posts1.getAuthorFollowedBy().get(j));
                StringBuilder sb = new StringBuilder(id_length);
                for (int k = 0; k < id_length; k++) {
                    int index = random.nextInt(alphabet.length());
                    sb.append(alphabet.charAt(index));
                }
                String randomString = sb.toString();
                author.setID(randomString);
                long randomTimestamp = offset + random.nextLong() % range; // 生成随机时间戳
                Timestamp randomTime = new Timestamp(randomTimestamp);
                author.setRegistration_time(randomTime);
                authors.add(author);
            }
        }
        //favorite
        for (int i = 0; i < posts.size(); i++) {
            Posts posts1 = posts.get(i);
            for (int j = 0; j < posts1.getAuthorFavorite().size(); j++) {
                boolean exist=false;
                for (author value : authors) {
                    if (value.getName().equals(posts1.getAuthorFavorite().get(j))) {
                        exist = true;
                        break;
                    }
                }
                if (exist){
                    continue;
                }
                author author = new author();
                author.setName(posts1.getAuthorFavorite().get(j));
                StringBuilder sb = new StringBuilder(id_length);
                for (int k = 0; k < id_length; k++) {
                    int index = random.nextInt(alphabet.length());
                    sb.append(alphabet.charAt(index));
                }
                String randomString = sb.toString();
                author.setID(randomString);
                long randomTimestamp = offset + random.nextLong() % range; // 生成随机时间戳
                Timestamp randomTime = new Timestamp(randomTimestamp);
                author.setRegistration_time(randomTime);
                authors.add(author);
            }
        }
        //shared
        for (int i = 0; i < posts.size(); i++) {
            Posts posts1 = posts.get(i);
            for (int j = 0; j < posts1.getAuthorShared().size(); j++) {
                boolean exist=false;
                for (author value : authors) {
                    if (value.getName().equals(posts1.getAuthorShared().get(j))) {
                        exist = true;
                        break;
                    }
                }
                if (exist){
                    continue;
                }
                author author = new author();
                author.setName(posts1.getAuthorShared().get(j));
                StringBuilder sb = new StringBuilder(id_length);
                for (int k = 0; k < id_length; k++) {
                    int index = random.nextInt(alphabet.length());
                    sb.append(alphabet.charAt(index));
                }
                String randomString = sb.toString();
                author.setID(randomString);
                long randomTimestamp = offset + random.nextLong() % range; // 生成随机时间戳
                Timestamp randomTime = new Timestamp(randomTimestamp);
                author.setRegistration_time(randomTime);
                authors.add(author);
            }
        }

        //liked
        for (int i = 0; i < posts.size(); i++) {
            Posts posts1 = posts.get(i);
            for (int j = 0; j < posts1.getAuthorLiked().size(); j++) {
                boolean exist=false;
                for (author value : authors) {
                    if (value.getName().equals(posts1.getAuthorLiked().get(j))) {
                        exist = true;
                        break;
                    }
                }
                if (exist){
                    continue;
                }
                author author = new author();
                author.setName(posts1.getAuthorLiked().get(j));
                StringBuilder sb = new StringBuilder(id_length);
                for (int k = 0; k < id_length; k++) {
                    int index = random.nextInt(alphabet.length());
                    sb.append(alphabet.charAt(index));
                }
                String randomString = sb.toString();
                author.setID(randomString);
                long randomTimestamp = offset + random.nextLong() % range; // 生成随机时间戳
                Timestamp randomTime = new Timestamp(randomTimestamp);
                author.setRegistration_time(randomTime);
                authors.add(author);
            }
        }

        //reply
        for (int i = 0; i < replies.size(); i++) {
            Replies reply = replies.get(i);
            boolean exist=false;
            for (author value : authors) {
                if (value.getName().equals(reply.getReplyAuthor())) {
                    exist = true;
                    break;
                }
            }
            if (exist){
                continue;
            }
            author author = new author();
            author.setName(reply.getReplyAuthor());
            StringBuilder sb = new StringBuilder(id_length);
            for (int k = 0; k < id_length; k++) {
                int index = random.nextInt(alphabet.length());
                sb.append(alphabet.charAt(index));
            }
            String randomString = sb.toString();
            author.setID(randomString);
            long randomTimestamp = offset + random.nextLong() % range; // 生成随机时间戳
            Timestamp randomTime = new Timestamp(randomTimestamp);
            author.setRegistration_time(randomTime);
            authors.add(author);
        }


        //subreply
        for (int i = 0; i < replies.size(); i++) {
            Replies reply = replies.get(i);
            boolean exist=false;
            for (author value : authors) {
                if (value.getName().equals(reply.getSecondaryReplyAuthor())) {
                    exist = true;
                    break;
                }
            }
            if (exist){
                continue;
            }
            author author = new author();
            author.setName(reply.getSecondaryReplyAuthor());
            StringBuilder sb = new StringBuilder(id_length);
            for (int k = 0; k < id_length; k++) {
                int index = random.nextInt(alphabet.length());
                sb.append(alphabet.charAt(index));
            }
            String randomString = sb.toString();
            author.setID(randomString);
            long randomTimestamp = offset + random.nextLong() % range; // 生成随机时间戳
            Timestamp randomTime = new Timestamp(randomTimestamp);
            author.setRegistration_time(randomTime);
            authors.add(author);
        }
    }

    public static void put_posts(){

    }

    public static void put_categories(){

    }

    public static void put_replies(){

    }

    public static void put_subreplies(){}


    public static void main(String[] args) {
        Properties prop = loadDBUser();
        put_posts_0();
        put_replies_0();
        put_authors();
        put_posts();
        put_categories();
        put_replies();
        put_subreplies();

        //要不要清零
//        openDB(prop);
//        clearDataInTable();
//        closeDB();

        int cnt = 0;
        openDB(prop);
        //这里要都写掉
        setPrepareStatement_author();

        //这个也要都写
        try {
            for (author author : authors) {
//                if (line.startsWith("movieid"))
//                    continue; // skip the first line
                loadData_author(author);//do insert command
                if (cnt % BATCH_SIZE == 0 && cnt != 0) {
                    stmt.executeBatch();
                    System.out.println("insert " + BATCH_SIZE + " data successfully!");
                    stmt.clearBatch();
                }
                cnt++;
            }

            if (cnt % BATCH_SIZE != 0) {
                stmt.executeBatch();
                System.out.println("insert " + cnt % BATCH_SIZE + " data successfully!");

            }
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeDB();
    }
}
