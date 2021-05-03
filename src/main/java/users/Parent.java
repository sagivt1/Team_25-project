package users;/*
users.Parent
*/

import Database.ZeroDawnDatabase;

import java.sql.*;
import java.util.ArrayList;

public class Parent extends User {

    ArrayList<Student> Kids;

    public Parent() {
        Kids = new ArrayList<Student>();
    }

    public Parent(String ParentID, String password, String lname, String fname, Date birth_date, String email){
        super(ParentID,password, lname, fname,  birth_date, email);
        Kids = new ArrayList<Student>();
    }

    public void SignUp(String Id, String Password, String FirstName, String LastName, Date BirthDate,
                       String email){
        Kids = new ArrayList<Student>();
        Connection con = ZeroDawnDatabase.GetDbCon();
        if(con == null)
        {
            System.exit(1);
        }
        try {
            String query = "INSERT INTO users Values(?,?,?,?,?,?)";
            PreparedStatement stmt = con.prepareCall(query);


            this.UserID = Id;
            stmt.setString(1,Id);
            this.password = Password;
            stmt.setString(2,Password);
            this.fname = FirstName;
            stmt.setString(3,FirstName);
            this.lname = LastName;
            stmt.setString(4,LastName);
            this.birth_date = BirthDate;
            stmt.setString(5,BirthDate.toString());
            this.email = email;
            stmt.setString(6,email);

            stmt.execute();

            query = "INSERT INTO parent Values(?)";
            stmt = con.prepareCall(query);
            stmt.setString(1,Id);
            stmt.execute();
            con.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }







}
