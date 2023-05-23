import java.sql.Timestamp;

public class post {
    private int ID;
    private String title;
    private String content;
    private Timestamp posting_time;
    private String posting_city;

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPosting_time(Timestamp posting_time) {
        this.posting_time = posting_time;
    }

    public void setPosting_city(String posting_city) {
        this.posting_city = posting_city;
    }


    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getPosting_time() {
        return posting_time;
    }

    public String getPosting_city() {
        return posting_city;
    }


}
