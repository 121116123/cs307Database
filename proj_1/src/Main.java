import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static   List<Author> authors = new ArrayList<>();
    public static void put_authors(String[] args) {
        List<Posts> posts = Loader5_posts.posts;
        List<Replies> replies = Loader5_replies.replies;

        int id_length = posts.get(0).getAuthorID().length();
        String alphabet = "0123456789";
        Random random = new Random();
        Timestamp earliest = posts.get(0).getAuthorRegistrationTime();
        Timestamp latest = posts.get(0).getAuthorRegistrationTime();


        for (int i = 0; i < posts.size(); i++) {
            Author author = new Author();
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


        //favorite
        for (int i = 0; i < posts.size(); i++) {
            Posts posts1 = posts.get(i);
            for (int j = 0; j < posts1.getAuthorFollowedBy().size(); j++) {
                Author author = new Author();
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
        //
        for (int i = 0; i < posts.size(); i++) {
            Posts posts1 = posts.get(i);
            for (int j = 0; j < posts1.getAuthorFavorite().size(); j++) {
                Author author = new Author();
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
        for (int i = 0; i < posts.size(); i++) {
            Posts posts1 = posts.get(i);
            for (int j = 0; j < posts1.getAuthorShared().size(); j++) {
                Author author = new Author();
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

        for (int i = 0; i < posts.size(); i++) {
            Posts posts1 = posts.get(i);
            for (int j = 0; j < posts1.getAuthorLiked().size(); j++) {
                Author author = new Author();
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

        for (int i = 0; i < replies.size(); i++) {
            Replies reply = replies.get(i);
            Author author = new Author();
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

        for (int i = 0; i < replies.size(); i++) {
            Replies reply = replies.get(i);
            Author author = new Author();
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
}
