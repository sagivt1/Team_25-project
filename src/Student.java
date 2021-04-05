/*
        Student
        */

public class Student {
    private String StudentsID;
    private String lname;
    private String fname;
    private String password;
    private String Type = "Student";
    private String birth_date;

    public Student() {
    }

    public Student(String StudentsID, String lname, String fname, String password, String birth_date){
        this.StudentsID = StudentsID;
        this.lname = lname;
        this.fname = fname;
        this.password = password;
        this.birth_date = birth_date;

    }

    public String getStudentsID() {
        return StudentsID;
    }

    public void setStudentsID(String StudentsID) {
        this.StudentsID = StudentsID;
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


