import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.sql.*;

public class Loader1_posts {
    private static Connection con = null;
    private static Statement stmt = null;
    private static List<Posts> posts = new ArrayList<>();

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
//            if (con != null) {
//                System.out.println("Successfully connected to the database "
//                        + prop.getProperty("database") + " as " + prop.getProperty("user"));
//            }
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
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
            return properties;
        } catch (IOException e) {
            System.err.println("can not find db user file");
            throw new RuntimeException(e);
        }
    }

//    private static List<String> loadTXTFile() {
//        try {
//            return Files.readAllLines(Paths.get("resources/movies.txt"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static void loadData(Posts post) {
        String categoryString = String.join(",", post.getCategory());
        String PostingTime = post.getPostingTime().toString();
        String AuthorRegistrationTime = post.getAuthorRegistrationTime().toString();
        String AuthorFollowedBy = String.join(",", post.getAuthorFollowedBy());

        String AuthorFavorite = String.join(",", post.getAuthorFavorite());
        String AuthorLiked = String.join(",", post.getAuthorLiked());
        String AuthorShared = String.join(",", post.getAuthorShared());


        String sql = String.format("INSERT INTO public.posts (postID, title, category, content, postingTime,postingCity,Author,authorRegistrationTime,authorID,authoPhone,authorFollowedBy,authorFavorite,authorShared,authorLiked) " +
                        "VALUES (%d,'%s', '%s', %s, %s,%s,'%s', '%s',%s,'%s', '%s', %s, %s,%s);",
                post.getPostID(), post.getTitle(), categoryString, post.getContent(), PostingTime, post.getPostingCity(), post.getAuthor(), AuthorRegistrationTime, post.getAuthorID(), post.getAuthoPhone(), AuthorFollowedBy, AuthorFavorite, AuthorShared, AuthorLiked);

    }

    public static void clearDataInTable() {
        Statement stmt0;
        if (con != null) {
            try {
                stmt0 = con.createStatement();
                stmt0.executeUpdate("drop table Posts;");
                stmt0.executeUpdate("create table Posts\n" +
                        "(\n" +
                        "    postID                 int unique,\n" +
                        "    title                  varchar,\n" +
                        "    category               varchar[],\n" +
                        "    content                varchar,\n" +
                        "    postingTime            timestamp,\n" +
                        "    postingCity            varchar,\n" +
                        "    Author                 varchar unique,\n" +
                        "    authorRegistrationTime timestamp,\n" +
                        "    authorID               varchar,\n" +
                        "    authoPhone             varchar,\n" +
                        "    authorFollowedBy       varchar[],\n" +
                        "    authorFavorite         varchar[],\n" +
                        "    authorShared           varchar[],\n" +
                        "    authorLiked            varchar[]\n" +
                        "\n" +
                        ");");
                stmt0.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void main(String[] args) {
        Properties prop = loadDBUser();
        String fileName = "D:\\AAAA\\study\\cs307数据库\\proj_1\\resources\\posts.json";

        // Empty target table

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


        openDB(prop);
        clearDataInTable();
        closeDB();

        int cnt = 0;

        long start = System.currentTimeMillis();

        for (Posts post : posts) {// skip the first line
            openDB(prop);
            loadData(post);//do insert command
            closeDB();

            cnt++;
            if (cnt % 1000 == 0) {
                System.out.println("insert " + 1000 + " data successfully!");
            }
        }


        long end = System.currentTimeMillis();
        long totalTime = end - start;
        System.out.println("程序运行时间为：" + totalTime + "毫秒");
        System.out.println();
        System.out.println("Loading speed : " + (cnt * 1000L) / (end - start) + " records/s");
    }
}
