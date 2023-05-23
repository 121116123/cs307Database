public class author_send_post extends author_share_post {
    private String author;
    private int postid;
    private String content;
    private String title;

    public author_send_post(){
    }
    public author_send_post(int postid,String title){
        this.postid=postid;
        this.title=title;
    }

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
        loader.author_share_posts.add(author_send_post);
    }
}
