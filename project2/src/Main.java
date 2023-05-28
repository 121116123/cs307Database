
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    public static void main(String[] args) {
        Socket socket = server_init();
        StringBuffer s = from_client(socket);
        System.out.println("[Form Client] " + s);
        Scanner input = new Scanner(System.in);
//        loader.main(args);
        load(args);
        String author_name = login_or_setup();
        System.out.println("au=" + author_name);
        while (true) {
//            System.out.println("what do you want to do? you can choose a number from 1~6, each number represents an operation,\n 1. You want to favorite, like, or share post,\n 2. You want to view the favorite, like or share list of a post,\n 3. you want to follow or unfollow other users, and you can also view the user list you have been followed.\n 4. you want to create a post\n 5. you want to reply a post or reply a reply\n 6. you want to own your own posts or replies\n");
            String to_c = "what do you want to do? you can choose a number from 1~6, each number represents an operation,\n 1. You want to favorite, like, or share post,\n 2. You want to view the favorite, like or share list of a post,\n 3. you want to follow or unfollow other users, and you can also view the user list you have been followed.\n 4. you want to create a post\n 5. you want to reply a post or reply a reply\n 6. you want to own your own posts or replies\n";
            to_client(socket, to_c);
//            int what_to_do = input.nextInt();
            StringBuffer stringBuffer = from_client(socket);
            String string=stringBuffer.toString();
            int what_to_do=string.charAt(0) - '0';
            System.out.println("what_to_do: "+what_to_do);
            if (what_to_do == 1) {
                author_favorite_share_like_post(author_name);
            } else if (what_to_do == 2) {
                view_favorite_share_like_list();
            } else if (what_to_do == 3) {
                follow_others_or_cancel_follow_and_lookfor_follow_list(author_name);
            } else if (what_to_do == 4) {
                send_post(author_name);
            } else if (what_to_do == 5) {
                reply_post_or_reply_reply(author_name);
            } else if (what_to_do == 6) {
                look_for_posts_or_replies_send(author_name);
            }

            System.out.println("if you want to exit, please type 0, If you want to continue, please type 1");
            int res = input.nextInt();
            if (res == 0) {
                break;
            }
        }
    }

    public static StringBuffer from_client(Socket socket) {
        while (true) {
            InputStream in;
            try {
                in = socket.getInputStream();
                byte[] b = new byte[512];
                StringBuffer sb = new StringBuffer();
                String s;
                int x = in.read(b);
                if (x != -1) {
                    byte[] c = copyOfRange(b, 0, x);
                    s = new String(c);
                    sb.append(s);
                }
                //OutputStream out=socket.getOutputStream();
                //System.out.println("[Form Client] "+sb);
                return sb;

//                    Scanner sc=new Scanner(System.in);
//                    String str=sc.nextLine();
//                    out.write(str.getBytes());
//                    out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void to_client(Socket socket, String s) {
        while (true) {
            InputStream in;
            try {
                OutputStream out = socket.getOutputStream();
                out.write(s.getBytes());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
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

    public static void load(String[] args) {
        loader.main(args);
    }

    public static String login_or_setup() {
        Scanner input = new Scanner(System.in);
        System.out.println("if you already have an account, please type 0, If you want to register a new user, please type 1");
        int type = input.nextInt();
        String author_name = null;
        if (type == 0) {
            System.out.println("please input your author name: ");
            String s = input.next();
            for (author author : authors
            ) {
                if (author.getName().equals(s)) {
                    author_name = s;
                    break;
                }
            }
            if (author_name == null) {
                System.out.println("not a valid name");
                author_name = login_or_setup();
            }
        } else {
            author_name = set_new_author();
            System.out.println(author_name);
        }
        return author_name;
    }

    public static String set_new_author() {
        Scanner input = new Scanner(System.in);
        System.out.println("please input your id: ");
        String id = input.next();
        for (int i = 0; i < authors.size(); i++) {
            if (id.equals(authors.get(i).getID())) {
                System.out.printf("Your ID has already registered an account, and the nickname is %s. Please check if it is yours \n, If you want to use this account, please type 0. If you want to re-enter your id, please type 1 \n", authors.get(i).getName());
                int res = input.nextInt();
                if (res == 0) {
                    return authors.get(i).getName();
                } else {
                    System.out.println("please re-enter your id: ");
                    id = input.next();
                    i = 0;
                }
            }
        }
        long now_time = System.currentTimeMillis();
        Timestamp reg_time = new Timestamp(now_time);
        System.out.println("please input your phone number: ");
        String phone = input.next();
        System.out.println("please input your name: ");
        String name = input.next();
        for (int i = 0; i < authors.size(); i++) {
            if (authors.get(i).getName().equals(name)) {
                System.out.println("This name has already been registered by someone else. Please change your name: ");
                name = input.next();
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

    public static void author_favorite_share_like_post(String author_name) {
//        System.out.println(author_name);
        Scanner input = new Scanner(System.in);
        System.out.println("if you know which post you want to do with, please type 1, if you want to choose 1 according to category, please type 0");
        int postid = 0;
        //get postid
        if (input.nextInt() == 1) {
            System.out.println("Please enter the ID of the post you want to operate");
            postid = input.nextInt();
        } else {
            System.out.println("The following is the categories we have. What kind of post do you want to read? Please enter the number before the category");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println(i + " " + categories.get(i).getContent());
            }
            int id = input.nextInt();
            String category_content = categories.get(id).getContent();
            System.out.println("there are some posts about that, you can choose a postid to view the details");
            for (post_category post_category : post_categories
            ) {
                if (post_category.getCategory().equals(category_content)) {
                    System.out.println(post_category.getPostid());
                }
            }
            System.out.println("which post will you choose? Please select the post you want to see according to the above ID");
            postid = input.nextInt();
        }

        //do with post id
        for (post post : posts) {
            if (post.getID() == postid) {
                System.out.printf("the title of the post is %s\n, the content is %s\n", post.getTitle(), post.getContent());
                System.out.println("type 1 to favorite it, type 2 to share it, type 3 to like it");
                int what_to_do = input.nextInt();
                if (what_to_do == 1) {
                    author_favorite_post author_favorite_post = new author_favorite_post();
                    author_favorite_post.setAuthor(author_name);
                    author_favorite_post.setPostid(post.getID());
                    System.out.println("aufa.au= " + author_favorite_post.getAuthor());
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
        System.out.println("there is no that postid, please check the id, if you want to try again,please type 1, if you want to exit, please type 0");
        if (input.nextInt() == 1) {
            author_favorite_share_like_post(author_name);
        }
    }

    public static void view_favorite_share_like_list() {
        Scanner input = new Scanner(System.in);
        System.out.println("please enter the id of the post you want to do with: ");
        int postid = input.nextInt();
        System.out.println("which list do you want to view? enter 1 to see favorites, type 2 to see shares, type 3 to see likes");
        int type = input.nextInt();
        if (type == 1) {
            System.out.println("who favorites the post: ");
            for (author_favorite_post author_favorite_post : author_favorite_posts) {
                if (author_favorite_post.getPostid() == postid) {
                    System.out.println(author_favorite_post.getAuthor());
                }
            }
        } else if (type == 2) {
            System.out.println("who shares the post: ");
            for (author_share_post author_share_post : author_share_posts) {
                if (author_share_post.getPostid() == postid) {
                    System.out.println(author_share_post.getAuthor());
                }
            }
        } else if (type == 3) {
            System.out.println("who likes the post: ");
            for (author_like_post author_like_post : author_like_posts) {
                if (author_like_post.getPostid() == postid) {
                    System.out.println(author_like_post.getAuthor());
                }
            }
        }

    }

    public static void follow_others_or_cancel_follow_and_lookfor_follow_list(String author_name) {
        Scanner input = new Scanner(System.in);
        System.out.println("please enter the author you want to follow or cancel follow");
        String wantToFollow = input.nextLine();
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
            System.out.println("Do you want to unfollow him/her?please enter 0 for unfollow,enter 1 for keep following");
            int temp = input.nextInt();
            leader_follower y = new leader_follower();
            if (temp == 0) {//在leader_follower中删除这一条
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
            System.out.println("Do you want to follow him/her?Please enter 0 for follow or 1 for no");
            int temp1 = input.nextInt();
            if (temp1 == 0) {
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
        System.out.print("the authors you follow: ");
        if (thisUserFollow.size() == 0) {
            System.out.println("null");
        }
        for (String a : thisUserFollow) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public static void send_post(String author_name) {
        Scanner input = new Scanner(System.in);
        System.out.println("If you want anonymous speech, enter 1, real name, enter 0");
        String author = author_name;
        boolean is_anonymous = false;
        if (input.nextInt() == 1) {
            is_anonymous = true;
            author = "unknown";
        } else author = author_name;
        int postid = posts.size();

        System.out.println("Please enter the title:");
        input.nextLine();
        String title = input.nextLine();
        System.out.println("Please enter the content:");
        String content = input.nextLine();
        Date currentDate = new Date();
        Timestamp time = new Timestamp(currentDate.getTime());
        System.out.println("Please enter the city:");
        String city = input.nextLine();

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

    public static void reply_post_or_reply_reply(String author_name) {
        Scanner input = new Scanner(System.in);
        System.out.println("If you want anonymous speech, enter 1, real name, enter 0");
        String author = author_name;
        boolean is_anonymous = false;
        if (input.nextInt() == 1) {
            author = "unknown";
            is_anonymous = true;
        } else author = author_name;

        System.out.println("Do you want to reply post or reply reply? Please enter 0 for post or 1 for reply");
        int temp = input.nextInt();
        if (temp == 0) {
            System.out.println("Please enter the postid of the post you want to reply");
            int postid = input.nextInt();
            System.out.println("Please enter the reply content:");
            input.nextLine();
            String content = input.nextLine();
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
            System.out.println("Please enter the replyid of the reply you want to reply");
            int subreplyid = input.nextInt();
            System.out.println("Please enter the reply content:");
            input.nextLine();
            String content = input.nextLine();
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

    public static void look_for_posts_or_replies_send(String author_name) {
//        String author = author_name;
        Scanner input = new Scanner(System.in);
        System.out.println("");
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

        System.out.print("postsend:");
        if (postsend.size() == 0) {
            System.out.println("null");
        }
        for (post post : postsend) {
            System.out.println("postid: " + post.getID());
            System.out.println("title: " + post.getTitle());
            System.out.println("content: " + post.getContent());
            for (int i = 0; i < author_send_posts.size(); i++) {
                if (author_send_posts.get(i).getPostid() == post.getID()) {
                    if (author_send_posts.get(i).getIs_anonymous() == 1) {
                        System.out.println("post in anonymous");
                    } else System.out.println("post in real name");
                }
            }
        }
        System.out.println();


        System.out.print("replysend:");
        if (replysend.size() == 0) {
            System.out.println("null");
        }
        for (reply reply : replysend) {
            System.out.println("replyid: " + reply.getId());
            System.out.println("content " + reply.getContent());
            for (int i = 0; i < author_send_replies.size(); i++) {
                if (author_send_replies.get(i).getReplyid() == reply.getId()) {
                    if (author_send_replies.get(i).getIs_anonymous() == 1) {
                        System.out.println("post in anonymous");
                    } else System.out.println("post in real name");
                }
            }
        }
        System.out.println();

        System.out.print("subreplysend:");
        if (subreplysend.size() == 0) {
            System.out.println("null");
        }
        for (subreply subreply : subreplysend) {
            System.out.println("id: " + subreply.getId());
            System.out.println("content: " + subreply.getContent());
            for (int i = 0; i < author_send_subreplies.size(); i++) {
                if (author_send_subreplies.get(i).getSubreplyid() == subreply.getId()) {
                    if (author_send_subreplies.get(i).getIs_anonymous() == 1) {
                        System.out.println("post in anonymous");
                    } else System.out.println("post in real name");
                }
            }
        }
    }

}
