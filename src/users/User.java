package users;

import Database.ZeroDawnDatabase;

import java.sql.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
User basic class
*/
public class User {


    protected String UserID;
    protected String password;
    protected String lname;
    protected String fname;
    protected Date birth_date;
    protected String email;

    public User() {
    }

    public User(String UserID, String password, String fname, String lname, Date birth_date, String email){
        this.UserID = UserID;
        this.lname = lname;
        this.fname = fname;
        this.password = password;
        this.birth_date = birth_date;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return UserID.equals(user.UserID) && lname.equals(user.lname) && fname.equals(user.fname) && password.equals(user.password) && birth_date.equals(user.birth_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UserID, lname, fname, password, birth_date);
    }

    @Override
    public String toString() {
        return "User{" +
                "UserID='" + UserID + '\'' +
                ", lname='" + lname + '\'' +
                ", fname='" + fname + '\'' +
                ", password='" + password + '\'' +
                ", birth_date='" + birth_date + '\'' +
                '}';
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

    public Date getbirth_date() {
        return birth_date;
    }

    public void setbirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public static User Login(String Id, String Password )
    {
        Connection con = ZeroDawnDatabase.GetDbCon();
        if(con == null)
        {
            System.exit(1);
        }

        try {
            String query = "SELECT * FROM users " +
                    "JOIN parent " +
                    "ON id = user_id " +
                    "WHERE users.id = ? AND users.password = ?;";
            PreparedStatement stmt = con.prepareCall(query);
            stmt.setString(1,Id);
            stmt.setString(2,Password);

            boolean HadResult = stmt.execute();

            if(HadResult) {
                ResultSet res = stmt.getResultSet();
                if(res.next()) {
                    Parent parent = new Parent(
                            res.getString("id"),
                            res.getString("password"),
                            res.getString("first_name"),
                            res.getString("last_name"),
                            res.getDate("birth_date"),
                            res.getString("email")
                    );
                    res.close();
                    return parent;
                }
                res.close();
            }

            query = "SELECT * FROM users " +
                    "JOIN student " +
                    "ON id = user_id " +
                    "WHERE users.id = ? AND users.password = ?;";
            stmt = con.prepareCall(query);
            stmt.setString(1,Id);
            stmt.setString(2,Password);

            HadResult = stmt.execute();

            if(HadResult) {
                ResultSet res = stmt.getResultSet();
                if(res.next()) {
                    Student student = new Student(
                            res.getString("id"),
                            res.getString("password"),
                            res.getString("first_name"),
                            res.getString("last_name"),
                            res.getDate("birth_date"),
                            res.getString("email")
                    );
                    res.close();
                    con.close();
                    return student;
                }
                res.close();
            }

            query = "SELECT * FROM users " +
                    "JOIN counselor " +
                    "ON id = user_id " +
                    "WHERE users.id = ? AND users.password = ?;";
            stmt = con.prepareCall(query);
            stmt.setString(1,Id);
            stmt.setString(2,Password);

            HadResult = stmt.execute();

            if(HadResult) {
                ResultSet res = stmt.getResultSet();
                if(res.next()) {
                    Counselor counselor = new Counselor(
                            res.getString("id"),
                            res.getString("password"),
                            res.getString("first_name"),
                            res.getString("last_name"),
                            res.getDate("birth_date"),
                            res.getString("email")
                    );
                    res.close();
                    con.close();
                    return counselor;
                }
                res.close();
            }

            con.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }


    public void EditEmail()
    {
        Scanner scanM = new Scanner(System.in);
        String NewMail;

        while (true) {
            String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
            Pattern pattern = Pattern.compile(regex);
            System.out.print("Enter user email: ");
            NewMail = scanM.nextLine();
            Matcher match = pattern.matcher(NewMail);
            if (!NewMail.isEmpty() && match.matches()) {
                break;
            }
            else {
                System.out.println("you didn't insert an email or the email is Invalid, please try again..");
            }
        }

        Connection con = ZeroDawnDatabase.GetDbCon();
        try {
            String query = "update users set email = ? where id = ?;";
            assert con != null;
            PreparedStatement stmt = con.prepareCall(query);

            stmt.setString(1,NewMail);
            stmt.setString(2,UserID);
            stmt.execute();
            email = NewMail;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void EditLastName()
    {
        Scanner scanM = new Scanner(System.in);
        String NewLastName;

        while (true) {
            System.out.print("Enter user new last name: ");
            NewLastName = scanM.nextLine();
            if (!NewLastName.isEmpty()) {
                break;
            }
            else {
                System.out.println("you didn't insert your new last name, please try again..");
            }
        }

        Connection con = ZeroDawnDatabase.GetDbCon();
        try {
            String query = "update users set last_name = ? where id = ?;";
            assert con != null;
            PreparedStatement stmt = con.prepareCall(query);

            stmt.setString(1,NewLastName);
            stmt.setString(2,UserID);
            stmt.execute();
            lname = NewLastName;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void EditFristName()
    {
        Scanner scanM = new Scanner(System.in);
        String NewFirstName;

        while (true) {
            System.out.print("Enter user new first name: ");
            NewFirstName = scanM.nextLine();
            if (!NewFirstName.isEmpty()) {
                break;
            }
            else {
                System.out.println("you didn't insert your new first name, please try again..");
            }
        }

        Connection con = ZeroDawnDatabase.GetDbCon();
        try {
            String query = "update users set first_name = ? where id = ?;";
            assert con != null;
            PreparedStatement stmt = con.prepareCall(query);

            stmt.setString(1,NewFirstName);
            stmt.setString(2,UserID);
            stmt.execute();
            fname = NewFirstName;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void EditPassword(){
        Scanner scanM = new Scanner(System.in);
        String NewPassword;

        while (true) {
            System.out.print("Enter new password: ");
            NewPassword = scanM.nextLine();
            if (!NewPassword.isEmpty()) {
                break;
            }
            else {
                System.out.println("you didn't insert a new password, please try again..");
            }
        }

        Connection con = ZeroDawnDatabase.GetDbCon();
        try {
            String query = "update users set password = ? where id = ?;";
            assert con != null;
            PreparedStatement stmt = con.prepareCall(query);

            stmt.setString(1,NewPassword);
            stmt.setString(2,UserID);
            stmt.execute();
            password = NewPassword;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
