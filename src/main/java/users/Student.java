package users;

import Database.ZeroDawnDatabase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
Student class
 */
public class Student extends User {

    int grade;

    public Student() {
    }

    public Student(String StudentsID, String password, String lname, String fname, Date birth_date, String email, int grade){
        super(StudentsID, password, lname, fname, birth_date, email);
        this.grade = grade;
    }

    public void SignUp(String Id, String Password, String FirstName, String LastName, Date BirthDate,
                       String email,int grade) {
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        try {
            String query = "INSERT INTO users Values(?,?,?,?,?,?)";
            PreparedStatement stmt = con.prepareCall(query);


            this.UserID = Id;
            stmt.setString(1, Id);
            this.password = Password;
            stmt.setString(2, Password);
            this.fname = FirstName;
            stmt.setString(3, FirstName);
            this.lname = LastName;
            stmt.setString(4, LastName);
            this.birth_date = BirthDate;
            stmt.setString(5, BirthDate.toString());
            this.email = email;
            stmt.setString(6, email);

            stmt.execute();

            query = "INSERT INTO student Values(?,?)";
            stmt = con.prepareCall(query);
            stmt.setString(1, Id);
            this.grade = grade;
            stmt.setInt(2, grade);
            stmt.execute();
            con.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



}


