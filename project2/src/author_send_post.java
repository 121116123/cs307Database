public class author_send_post {
    private String author;
    private int postid;
    private int is_anonymous;

    public void setIs_anonymous(int is_anonymous) {
        this.is_anonymous = is_anonymous;
    }

    public int getIs_anonymous() {
        return is_anonymous;
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
}
