public class author_send_post extends author_share_post {
    private String author;
    private int postid;
    private String content;

    public int getPostid() {
        return postid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public static void sendpost(String author,String content){
        author_send_post author_send_post=new author_send_post();
        author_send_post.author=author;
        author_send_post.content=content;
        loader1820.author_share_posts.add(author_send_post);
    }
}
