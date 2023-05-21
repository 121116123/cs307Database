public class author_favorite_post {
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

    public static void action_author_favorite_post(String author,int postid){
        author_favorite_post author_favorite_post = new author_favorite_post();
        author_favorite_post.author=author;
        author_favorite_post.postid=postid;
        loader1820.author_favorite_posts.add(author_favorite_post);
    }
}
