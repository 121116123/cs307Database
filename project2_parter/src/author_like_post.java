public class author_like_post {
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

    public static void action_author_like_post(String author,int postid){
        author_like_post author_like_post = new author_like_post();
        author_like_post.author=author;
        author_like_post.postid=postid;
        loader1820.author_like_posts.add(author_like_post);
    }
}
