
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

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

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        loader.main(args);
        while (true) {
            String author_name = login_or_setup();
            System.out.println("what do you want to do? you can choose a number from 1~6, each number represents an operation,\n 1. You want to favorite, like, or share post,\n 2. You want to view the favorite, like or share list of a post,\n 3. you want to follow or unfollow other users, and you can also view the user list you have been followed.\n 4. you want to create a post\n 5. you want to reply a post or reply a reply\n 6. you want to own your own posts or replies\n");
            int what_to_do = input.nextInt();
            if (what_to_do == 1) {
                author_favorite_share_like_post(author_name);
            } else if (what_to_do == 2) {
                view_favorite_share_like_list();
            }


            System.out.println("if you want to exit, please type 0, If you want to continue, please type 1");
            int res = input.nextInt();
            if (res == 0) {
                break;
            }
        }
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
                login_or_setup();
            }
        } else if (type == 1) {
            author_name = set_new_author();
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
}
