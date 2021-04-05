public class User {

    public User() {
    }
    private String UserID;
    private String lname;
    private String fname;
    private String password;
    private String birth_date;

    public User(String UserID, String lname, String fname, String password, String birth_date){
        this.UserID = UserID;
        this.lname = lname;
        this.fname = fname;
        this.password = password;
        this.birth_date = birth_date;

    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getbirth_date() {
        return birth_date;
    }

    public void setbirth_date(String birth_date) {
        this.birth_date = birth_date;
    }
}
