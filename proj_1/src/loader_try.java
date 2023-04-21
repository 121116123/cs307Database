
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//public class loader_try {
//    public static void main(String[] args) {
//        String fileName = "D:\\AAAA\\study\\cs307数据库\\proj_1\\src\\posts.json";
//        List<Post> posts = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
//            Gson gson = new Gson();
//            String line;
//            while ((line = br.readLine()) != null) {
//                // 解析JSON字符串并转换为Java对象
//                Post post = gson.fromJson(line, Post.class);
//                posts.add(post);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}

public class loader_try {
    public static void main(String[] args) {
        String fileName = "D:\\AAAA\\study\\cs307数据库\\lab的文件\\project data and scripts\\project data and scripts\\posts.json";
        List<Post> posts = new ArrayList<>();
        try (JsonReader reader = new JsonReader(new FileReader(fileName))) {
            Gson gson = new Gson();
            reader.beginArray(); // 读取JSON数组的开头
            while (reader.hasNext()) {
                Post post = gson.fromJson(reader, Post.class);
//                System.out.println(post.getTitle());
                posts.add(post);
            }
            reader.endArray(); // 读取JSON数组的结尾
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


