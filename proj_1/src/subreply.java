public class subreply {
    private String content;
    private String authorID;
    private int stars;
    private int postID;
    private String reply_content;
    private int reply_authorID;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }

    public int getReply_authorID() {
        return reply_authorID;
    }

    public void setReply_authorID(int reply_authorID) {
        this.reply_authorID = reply_authorID;
    }
}
