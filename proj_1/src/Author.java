import java.sql.Timestamp;

public class Author {
    private String ID;
    private Timestamp registration_time;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Timestamp getRegistration_time() {
        return registration_time;
    }

    public void setRegistration_time(Timestamp registration_time) {
        this.registration_time = registration_time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;
}
