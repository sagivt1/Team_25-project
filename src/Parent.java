/*
Parent
*/

public class Parent {
    private String ParentID;
    private String lname;
    private String fname;
    private String password;
    private String Type = "Parent";
    private String birth_date;


    public Parent() {
    }

    public Parent(String ParentID, String lname, String fname, String password, String birth_date){
        this.ParentID = ParentID;
        this.lname = lname;
        this.fname = fname;
        this.password = password;
        this.birth_date = birth_date;


    }

    public String getCounselorID() {
        return ParentID;
    }

    public void setCounselorID(String StudentsID) {
        this.ParentID = ParentID;
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

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }
    public String getbirth_date() {
        return birth_date;
    }

    public void setbirth_date(String Type) {
        this.Type = birth_date;
    }



}
