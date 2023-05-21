public class author_share_post {
    private String author;
    private int postid;

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

    public static void action_author_share_post(String author,int postid){
        author_share_post author_share_post = new author_share_post();
        author_share_post.author=author;
        author_share_post.postid=postid;
        loader1820.author_share_posts.add(author_share_post);
    }
}
