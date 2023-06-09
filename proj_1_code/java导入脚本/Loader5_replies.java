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
/*
数据传输上提高了效率，
 */

public class Loader5_replies {
    private static final int BATCH_SIZE = 1000;
    private static Connection con = null;
    private static PreparedStatement stmt = null;
    public static List<Replies> replies = new ArrayList<>();
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
            stmt = con.prepareStatement("INSERT INTO public.Replies (postID, replyContent, replyStars, replyAuthor, secondaryReplyContent,secondaryReplyStars,secondaryReplyAuthor) " +
                    "VALUES (?,?,?,?,?,?,?);");
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

    private static void loadData(Replies replies) {
//        String[] lineData = line.split(";");
        if (con != null) {
            try {
                stmt.setInt(1, replies.getPostID());
                stmt.setString(2, replies.getReplyContent());
                stmt.setInt(3, replies.getReplyStars());
                stmt.setString(4, replies.getReplyAuthor());
                stmt.setString(5, replies.getSecondaryReplyContent());
                stmt.setInt(6, replies.getSecondaryReplyStars());
                stmt.setString(7, replies.getSecondaryReplyAuthor());
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
                stmt0.executeUpdate("drop table replies;");
                con.commit();
                stmt0.executeUpdate("create table Replies\n" +
                        "(\n" +
                        "    postID                int,\n" +
                        "    replyContent          varchar,\n" +
                        "    replyStars            int,\n" +
                        "    replyAuthor           varchar,\n" +
                        "    secondaryReplyContent varchar,\n" +
                        "    secondaryReplyStars   int,\n" +
                        "    secondaryReplyAuthor  varchar\n" +
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
        String fileName = "D:\\AAAA\\study\\cs307数据库\\proj_1\\resources\\replies.json";
        try (JsonReader reader = new JsonReader(new FileReader(fileName))) {
            Gson gson = new Gson();
            reader.beginArray(); // 读取JSON数组的开头
            while (reader.hasNext()) {
                Replies replies1 = gson.fromJson(reader, Replies.class);
//                System.out.println(post.getPostID());
                replies.add(replies1);
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
            for (Replies replies1 : replies) {
//                if (line.startsWith("movieid"))
//                    continue; // skip the first line
                loadData(replies1);//do insert command
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

