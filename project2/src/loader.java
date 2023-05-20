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
import java.util.Random;

public class loader {
    public static final int BATCH_SIZE = 1000;
    public static Connection con = null;
    public static PreparedStatement stmt = null;

    public static List<Posts> posts_0 = new ArrayList<>();
    public static List<Replies> replies_0 = new ArrayList<>();
    public static List<author> authors = new ArrayList<>();
    public static List<post> posts = new ArrayList<>();
    public static List<reply> replies = new ArrayList<>();
    public static List<subreply> subreplies = new ArrayList<>();
    public static List<category> categories = new ArrayList<>();

    public static void openDB(Properties prop) {
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

    public static void setPrepareStatement_post() {
        try {
            System.out.println("set pre post");
            stmt = con.prepareStatement("insert into post(id, title, content, posting_time, posting_city)  " +
                    "VALUES (?,?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void setPrepareStatement_category() {
        try {
            stmt = con.prepareStatement("insert into category(content)  " +
                    "VALUES (?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public static void setPrepareStatement_reply() {
        try {
            stmt = con.prepareStatement("insert into reply(id,author, content, postID, stars)   " +
                    "VALUES (?,?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    //这里有错，主要是replyid不好拿，要不改成replycontent？
    public static void setPrepareStatement_subreply() {
        try {
            stmt = con.prepareStatement("insert into subreply(id,author, content, stars, replyid)\n" +
                    "VALUES (?,?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }


    public static void closeDB() {
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

    public static Properties loadDBUser() {
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

    public static void loadData_author(author author) {
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

    public static void loadData_post(post post) {
//        String[] lineData = line.split(";");
        if (con != null) {
            try {
                stmt.setInt(1, post.getID());
                stmt.setString(2, post.getTitle());
                stmt.setString(3, post.getContent());
                stmt.setTimestamp(4, post.getPosting_time());
                stmt.setString(5, post.getPosting_city());
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void loadData_category(category category) {
//        String[] lineData = line.split(";");
        if (con != null) {
            try {
                stmt.setString(1, category.getContent());
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void loadData_reply(reply reply) {
//        String[] lineData = line.split(";");
        if (con != null) {
            try {
                stmt.setInt(1, reply.getId());
                stmt.setString(2, reply.getAuthor());
                stmt.setString(3, reply.getContent());
                stmt.setInt(4, reply.getPostID());
                stmt.setInt(5, reply.getStars());
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    public static void loadData_subreply(subreply subreply) {
//        String[] lineData = line.split(";");
        if (con != null) {
            try {
                stmt.setInt(1, subreply.getId());
                stmt.setString(2, subreply.getAuthor());
                stmt.setString(3, subreply.getContent());
                stmt.setInt(4, subreply.getStars());
                stmt.setInt(5, subreply.getReplyid());
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
                        "    name              varchar unique not null\n" +
                        ");");
                con.commit();
                stmt0.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void clearDataInTable_post() {
        System.out.println("cleardatainpost");
        Statement stmt0;
        if (con != null) {
            try {
                stmt0 = con.createStatement();
                stmt0.executeUpdate("drop table post cascade ;");
                con.commit();
                stmt0.executeUpdate("create table post\n" +
                        "(\n" +
                        "    /*\n" +
                        "     id是已知的postid\n" +
                        "     title是已知\n" +
                        "     content是已知\n" +
                        "     postingtime是已知的时间戳\n" +
                        "     city已知\n" +
                        "     */\n" +
                        "    ID           int primary key unique not null,\n" +
                        "    title        varchar,\n" +
                        "    content      varchar,\n" +
                        "    posting_time timestamp,\n" +
                        "    posting_city varchar\n" +
                        ");\n");
                con.commit();
                stmt0.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void clearDataInTable_category() {
        Statement stmt0;
        if (con != null) {
            try {
                stmt0 = con.createStatement();
                stmt0.executeUpdate("drop table category cascade ;");
                con.commit();
                stmt0.executeUpdate("create table category(\n" +
                        "    content varchar primary key\n" +
                        ");\n");
                con.commit();
                stmt0.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    public static void clearDataInTable_reply() {
        Statement stmt0;
        if (con != null) {
            try {
                stmt0 = con.createStatement();
                stmt0.executeUpdate("drop table if exists reply cascade;");
                con.commit();
                stmt0.executeUpdate("create table reply\n" +
                        "(\n" +
                        "    /*\n" +
                        "     id是自增的，每个reply有独一无二的id\n" +
                        "     author已知\n" +
                        "     content已知\n" +
                        "     post是指向post的一个外键\n" +
                        "     stars已知\n" +
                        "     groupnum是按照postid分组后的编号\n" +
                        "     */\n" +
                        "    id        int primary key,\n" +
                        "    author    varchar,\n" +
                        "    content   varchar not null,\n" +
                        "    postID    int     not null,\n" +
                        "    stars     int,\n" +
                        "--     group_num int,\n" +
                        "    foreign key (author) references author (name),\n" +
                        "    foreign key (postID) references post (ID)\n" +
                        ")");
                con.commit();
                stmt0.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void clearDataInTable_subreply() {
        Statement stmt0;
        if (con != null) {
            try {
                stmt0 = con.createStatement();
                stmt0.executeUpdate("drop table subreply cascade;");
                con.commit();
                stmt0.executeUpdate("create table subreply\n" +
                        "(\n" +
                        "    /*\n" +
                        "     id是根据replyid分组后的编号\n" +
                        "     authorid content stars已知\n" +
                        "     replyid是指向reply的外键\n" +
                        "     */\n" +
                        "    id      int,\n" +
                        "    author  varchar,\n" +
                        "    content varchar not null,\n" +
                        "    stars   int,\n" +
                        "    replyid int     not null,\n" +
                        "    primary key (id, replyid),\n" +
                        "    foreign key (replyid) references reply (id),\n" +
                        "    foreign key (author) references author(name)\n" +
                        ");");
                con.commit();
                stmt0.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void put_authors() {
        int id_length = posts_0.get(0).getAuthorID().length();
        String alphabet = "0123456789";
        Random random = new Random();
        Timestamp earliest = posts_0.get(0).getAuthorRegistrationTime();
        Timestamp latest = posts_0.get(0).getAuthorRegistrationTime();


        for (int i = 0; i < posts_0.size(); i++) {
            author author = new author();
            Posts posts_1 = posts_0.get(i);
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
        for (int i = 0; i < posts_0.size(); i++) {
            Posts posts1 = posts_0.get(i);
            for (int j = 0; j < posts1.getAuthorFollowedBy().size(); j++) {
                boolean exist = false;
                for (author value : authors) {
                    if (value.getName().equals(posts1.getAuthorFollowedBy().get(j))) {
                        exist = true;
                        break;
                    }
                }
                if (exist) {
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
        for (int i = 0; i < posts_0.size(); i++) {
            Posts posts1 = posts_0.get(i);
            for (int j = 0; j < posts1.getAuthorFavorite().size(); j++) {
                boolean exist = false;
                for (author value : authors) {
                    if (value.getName().equals(posts1.getAuthorFavorite().get(j))) {
                        exist = true;
                        break;
                    }
                }
                if (exist) {
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
        for (int i = 0; i < posts_0.size(); i++) {
            Posts posts1 = posts_0.get(i);
            for (int j = 0; j < posts1.getAuthorShared().size(); j++) {
                boolean exist = false;
                for (author value : authors) {
                    if (value.getName().equals(posts1.getAuthorShared().get(j))) {
                        exist = true;
                        break;
                    }
                }
                if (exist) {
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
        for (int i = 0; i < posts_0.size(); i++) {
            Posts posts1 = posts_0.get(i);
            for (int j = 0; j < posts1.getAuthorLiked().size(); j++) {
                boolean exist = false;
                for (author value : authors) {
                    if (value.getName().equals(posts1.getAuthorLiked().get(j))) {
                        exist = true;
                        break;
                    }
                }
                if (exist) {
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
        for (int i = 0; i < replies_0.size(); i++) {
            Replies reply = replies_0.get(i);
            boolean exist = false;
            for (author value : authors) {
                if (value.getName().equals(reply.getReplyAuthor())) {
                    exist = true;
                    break;
                }
            }
            if (exist) {
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
        for (int i = 0; i < replies_0.size(); i++) {
            Replies reply = replies_0.get(i);
            boolean exist = false;
            for (author value : authors) {
                if (value.getName().equals(reply.getSecondaryReplyAuthor())) {
                    exist = true;
                    break;
                }
            }
            if (exist) {
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

    public static void put_posts() {
        System.out.println("put posts");
        for (int i = 0; i < posts_0.size(); i++) {
            post post = new post();
            Posts posts_1 = posts_0.get(i);
            post.setID(posts_1.getPostID());
            post.setContent(posts_1.getContent());
            post.setTitle(posts_1.getTitle());
            post.setPosting_time(posts_1.getPostingTime());
            post.setPosting_city(posts_1.getPostingCity());
            posts.add(post);
        }
    }

    public static void put_categories() {
        for (int i = 0; i < posts_0.size(); i++) {
            for (int j = 0; j < posts_0.get(i).getCategory().size(); j++) {
                String cate = posts_0.get(i).getCategory().get(j);
                boolean exist = false;
                for (int k = 0; k < categories.size(); k++) {
                    if (cate.equals(categories.get(k).getContent())) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    category category = new category();
                    category.setContent(cate);
                    categories.add(category);
                }
            }

        }
    }

    //去重
    public static void put_replies() {
        int id = 0;
        for (int i = 0; i < replies_0.size(); i++) {
            Replies replies_1 = replies_0.get(i);
            boolean exist = false;
            for (int j = 0; j < replies.size(); j++) {
                if (replies_1.getReplyContent().equals(replies.get(j).getContent())) {
                    exist = true;
                    break;
                }
            }
            if (exist) {
                continue;
            }
            reply reply = new reply();
            reply.setId(id);
            id++;
            reply.setContent(replies_1.getReplyContent());
            reply.setAuthor(replies_1.getReplyAuthor());
            reply.setStars(replies_1.getReplyStars());
            reply.setPostID(replies_1.getPostID());
            replies.add(reply);
        }
    }

    //需要replyid
    public static void put_subreplies() {
        for (int i = 0; i < replies_0.size(); i++) {
            Replies replies1 = replies_0.get(i);
            subreply subreply = new subreply();
            subreply.setId(i);
            subreply.setAuthor(replies1.getSecondaryReplyAuthor());
            subreply.setContent(replies1.getSecondaryReplyContent());
            for (int j = 0; j < replies.size(); j++) {
                if (replies.get(j).getContent().equals(replies_0.get(i).getReplyContent())) {
                    subreply.setReplyid(j);
                    break;
                } else subreply.setReplyid(0);
            }
            subreply.setStars(replies1.getSecondaryReplyStars());
            subreplies.add(subreply);
        }
    }


    public static void main(String[] args) {
        Properties prop = loadDBUser();
//读两个json文件，建好了两个list
        String fileName_posts = "D:\\AAAA\\study\\cs307数据库\\project2\\resources\\posts.json";
        try (JsonReader reader = new JsonReader(new FileReader(fileName_posts))) {
            Gson gson = new Gson();
            reader.beginArray(); // 读取JSON数组的开头
            while (reader.hasNext()) {
                Posts Posts = gson.fromJson(reader, Posts.class);
                posts_0.add(Posts);
            }
            reader.endArray(); // 读取JSON数组的结尾
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileName_replies = "D:\\AAAA\\study\\cs307数据库\\project2\\resources\\replies.json";
        try (JsonReader reader = new JsonReader(new FileReader(fileName_replies))) {
            Gson gson = new Gson();
            reader.beginArray(); // 读取JSON数组的开头
            while (reader.hasNext()) {
                Replies replies1 = gson.fromJson(reader, Replies.class);
//                System.out.println(post.getPostID());
                replies_0.add(replies1);
            }
            reader.endArray(); // 读取JSON数组的结尾
        } catch (IOException e) {
            e.printStackTrace();
        }
//把两个原始数据的list分拆成需要的list们

        put_authors();
        put_posts();
        put_categories();
        put_replies();
        put_subreplies();

        //要不要清零
        openDB(prop);
        clearDataInTable_author();
        clearDataInTable_post();
        clearDataInTable_category();
        clearDataInTable_reply();
        clearDataInTable_subreply();
        closeDB();

        int cnt = 0;
        openDB(prop);
        //这里要都写掉
        setPrepareStatement_author();
        try {
            for (author author : authors) {
                loadData_author(author);//do insert command
                if (cnt % BATCH_SIZE == 0 && cnt != 0) {
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

        setPrepareStatement_post();

        try {
            for (post post : posts) {
                loadData_post(post);
                if (cnt % BATCH_SIZE == 0 && cnt != 0) {
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

        setPrepareStatement_category();
        try {
            for (category category : categories
            ) {
                loadData_category(category);
                if (cnt % BATCH_SIZE == 0 && cnt != 0) {
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

        setPrepareStatement_reply();
        try {
            for (reply reply : replies
            ) {
                loadData_reply(reply);
                if (cnt % BATCH_SIZE == 0 && cnt != 0) {
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


        setPrepareStatement_subreply();
        try {
            for (subreply subreply : subreplies
            ) {
                loadData_subreply(subreply);
                if (cnt % BATCH_SIZE == 0 && cnt != 0) {
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
    }
}
