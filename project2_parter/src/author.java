import java.sql.Timestamp;

public class author {
    private String ID;
    private Timestamp registration_time;
    private String phone;
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


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

    public static void registerNewAuthor(String name,String ID){//注册方法
        author author=new author();
        author.name=name;
        author.ID=ID;
        loader1820.authors.add(author);
    }
}
