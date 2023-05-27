public class author_send_reply {
    private String author;
    private int replyid;
    private int is_anonymous;

    public void setIs_anonymous(int is_anonymous) {
        this.is_anonymous = is_anonymous;
    }

    public int getIs_anonymous() {
        return is_anonymous;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setReplyid(int replyid) {
        this.replyid = replyid;
    }

    public String getAuthor() {
        return author;
    }

    public int getReplyid() {
        return replyid;
    }
}
