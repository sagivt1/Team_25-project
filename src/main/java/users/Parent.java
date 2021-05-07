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
        AddKidsToArray();
    }

    public void SignUp(String Id, String Password, String FirstName, String LastName, Date BirthDate,
                       String email){
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
            String query = "INSERT INTO kids Values(?,?)";
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
                System.out.println("child was added");
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
        New_Kids.clear();
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
            }
            res.close();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void DeleteChildFromDB(String pId, String cId) {
        Connection con = ZeroDawnDatabase.GetDbCon();
        if(con == null)
        {
            System.exit(1);
        }
        try {
            String query = "DELETE FROM kids WHERE parent_id = " + pId + " AND student_id = " + cId;
            PreparedStatement stmt = con.prepareCall(query);
            stmt.executeUpdate(query);
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void ShowMyKids() {
        int i;
        for (i = 0; i < New_Kids.size(); i++) {
            System.out.println(i + 1 + ".ID: " + New_Kids.get(i));
        }
    }

    public void RemoveChild() {
        AddKidsToArray();
        if (New_Kids.size() == 0) {
            System.out.println("You didn't added any kid yet");
            System.out.println("You need to add kid first");
        }
        else {
            System.out.println("Choose a kid from the list to remove");
            ShowMyKids();
            int Opt = ChooseKidFromArray();
            DeleteChildFromDB(getUserID(), New_Kids.get(Opt - 1));
            System.out.println("child was deleted");
        }
    }

    public int ChooseKidFromArray() {
        Scanner scanM = new Scanner(System.in);
        int Opt = scanM.nextInt();
        while (Opt < 1 && Opt > New_Kids.size()) {
            System.out.print("Wrong Input, try again: ");
            Opt = scanM.nextInt();
        }
        return Opt;
    }

    public void MessageToCounselor() {
        AddKidsToArray();
        if (New_Kids.size() == 0) {
            System.out.println("You didn't added any kid yet");
            System.out.println("You need to add kid first");
        }
        else {
            Connection con = ZeroDawnDatabase.GetDbCon();
            if(con == null)
            {
                System.exit(1);
            }
            try {
                System.out.println("Choose a child from the list you would like to send a message to the counselor about");
                ShowMyKids();
                int Opt = ChooseKidFromArray();
                System.out.println("Enter your message to the counselor");
                Scanner scan = new Scanner(System.in);
                String msg = scan.nextLine();
                String query = "INSERT INTO message(parent_id,student_id,msg) Values(?,?,?)";
                PreparedStatement stmt = con.prepareCall(query);
                stmt.setString(1, getUserID());
                stmt.setString(2, New_Kids.get(Opt - 1));
                stmt.setString(3, msg);
                stmt.execute();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
