import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
/*
数据传输上提高了效率，
 */

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
        //jdbc:postgresql://localhost:5432/proj_1
//        String url = "jdbc:postgresql://" + prop.getProperty("localhost:5432") + "/" + prop.getProperty("proj_1");
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
//
//    private static List<String> loadTXTFile() {
//        try {
//            return Files.readAllLines(Paths.get("D:\\AAAA\\study\\cs307数据库\\proj_1\\src\\posts.json"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static void loadData(Posts post) {
//        String[] lineData = line.split(";");
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
        System.out.println("zuikaishi");
        Properties prop = loadDBUser();
        String fileName = "D:\\AAAA\\study\\cs307数据库\\proj_1\\resources\\posts.json";
        try (JsonReader reader = new JsonReader(new FileReader(fileName))) {
            Gson gson = new Gson();
            reader.beginArray(); // 读取JSON数组的开头
            while (reader.hasNext()) {
                Posts post = gson.fromJson(reader, Posts.class);
//                System.out.println(post.getPostID());
                posts.add(post);
//                System.out.println(post.getPostingTime());
            }
            reader.endArray(); // 读取JSON数组的结尾
        } catch (IOException e) {
            e.printStackTrace();
        }


//        List<String> lines = loadTXTFile();

        // Empty target table
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
        long end = System.currentTimeMillis();
        System.out.println(cnt + " records successfully loaded");
        System.out.println("Loading speed : " + (cnt * 1000L) / (end - start) + " records/s");

    }
}

