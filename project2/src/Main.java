
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static java.util.Arrays.copyOfRange;


public class Main {
    public static List<author> authors = loader.authors;
    public static List<post> posts = loader.posts;
    public static List<reply> replies = loader.replies;
    public static List<subreply> subreplies = loader.subreplies;
    public static List<category> categories = loader.categories;
    public static List<author_favorite_post> author_favorite_posts = loader.author_favorite_posts;
    public static List<author_like_post> author_like_posts = loader.author_like_posts;
    public static List<author_send_post> author_send_posts = loader.author_send_posts;
    public static List<author_share_post> author_share_posts = loader.author_share_posts;
    public static List<leader_follower> leader_followers = loader.leader_followers;
    public static List<post_category> post_categories = loader.post_categories;
    public static List<author_send_reply> author_send_replies = loader.author_send_replies;
    public static List<author_send_subreply> author_send_subreplies = loader.author_send_subreplies;
    public static List<blocker_blocked> blocker_blockeds = new ArrayList<>();

    public static void main(String[] args) {

        Socket socket = server_init();

        to_client_sout_no_input(socket, "Waiting db loading...\n");
        load(args);

        String author_name = login_or_setup(socket);


        while (true) {
            to_client_sout_no_input(socket, "what do you want to do? you can choose a number from 1~9, each number represents an operation,\n");
            to_client_sout_no_input(socket, "1. You want to favorite, like, or share post,\n");
            to_client_sout_no_input(socket, "2. You want to view the favorite, like or share list of a post,\n");
            to_client_sout_no_input(socket, "3. you want to follow or unfollow other users, and you can also view the user list you have been followed.\n");
            to_client_sout_no_input(socket, "4. you want to create a post\n");
            to_client_sout_no_input(socket, "5. you want to reply a post or reply a reply\n");
            to_client_sout_no_input(socket, "6. you want to own your own posts or replies\n");
            to_client_sout_no_input(socket, "7. you want to block or unblock other person\n");
            to_client_sout_no_input(socket, "8. You want to see the more popular post\n");
            to_client_sout(socket, "9. you want to look for posts according to author and posting_time \n");


            int what_to_do = from_client_int(socket);
            to_client_sout_no_input(socket, "what_to_do: " + what_to_do + "\n");
            if (what_to_do == 1) {
                author_favorite_share_like_post(socket, author_name);
            } else if (what_to_do == 2) {
                view_favorite_share_like_list(socket);
            } else if (what_to_do == 3) {
                follow_others_or_cancel_follow_and_lookfor_follow_list(socket, author_name);
            } else if (what_to_do == 4) {
                send_post(socket, author_name);
            } else if (what_to_do == 5) {
                reply_post_or_reply_reply(socket, author_name);
            } else if (what_to_do == 6) {
                look_for_posts_or_replies_send(socket, author_name);
            } else if (what_to_do == 7) {
                block_or_unblock(socket, author_name);
            } else if (what_to_do == 8) {
                find_most_like_post(socket);
            } else if (what_to_do == 9) {
                find_post_by_time_and_author(socket);
            }

            to_client_sout(socket, "if you want to exit, please type 0, If you want to continue, please type 1");
            int res = from_client_int(socket);
            if (res == 0) {
                break;
            }
        }
    }

    public static Socket server_init() {
        ServerSocket server;
        Socket socket1 = new Socket();
        try {
            server = new ServerSocket(7878);
            System.out.println("==服务器启动成功==");
            Socket socket = server.accept();
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket1;
    }

    public static String from_client_next(Socket socket) {
        InputStream in;
        while (true) {
            try {
                in = socket.getInputStream();
                byte[] b = new byte[8192];
                StringBuffer sb = new StringBuffer();
                String s;
                int x = in.read(b);
                if (x != -1) {
                    byte[] c = copyOfRange(b, 0, x);
                    s = new String(c);
                    sb.append(s);
                }
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int from_client_int(Socket socket) {
        InputStream in;
        while (true) {
            try {
                in = socket.getInputStream();
                byte[] b = new byte[8192];
                StringBuffer sb = new StringBuffer();
                String s;
                int x = in.read(b);
                if (x != -1) {
                    byte[] c = copyOfRange(b, 0, x);
                    s = new String(c);
                    sb.append(s);
                }
                int i = Integer.parseInt(sb.toString());
                return i;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void to_client_sout(Socket socket, String s) {
        while (true) {
            try {
                OutputStream out = socket.getOutputStream();
                out.write(s.getBytes());
                out.flush();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void to_client_sout_no_input(Socket socket, String s) {
        while (true) {
            try {
                String s1 = s + "*";
                OutputStream out = socket.getOutputStream();
                out.write(s1.getBytes());
                out.flush();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void load(String[] args) {
        loader.main(args);

    }

    public static String login_or_setup(Socket socket) {
        //Scanner input = new Scanner(System.in);
        to_client_sout(socket, "if you already have an account, please type 0, If you want to register a new user, please type 1");
        int type = from_client_int(socket);
        String author_name = null;
        if (type == 0) {
            to_client_sout(socket, "please input your author name: ");
            String s = from_client_next(socket);
            for (author author : authors
            ) {
                if (author.getName().equals(s)) {
                    author_name = s;
                    break;
                }
            }
            if (author_name == null) {
                to_client_sout_no_input(socket, "not a valid name");
//                System.out.println("not a valid name");
                author_name = login_or_setup(socket);
            }
        } else {
            author_name = set_new_author(socket);
//            to_client_sout_no_input(socket, author_name);
        }
        return author_name;
    }

    public static String set_new_author(Socket socket) {
//        Scanner input = new Scanner(System.in);
        to_client_sout(socket, "please input your id: ");
        String id = from_client_next(socket);
        for (int i = 0; i < authors.size(); i++) {
            if (id.equals(authors.get(i).getID())) {
                to_client_sout(socket, "Your ID has already registered an account, and the nickname is" + authors.get(i).getName() + ". Please check if it is yours \n, If you want to use this account, please type 0. If you want to re-enter your id, please type 1 \n");
                int res = from_client_int(socket);
                if (res == 0) {
                    return authors.get(i).getName();
                } else {
                    to_client_sout(socket, "please re-enter your id: ");
                    id = from_client_next(socket);
                    i = 0;
                }
            }
        }
        long now_time = System.currentTimeMillis();
        Timestamp reg_time = new Timestamp(now_time);
        to_client_sout(socket, "please input your phone number: ");
        String phone = from_client_next(socket);
        to_client_sout(socket, "please input your name: ");
        String name = from_client_next(socket);
        for (int i = 0; i < authors.size(); i++) {
            if (authors.get(i).getName().equals(name)) {
                to_client_sout(socket, "This name has already been registered by someone else. Please change your name: ");
                name = from_client_next(socket);
                i = 0;
            }
        }
        author author = new author();
        author.setID(id);
        author.setRegistration_time(reg_time);
        author.setPhone(phone);
//        System.out.println(name);
        author.setName(name);
        authors.add(author);

        Properties prop = loader.loadDBUser();
        loader.openDB(prop);
        loader.setPrepareStatement_author();
        try {
            loader.loadData_author(author);//do insert command
            loader.stmt.executeBatch();
            loader.con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return name;
    }

    public static void author_favorite_share_like_post(Socket socket, String author_name) {
        to_client_sout(socket, "if you know which post you want to do with, please type 1, if you want to choose one according to category, please type 0");
        int postid = 0;
        //get postid
        int input = from_client_int(socket);
        if (input == 1) {
            to_client_sout(socket, "Please enter the ID of the post you want to operate");
            postid = from_client_int(socket);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < categories.size(); i++) {
                stringBuilder.append(i).append(" ").append(categories.get(i).getContent()).append("\n");
            }
            to_client_sout_no_input(socket, stringBuilder.toString());
            to_client_sout(socket, "The above is the categories we have. What kind of post do you want to read? Please enter the number before the category");
            int id = from_client_int(socket);
            String category_content = categories.get(id).getContent();
            to_client_sout_no_input(socket, "there are some posts about that, you can choose a postid to view the details");
            List<post> category_posts = new ArrayList<>();
            StringBuilder stringBuilder1 = new StringBuilder();
            for (post_category post_category : post_categories) {
                if (post_category.getCategory().equals(category_content)) {
                    stringBuilder1.append(post_category.getPostid()).append("\n");
                    category_posts.add(posts.get(post_category.getPostid()));
                }
            }
            to_client_sout_no_input(socket, stringBuilder1.toString());
            show_posts_list(socket, category_posts);
            to_client_sout(socket, "which post will you choose? Please select the post you want to see according to the above ID");
            postid = from_client_int(socket);
        }

        //do with post id
        for (post post : posts) {
            if (post.getID() == postid) {
                String sender = null;
                for (int i = 0; i < posts.size(); i++) {
                    if (postid == posts.get(i).getID()) {
                        sender = posts.get(i).getAuthor();
                        break;
                    }
                }
                boolean exist = false;
                for (blocker_blocked blocker_blocked : blocker_blockeds) {
                    if (blocker_blocked.getBlocker_name().equals(author_name) && blocker_blocked.getBlocked_name().equals(sender)) {
                        exist = true;
                        break;
                    }
                    break;
                }
                if (!exist) {
                    to_client_sout_no_input(socket, "the title of the post is: \n" + post.getTitle() + "\nthe content is: \n" + post.getContent() + "\n");
                    to_client_sout(socket, "type 1 to favorite it, type 2 to share it, type 3 to like it");
                    int what_to_do = from_client_int(socket);
                    if (what_to_do == 1) {
                        author_favorite_post author_favorite_post = new author_favorite_post();
                        author_favorite_post.setAuthor(author_name);
                        author_favorite_post.setPostid(post.getID());
                        author_favorite_posts.add(author_favorite_post);
                        Properties prop = loader.loadDBUser();
                        loader.openDB(prop);
                        loader.setPrepareStatement_author_favorite_post();
                        try {
                            loader.loadData_author_favorite_post(author_favorite_post);//do insert command
                            loader.stmt.executeBatch();
                            loader.con.commit();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    } else if (what_to_do == 2) {
                        author_share_post author_share_post = new author_share_post();
                        author_share_post.setAuthor(author_name);
                        author_share_post.setPostid(post.getID());
                        author_share_posts.add(author_share_post);

                        Properties prop = loader.loadDBUser();
                        loader.openDB(prop);
                        loader.setPrepareStatement_author_share_post();
                        try {
                            loader.loadData_author_share_post(author_share_post);//do insert command
                            loader.stmt.executeBatch();
                            loader.con.commit();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        author_like_post author_like_post = new author_like_post();
                        author_like_post.setAuthor(author_name);
                        author_like_post.setPostid(post.getID());
                        author_like_posts.add(author_like_post);

                        Properties prop = loader.loadDBUser();
                        loader.openDB(prop);
                        loader.setPrepareStatement_author_like_post();
                        try {
                            loader.loadData_author_like_post(author_like_post);//do insert command
                            loader.stmt.executeBatch();
                            loader.con.commit();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return;
                }
            }

        }
        to_client_sout(socket, "there is no that postid, please check the id, if you want to try again,please type 1, if you want to exit, please type 0");
        input = from_client_int(socket);
        if (input == 1) {
            author_favorite_share_like_post(socket, author_name);
        }
    }

    public static void view_favorite_share_like_list(Socket socket) {
        to_client_sout(socket, "please enter the id of the post you want to do with: ");
        int postid = from_client_int(socket);
        to_client_sout(socket, "which list do you want to view? enter 1 to see favorites, type 2 to see shares, type 3 to see likes");
        int type = from_client_int(socket);
        if (type == 1) {
            to_client_sout_no_input(socket, "who favorites the post: ");
            StringBuilder stringBuilder = new StringBuilder();
            for (author_favorite_post author_favorite_post : author_favorite_posts) {
                if (author_favorite_post.getPostid() == postid) {
                    String author = author_favorite_post.getAuthor();
                    stringBuilder.append(author);
                    stringBuilder.append("\n");
                }
            }
            to_client_sout_no_input(socket, stringBuilder.toString());
        } else if (type == 2) {
            to_client_sout_no_input(socket, "who shares the post: ");
            StringBuilder stringBuilder = new StringBuilder();
            for (author_share_post author_share_post : author_share_posts) {
                if (author_share_post.getPostid() == postid) {
                    stringBuilder.append(author_share_post.getAuthor());
                    stringBuilder.append("\n");
                }
            }
            to_client_sout_no_input(socket, stringBuilder.toString());

        } else if (type == 3) {
            to_client_sout_no_input(socket, "who likes the post: ");
            StringBuilder stringBuilder = new StringBuilder();
            for (author_like_post author_like_post : author_like_posts) {
                if (author_like_post.getPostid() == postid) {
                    stringBuilder.append(author_like_post.getAuthor());
                    stringBuilder.append("\n");
                }
            }
            to_client_sout_no_input(socket, stringBuilder.toString());
        }

    }

    public static void follow_others_or_cancel_follow_and_lookfor_follow_list(Socket socket, String author_name) {
        to_client_sout(socket, "please enter the author you want to follow or cancel follow");
        String wantToFollow = from_client_next(socket);
        String user = author_name;
        boolean isFollowing = false;//是否已经关注
        List<String> thisUserFollow = new ArrayList<>();
        for (leader_follower leader_follower : leader_followers) {
            if (leader_follower.getFollower().equals(user)) {
                thisUserFollow.add(leader_follower.getLeader());
            }
        }
        for (String x : thisUserFollow) {//判断是否已经关注
            if (wantToFollow.equals(x)) {
                isFollowing = true;
            }
        }
        if (isFollowing) {
            to_client_sout(socket, "Do you want to unfollow him/her?please enter 1 for unfollow,enter 0 for keep following");
            int temp = from_client_int(socket);
            leader_follower y = new leader_follower();
            if (temp == 1) {//在leader_follower中删除这一条
                for (int i = 0; i < leader_followers.size(); i++) {
                    if (leader_followers.get(i).getFollower().equals(user) && leader_followers.get(i).getLeader().equals(wantToFollow)) {
                        leader_followers.remove(i);

                    }
                }
                for (int i = 0; i < thisUserFollow.size(); i++) {
                    if (thisUserFollow.get(i).equals(wantToFollow)) {
                        thisUserFollow.remove(i);
                        y.setFollower(user);
                        y.setLeader(wantToFollow);
                    }
                }

                Properties prop = loader.loadDBUser();
                loader.openDB(prop);
                loader.set_delete_PrepareStatement_leader_follower(); // 设置预处理语句

                try {
                    loader.deleteData_leader_follower(y); // 执行删除操作
                    loader.con.commit(); // 提交事务
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        if (!isFollowing) {
            to_client_sout(socket, "Do you want to follow him/her?Please enter 1 for follow or 0 for no");
            int temp1 = from_client_int(socket);
            if (temp1 == 1) {
                leader_follower a = new leader_follower();
                a.setFollower(user);
                a.setLeader(wantToFollow);
                leader_followers.add(a);
                thisUserFollow.add(wantToFollow);

                Properties prop = loader.loadDBUser();
                loader.openDB(prop);
                loader.setPrepareStatement_leader_follower(); // 设置预处理语句

                try {
                    loader.loadData_leader_follower(a); // 执行删除操作
                    loader.con.commit(); // 提交事务
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        to_client_sout_no_input(socket, "the authors you follow: ");
        if (thisUserFollow.size() == 0) {
            to_client_sout_no_input(socket, "null                                              \n");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String a : thisUserFollow) {
            stringBuilder.append(a + " " + "\n");
        }
        to_client_sout_no_input(socket, stringBuilder.toString());
    }

    public static void send_post(Socket socket, String author_name) {
        to_client_sout(socket, "If you want anonymous speech, enter 1, real name, enter 0");
        String author = null;
        boolean is_anonymous = false;
        int input = from_client_int(socket);
        if (input == 1) {
            is_anonymous = true;
            author = "unknown";
        } else author = author_name;
        int postid = posts.size();

        to_client_sout(socket, "Please enter the title:");
        String title = from_client_next(socket);
        to_client_sout(socket, "Please enter the content:");
        String content = from_client_next(socket);
        Date currentDate = new Date();
        Timestamp time = new Timestamp(currentDate.getTime());
        to_client_sout(socket, "Please enter the city:");
        String city = from_client_next(socket);

        post x1 = new post();
        x1.setContent(content);
        x1.setID(postid);
        x1.setPosting_city(city);
        x1.setPosting_time(time);
        x1.setTitle(title);
        x1.setAuthor(author);
        posts.add(x1);

        author_send_post x = new author_send_post();
        x.setAuthor(author_name);
        x.setPostid(postid);
        if (is_anonymous) {
            x.setIs_anonymous(1);
        } else x.setIs_anonymous(0);
        author_send_posts.add(x);


        Properties prop = loader.loadDBUser();

        loader.openDB(prop);
        loader.setPrepareStatement_post();
        try {
            loader.loadData_post(x1);//do insert command
            loader.stmt.executeBatch();
            loader.con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        loader.openDB(prop);
        loader.setPrepareStatement_author_send_post();
        try {
            loader.loadData_author_send_post(x);//do insert command
            loader.stmt.executeBatch();
            loader.con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public static void reply_post_or_reply_reply(Socket socket, String author_name) {
        to_client_sout(socket, "If you want anonymous speech, enter 1, real name, enter 0");
        String author = author_name;
        boolean is_anonymous = false;
        int input = from_client_int(socket);
        if (input == 1) {
            author = "unknown";
            is_anonymous = true;
        } else author = author_name;

        to_client_sout(socket, "Do you want to reply post or reply reply? Please enter 0 for post or 1 for reply");
        int temp = from_client_int(socket);
        if (temp == 0) {
            to_client_sout(socket, "Please enter the postid of the post you want to reply");
            int postid = from_client_int(socket);
            to_client_sout(socket, "Please enter the reply content:");
            String content = from_client_next(socket);
            int stars = 0;
            int id = replies.size();
            reply x = new reply();
            x.setAuthor(author);
            x.setPostID(postid);
            x.setContent(content);
            x.setStars(stars);
            x.setId(id);
            replies.add(x);

            author_send_reply author_send_reply = new author_send_reply();
            author_send_reply.setAuthor(author_name);
            author_send_reply.setReplyid(id);
            if (is_anonymous) {
                author_send_reply.setIs_anonymous(1);
            } else author_send_reply.setIs_anonymous(0);
            author_send_replies.add(author_send_reply);

            Properties prop = loader.loadDBUser();
            loader.openDB(prop);
            loader.setPrepareStatement_reply();
            try {
                loader.loadData_reply(x);//do insert command
                loader.stmt.executeBatch();
                loader.con.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            loader.openDB(prop);
            loader.setPrepareStatement_author_send_reply();
            try {
                loader.loadData_author_send_reply(author_send_reply);//do insert command
                loader.stmt.executeBatch();
                loader.con.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        } else {
            to_client_sout(socket, "Please enter the replyid of the reply you want to reply");
            int subreplyid = from_client_int(socket);
            to_client_sout(socket, "Please enter the reply content:");
            String content = from_client_next(socket);
            int stars = 0;
            int id = subreplies.size();
            subreply x = new subreply();
            x.setAuthor(author);
            x.setReplyid(subreplyid);
            x.setContent(content);
            x.setStars(stars);
            x.setId(id);
            subreplies.add(x);

            author_send_subreply author_send_subreply = new author_send_subreply();
            author_send_subreply.setAuthor(author_name);
            author_send_subreply.setSubreplyid(id);
            if (is_anonymous) {
                author_send_subreply.setIs_anonymous(1);
            } else author_send_subreply.setIs_anonymous(0);
            author_send_subreplies.add(author_send_subreply);

            Properties prop = loader.loadDBUser();
            loader.openDB(prop);
            loader.setPrepareStatement_subreply();
            try {
                loader.loadData_subreply(x);//do insert command
                loader.stmt.executeBatch();
                loader.con.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            loader.openDB(prop);
            loader.setPrepareStatement_author_send_subreply();
            try {
                loader.loadData_author_send_subreply(author_send_subreply);//do insert command
                loader.stmt.executeBatch();
                loader.con.commit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void look_for_posts_or_replies_send(Socket socket, String author_name) {
        ArrayList<post> postsend = new ArrayList<>();
        for (author_send_post author_send_post : author_send_posts) {
            if (author_send_post.getAuthor().equals(author_name)) {
                for (int i = 0; i < posts.size(); i++) {
                    if (posts.get(i).getID() == author_send_post.getPostid()) {
                        postsend.add(posts.get(i));
                    }
                }
            }
        }
        ArrayList<reply> replysend = new ArrayList<>();
        for (author_send_reply author_send_reply : author_send_replies
        ) {
            if (author_send_reply.getAuthor().equals(author_name)) {
                for (int i = 0; i < replies.size(); i++) {
                    if (replies.get(i).getId() == author_send_reply.getReplyid()) {
                        replysend.add(replies.get(i));
                    }
                }
            }
        }

        ArrayList<subreply> subreplysend = new ArrayList<>();
        for (author_send_subreply author_send_subreply : author_send_subreplies
        ) {
            if (author_send_subreply.getAuthor().equals(author_name)) {
                for (int i = 0; i < subreplies.size(); i++) {
                    if (subreplies.get(i).getId() == author_send_subreply.getSubreplyid()) {
                        subreplysend.add(subreplies.get(i));
                    }
                }
            }
        }

        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("postsend:");
        if (postsend.size() == 0) {
            stringBuilder1.append("null                                              ");
        }
        for (post post : postsend) {
            stringBuilder1.append("postid: " + post.getID() + "\n" + "title: " + post.getTitle() + "\n" + "content: " + post.getContent() + "\n");
            for (int i = 0; i < author_send_posts.size(); i++) {
                if (author_send_posts.get(i).getPostid() == post.getID()) {
                    if (author_send_posts.get(i).getIs_anonymous() == 1) {
                        stringBuilder1.append("post in anonymous" + "\n");
                    } else stringBuilder1.append("post in real name" + "\n");
                }
            }
        }
        to_client_sout_no_input(socket, stringBuilder1.toString());

        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("replysend:");
        if (replysend.size() == 0) {
            stringBuilder2.append("null\n");
        }
        for (reply reply : replysend) {
            stringBuilder2.append("replyid: " + reply.getId() + "\n" + "content " + reply.getContent() + "\n");
            for (int i = 0; i < author_send_replies.size(); i++) {
                if (author_send_replies.get(i).getReplyid() == reply.getId()) {
                    if (author_send_replies.get(i).getIs_anonymous() == 1) {
                        stringBuilder2.append("post in anonymous \n");
                    } else stringBuilder2.append("post in real name\n");
                }
            }
        }
        to_client_sout_no_input(socket, stringBuilder2.toString());

        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("subreplysend:");
        if (subreplysend.size() == 0) {
            to_client_sout_no_input(socket, "null                                                       ");
        }
        for (subreply subreply : subreplysend) {
            stringBuilder3.append("id: " + subreply.getId() + "\n" + "content: " + subreply.getContent() + "\n");
            for (int i = 0; i < author_send_subreplies.size(); i++) {
                if (author_send_subreplies.get(i).getSubreplyid() == subreply.getId()) {
                    if (author_send_subreplies.get(i).getIs_anonymous() == 1) {
                        stringBuilder3.append("post in anonymous\n");
                    } else
                        stringBuilder3.append("post in real name\n");
                }
            }
        }
        to_client_sout_no_input(socket, stringBuilder3.toString());
    }

    public static void block_or_unblock(Socket socket, String author_name) {
        to_client_sout(socket, "please enter the author name you want to block or unblock: ");
        String dowith = from_client_next(socket);
        boolean exist = false;
        for (int i = 0; i < blocker_blockeds.size(); i++) {
            if (blocker_blockeds.get(i).getBlocker_name().equals(author_name) && blocker_blockeds.get(i).getBlocked_name().equals(dowith)) {
                exist = true;
                to_client_sout(socket, "you have blocked him before, do you want to unblock him? enter 1 to unblock him, enter 0 to cancel");
                int want_unblock = from_client_int(socket);
                if (want_unblock == 1) {
                    Properties prop = loader.loadDBUser();
                    loader.openDB(prop);
                    loader.set_delete_PrepareStatement_blocker_blocked(); // 设置预处理语句

                    try {
                        loader.deleteData_blocker_blocked(blocker_blockeds.get(i)); // 执行删除操作
                        loader.con.commit(); // 提交事务
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    blocker_blockeds.remove(i);

                }
                break;
            }
        }
        if (!exist) {
            to_client_sout(socket, "do you sure you want to block him? once do it, you can't see the posts he send, but you can unblock him at anytime. enter 1 to block him, enter 0 to cancel");
            int want_block = from_client_int(socket);
            if (want_block == 1) {
                blocker_blocked blocker_blocked = new blocker_blocked();
                blocker_blocked.setBlocker_name(author_name);
                blocker_blocked.setBlocked_name(dowith);
                blocker_blockeds.add(blocker_blocked);

                Properties prop = loader.loadDBUser();
                loader.openDB(prop);
                loader.setPrepareStatement_blocker_blocked(); // 设置预处理语句

                try {
                    loader.loadData_blocker_blocked(blocker_blocked); // 执行删除操作
                    loader.con.commit(); // 提交事务
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        to_client_sout_no_input(socket, "the authors you blocked as following: ");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < blocker_blockeds.size(); i++) {
            if (blocker_blockeds.get(i).getBlocker_name().equals(author_name)) {
                stringBuilder.append(blocker_blockeds.get(i).getBlocked_name() + "                                   \n");
            }
        }
        to_client_sout_no_input(socket, stringBuilder.toString());
    }

    public static void find_most_like_post(Socket socket) {
        int[][] like_count = new int[posts.size()][2];
//第一列是id，第二列是喜欢的人数
        for (int i = 0; i < like_count.length; i++) {
            like_count[i][0] = i;
        }
        for (int i = 0; i < author_like_posts.size(); i++) {
            like_count[author_like_posts.get(i).getPostid()][1]++;
        }
        Arrays.sort(like_count, Comparator.comparingInt((int[] a) -> a[1]).reversed());
        //按照like人数从多到少，第0列是id
        List<post> most_like_post = new ArrayList<>();
        for (int i = 0; i < like_count.length; i++) {
            post post = posts.get(like_count[i][0]);
            most_like_post.add(post);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < most_like_post.size(); i++) {
            stringBuilder.append(most_like_post.get(i).getID() + "\n");
        }
        to_client_sout_no_input(socket, stringBuilder.toString());
        int[] count = new int[most_like_post.size()];
        for (int i = 0; i < most_like_post.size(); i++) {
            int postid = most_like_post.get(i).getID();
            for (author_like_post author_like_post : author_like_posts) {
                int postid1 = author_like_post.getPostid();
                if (postid == postid1) {
                    count[i]++;
                    break;
                }
            }
        }
        show_posts_list_add_count(socket, most_like_post,count);
    }

    public static void find_post_by_time_and_author(Socket socket) {
        List<post> results = new ArrayList<>();
        to_client_sout(socket, "You can set a start time, let's enter the year of the start time first: ");
        int year_start = from_client_int(socket);
        to_client_sout(socket, "let's enter the month of the start time: ");
        int month_start = from_client_int(socket);
        to_client_sout(socket, "let's enter the day of the start time: ");
        int day_start = from_client_int(socket);

        Calendar calendar_start = Calendar.getInstance();
        calendar_start.set(year_start, month_start - 1, day_start);
        // 获取对应的Date对象
        Date date_start = calendar_start.getTime();
        // 获取时间戳
        long timestamp_start = date_start.getTime();

        to_client_sout(socket, "You can set a end time, let's enter the year of the end time first: ");
        int year_end = from_client_int(socket);
        to_client_sout(socket, "let's enter the month of the end time: ");
        int month_end = from_client_int(socket);
        to_client_sout(socket, "let's enter the day of the end time: ");
        int day_end = from_client_int(socket);

        Calendar calendar_end = Calendar.getInstance();
        calendar_end.set(year_end, month_end - 1, day_end);
        // 获取对应的Date对象
        Date date_end = calendar_end.getTime();
        // 获取时间戳
        long timestamp_end = date_end.getTime();

        Date start_date = new Date(timestamp_start);
        Date end_date = new Date(timestamp_end);

        to_client_sout(socket, "enter the author name you want to look for: ");
        String author = from_client_next(socket);
        for (post post : posts) {
            Date date = new Date(post.getPosting_time().getTime());
            if (start_date.before(date) && end_date.after(date) && author.equals(post.getAuthor())) {
                results.add(post);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("the postids send by " + author + "in that time are following: ");
        if (results.size() == 0) {
            stringBuilder.append("null ");
        }
        for (int i = 0; i < results.size(); i++) {
            stringBuilder.append(results.get(i).getID() + "\n");
        }
        to_client_sout_no_input(socket, stringBuilder.toString());
        show_posts_list(socket, results);
    }


    public static void show_posts_list(Socket socket, List<post> posts) {

        String s_html_content = "";
        for (post p1 : posts) {
            String s1 = "<div style=\"border:1px solid #4ADDFA\" class=\"blog\">\n" +
                    "    <div class=\"title\">" + p1.getTitle() + "</div>\n" +
                    "       <div class=\"date\">Post time: " + p1.getPosting_time().toString() +"  &nbsp;Post ID:" + p1.getID()+ "  &nbsp;&nbsp;   Author:" + p1.getAuthor() + "</div>\n" +
                    "    <div class=\"desc\">\n" +
                    "        " + p1.getContent() + "\n" +
                    "    </div>\n" +
                    "</div>\n" +
                    "<div style=\"height: 10pt\">\n" +
                    "</div>";
            s_html_content += s1;

        }

        try {
            ServerSocket serverSocket = new ServerSocket(12535);
//            System.out.println("show posts in http://127.0.0.1:12535");
            to_client_sout_no_input(socket, "show posts in http://127.0.0.1:12535");
            while (true) {
                Socket client = serverSocket.accept();
                OutputStream clientOutStream = client.getOutputStream();
                clientOutStream.write(
                        ("HTTP/1.1 200\n"
                                + "Content-Type: text/html\n"
                                + "\n"
                                + s_html_head + s_html_content + s_html_tail).getBytes()

                        //
                );

                clientOutStream.flush();
                Thread.sleep(10000);
                clientOutStream.close();
//                System.out.println("show posts in http://127.0.0.1:12535");

                break;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void show_posts_list_add_count(Socket socket, List<post> posts, int count[]) {

        String s_html_content = "";
        int i = 0;
        for (post p1 : posts) {
            String s1 = "<div style=\"border:1px solid #4ADDFA\" class=\"blog\">\n" +
                    "    <div class=\"title\">" + p1.getTitle() + "</div>\n" +
                    "       <div class=\"date\">Post time: " + p1.getPosting_time().toString() + "  &nbsp;Post ID:" + p1.getID() + "  &nbsp;Like:" + count[i] + ";&nbsp;   Author:" + p1.getAuthor() + "</div>\n" +
                    "    <div class=\"desc\">\n" +
                    "        " + p1.getContent() + "\n" +
                    "    </div>\n" +
                    "</div>\n" +
                    "<div style=\"height: 10pt\">\n" +
                    "</div>";
            s_html_content += s1;
            i++;
        }

        try {
            ServerSocket serverSocket = new ServerSocket(12535);
//            System.out.println("show posts in http://127.0.0.1:12535");
            to_client_sout_no_input(socket, "show posts in http://127.0.0.1:12535");
            while (true) {
                Socket client = serverSocket.accept();
                OutputStream clientOutStream = client.getOutputStream();
                clientOutStream.write(
                        ("HTTP/1.1 200\n"
                                + "Content-Type: text/html\n"
                                + "\n"
                                + s_html_head + s_html_content + s_html_tail).getBytes()

                        //
                );

                clientOutStream.flush();
                Thread.sleep(10000);
                clientOutStream.close();
//                System.out.println("show posts in http://127.0.0.1:12535");

                break;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }


    public static String s_html_tail = ""
            + "            </div>\n"
            + "        </div>\n"
            + "    </div>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>";


    public static String s_html_head = ""
            + "            <!DOCTYPE html>\n"
            + "            <html>\n"
            + "            <head>\n"
            + "                <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n"
            + "                <title>Blogs</title>\n"
            + "                <link href=\"http://cdn.bootcss.com/bootstrap/3.2.0/css/bootstrap.min.css\" rel=\"stylesheet\">\n"
            + "                <script src=\"http://cdn.bootcss.com/jquery/2.1.1/jquery.min.js\"></script>\n"
            + "                <script src=\"http://cdn.bootcss.com/bootstrap/3.2.0/js/bootstrap.min.js\"></script>\n"
            + "                <link href=\"https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.css\" rel=\"stylesheet\">\n"
            + "                <script src=\"https://cdn.bootcss.com/bootstrap-table/1.11.1/bootstrap-table.min.js\"></script>\n"
            + "                <script src=\"https://cdn.bootcss.com/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js\"></script>\n"
            + "                \n"
            + "                <style>\n"
            + "                    /* 表示一篇博客 */\n"
            + "                    .blog {\n"
            + "                        width: 100%;\n"
            + "                        padding: 10px 20px;\n"
            + "                    }\n"
            + "                    \n"
            + "                    /* 博客的标题 */\n"
            + "                    .blog .title {\n"
            + "                        color: black;\n"
            + "                        font-size: 20px;\n"
            + "                        font-weight: 700;\n"
            + "                        text-align: center;\n"
            + "                        padding: 10px 0;\n"
            + "                    }\n"
            + "                    \n"
            + "                    /* 博客的摘要 */\n"
            + "                    .blog .desc {\n"
            + "                        color: #000;\n"
            + "                        text-indent: 2em;\n"
            + "                        margin-top: 10px;\n"
            + "                    }\n"
            + "                    \n"
            + "                    .blog .date {\n"
            + "                        color: #008000;\n"
            + "                        margin-top: 10px;\n"
            + "                        text-align: center;\n"
            + "                    }\n"
            + "                    \n"
            + "                </style>\n"
            + "            </head>\n"
            + "            \n"
            + "            <body>\n"
            + "            <div style=\"width:100%;background-color:#363636\">\n"
            + "                <ul class=\"nav nav-pills\">\n"
            + "                    <li><a href=\"\"><p style=\"font-size:18px;\" >博客大厅 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp</p></a></li>\n"
            + "                </ul>\n"
            + "            </div>\n"
            + "            <div class=\"row\">\n"
            + "                <div class=\"col-md-2\">\n"
            + "                </div>\n"
            + "                \n"
            + "                <div class=\"col-md-10\">\n"
            + "                \n"
            + "                    <div style=\"width: 80%\">\n"
            + "                        <div class=\"panel panel-info\">\n"
            + "                            <div class=\"panel-heading\">\n"
            + "                                <h3 class=\"panel-title\">所有博客</h3>\n"
            + "                            </div>\n"
            + "                            \n"
            + "                           \n"
            + "                        \n"
            + " ";


}

