public class author_send_subreply {
    private String author;
    private int subreplyid;
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

    public void setSubreplyid(int subreplyid) {
        this.subreplyid = subreplyid;
    }

    public String getAuthor() {
        return author;
    }

    public int getSubreplyid() {
        return subreplyid;
    }
}
