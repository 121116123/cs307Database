
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class loader_try {
    public static void main(String[] args) {
        String fileName = "data.json";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            Gson gson = new Gson();
            String line;
            while ((line = br.readLine()) != null) {
                // 解析JSON字符串并转换为Java对象
                Post post = gson.fromJson(line, Post.class);
                System.out.println(post.getTitle());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

