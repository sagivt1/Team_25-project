package users;/*
users.Parent
*/

import Database.ZeroDawnDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parent extends User {
    ArrayList<Student> Kids;
    ArrayList<String> New_Kids;

    public Parent() {
        Kids = new ArrayList<Student>();
        New_Kids = new ArrayList<String>();
    }

    public Parent(String ParentID, String password, String lname, String fname, Date birth_date, String email){
        super(ParentID,password, lname, fname,  birth_date, email);
        Kids = new ArrayList<Student>();
        New_Kids = new ArrayList<String>();
    }

    public void SignUp(String Id, String Password, String FirstName, String LastName, Date BirthDate,
                       String email){
        Kids = new ArrayList<Student>();
        New_Kids = new ArrayList<String>();
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

    public boolean CheckIfStudent(String cId) {
        Connection con = ZeroDawnDatabase.GetDbCon();
        if(con == null)
        {
            System.exit(1);
        }
        try {
            String query = "SELECT * FROM users JOIN student ON id = user_id WHERE user_id = ?;";
            PreparedStatement stmt = con.prepareCall(query);
            stmt.setString(1, cId);
            boolean HadResult = stmt.execute();
            if (HadResult) {
                ResultSet res = stmt.getResultSet();
                if (res.next()){
                    res.close();
                    con.close();
                    return true;
                }
            }
            else{
                con.close();
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void AddMyChild(String pId, String cId) {
        Connection con = ZeroDawnDatabase.GetDbCon();
        if(con == null)
        {
            System.exit(1);
        }
        try {
            String query = "INSERT INTO kids Values(?,?)";// need to check new DB
            PreparedStatement stmt = con.prepareCall(query);
            stmt.setString(1, pId);
            stmt.setString(2, cId);
            stmt.execute();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void AddChild() {
        Scanner scanM = new Scanner(System.in);
        System.out.printf("Enter your child id: ");
        String cId = scanM.nextLine();
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(cId);
        if (cId.length() == 9 && m.matches()) {
            boolean check1 = CheckIfStudent(cId);
            if (check1){
                AddMyChild(getUserID(), cId);
                System.out.println("child was added\n");
            }
            else {
                System.out.println(cId + " this isn't a student ID, please try again..");
            }
        }
        else {
            System.out.println("you didn't insert an ID, please try again..");
        }
    }

    public void AddKidsToArray() {
        Connection con = ZeroDawnDatabase.GetDbCon();
        if(con == null)
        {
            System.exit(1);
        }
        try {
            String query = "SELECT student_id FROM kids WHERE parent_id = " + getUserID();
            PreparedStatement stmt = con.prepareCall(query);
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                New_Kids.add(res.getString("student_id"));
                //System.out.println("kids are:   " + res.getString("student_id"));
            }
            res.close();
            con.close();
            System.out.println("the student array is this long: " + New_Kids.size());
            System.out.println("the kids id is: " + New_Kids);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void DeleteChild(String pId, String cId) {
        Connection con = ZeroDawnDatabase.GetDbCon();
        if(con == null)
        {
            System.exit(1);
        }
        try {
            //need to think how to delete the child
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void RemoveChild() {
        AddKidsToArray();
        System.out.println("Choose a kid from the list to remove");
        int i;
        int j = 0;
        String cId = "15"; //need to get all child id time after time from DB
        for (i = 1; i <= j; i++) { //loop to print all the children id's
            System.out.println(i + ". ID: " + cId);
        }
        Scanner scanM = new Scanner(System.in);
        int Opt = scanM.nextInt();
        while (Opt < 1  && Opt > j)
        {
            System.out.print("Wrong Input, try again: ");
            Opt = scanM.nextInt();
        }
        // need to get id from DB with Opt
        String ChoosenChildID = "child";
        DeleteChild(getUserID(), ChoosenChildID);

    }
}
