import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get("D:\\AAAA\\study\\cs307数据库\\project data and scripts\\project data and scripts\\posts.json"));
            String jsonStrings = new String(fileBytes, StandardCharsets.UTF_8);

//            String jsonStrings = Files.readString(Path.of("resource/posts.json"));
            List<Post> posts = JSON.parseArray(jsonStrings,Post.class);
            posts.forEach(System.out::println);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
