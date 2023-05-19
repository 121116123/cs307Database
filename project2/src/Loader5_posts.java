import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Loader5_posts {
    private static final int BATCH_SIZE = 1000;
    private static Connection con = null;
    private static PreparedStatement stmt = null;
    public static List<Posts> posts = new ArrayList<>();

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

    public static void setPrepareStatement() {
        try {
            stmt = con.prepareStatement("INSERT INTO public.posts (postID, title, category, content, postingTime,postingCity,Author,authorRegistrationTime,authorID,authoPhone,authorFollowedBy,authorFavorite,authorShared,authorLiked) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
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

    private static void loadData(Posts post) {
        if (con != null) {
            try {
                stmt.setInt(1, post.getPostID());
                stmt.setString(2, post.getTitle());
                Array categoryArray = con.createArrayOf("VARCHAR", post.getCategory().toArray());
                stmt.setArray(3, categoryArray);
                stmt.setString(4, post.getContent());
                stmt.setTimestamp(5, post.getPostingTime());
                stmt.setString(6, post.getPostingCity());
                stmt.setString(7, post.getAuthor());
                stmt.setTimestamp(8, post.getAuthorRegistrationTime());
                stmt.setString(9, post.getAuthorID());
                stmt.setString(10, post.getAuthoPhone());
                Array AuthorFollowedBy = con.createArrayOf("VARCHAR", post.getAuthorFollowedBy().toArray());
                stmt.setArray(11, AuthorFollowedBy);
                Array AuthorFavorite = con.createArrayOf("VARCHAR", post.getAuthorFavorite().toArray());
                stmt.setArray(12, AuthorFavorite);
                Array AuthorShared = con.createArrayOf("VARCHAR", post.getAuthorShared().toArray());
                stmt.setArray(13, AuthorShared);
                Array AuthorLiked = con.createArrayOf("VARCHAR", post.getAuthorLiked().toArray());
                stmt.setArray(14, AuthorLiked);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void clearDataInTable() {
        Statement stmt0;
        if (con != null) {
            try {
                stmt0 = con.createStatement();
                stmt0.executeUpdate("drop table posts;");
                con.commit();
                stmt0.executeUpdate("create table posts\n" +
                        "(\n" +
                        "    postID                 int,\n" +
                        "    title                  varchar,\n" +
                        "    category               varchar(255)[],\n" +
                        "    content                varchar,\n" +
                        "    postingTime            timestamp ,\n" +
                        "    postingCity            varchar,\n" +
                        "    Author                 varchar,\n" +
                        "    authorRegistrationTime timestamp ,\n" +
                        "    authorID               varchar,\n" +
                        "    authoPhone             varchar,\n" +
                        "    authorFollowedBy       varchar(255)[],\n" +
                        "    authorFavorite         varchar(255)[],\n" +
                        "    authorShared           varchar(255)[],\n" +
                        "    authorLiked            varchar(255)[]\n" +
                        "\n" +
                        ");");
                con.commit();
                stmt0.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void main(String[] args) {
        Properties prop = loadDBUser();
        String fileName = "D:\\AAAA\\study\\cs307数据库\\proj_1\\resources\\posts.json";

        try (JsonReader reader = new JsonReader(new FileReader(fileName))) {
            Gson gson = new Gson();
            reader.beginArray(); // 读取JSON数组的开头
            while (reader.hasNext()) {
                Posts post = gson.fromJson(reader, Posts.class);
                posts.add(post);
            }
            reader.endArray(); // 读取JSON数组的结尾
        } catch (IOException e) {
            e.printStackTrace();
        }
        openDB(prop);
        clearDataInTable();
        closeDB();
        int cnt = 0;
        long start = System.currentTimeMillis();
        openDB(prop);
        setPrepareStatement();

        try {
            for (Posts post : posts) {
//                if (line.startsWith("movieid"))
//                    continue; // skip the first line
                loadData(post);//do insert command
                if (cnt % BATCH_SIZE == 0&&cnt!=0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
                cnt++;
            }

            if (cnt % BATCH_SIZE != 0) {
                stmt.executeBatch();
            }
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        closeDB();
        long end = System.currentTimeMillis();
        long totalTime = end - start;
        System.out.println("程序运行时间为：" + totalTime + "毫秒");
        System.out.println(cnt + " records successfully loaded");
        System.out.println("Loading speed : " + (cnt * 1000L) / (end - start) + " records/s");

    }
}

