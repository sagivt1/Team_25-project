package users;/*
users.Parent
*/

import Database.ZeroDawnDatabase;
import test.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parent extends User {
    ArrayList<Student> Kids;
    ArrayList<String> New_Kids;

    public Parent() {
        Kids = new ArrayList<Student>();
        New_Kids = new ArrayList<String>();
    }

    public Parent(String ParentID, String password, String lname, String fname, Date birth_date, String email) {
        super(ParentID, password, lname, fname, birth_date, email);
        Kids = new ArrayList<Student>();
        New_Kids = new ArrayList<String>();
        AddKidsToArray();
    }

    public void SignUp(String Id, String Password, String FirstName, String LastName, Date BirthDate,
                       String email) {
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
            query = "INSERT INTO parent Values(?)";
            stmt = con.prepareCall(query);
            stmt.setString(1, Id);
            stmt.execute();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean CheckIfStudent(String cId) {
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        try {
            String query = "SELECT * FROM users JOIN student ON id = user_id WHERE user_id = ?;";
            PreparedStatement stmt = con.prepareCall(query);
            stmt.setString(1, cId);
            boolean HadResult = stmt.execute();
            if (HadResult) {
                ResultSet res = stmt.getResultSet();
                if (res.next()) {
                    res.close();
                    con.close();
                    return true;
                }
            } else {
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
        if (con == null) {
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
            if (check1) {
                AddMyChild(getUserID(), cId);
                System.out.println("child was added");
            } else {
                System.out.println(cId + " this isn't a student ID, please try again..");
            }
        } else {
            System.out.println("you didn't insert an ID, please try again..");
        }
    }

    public void AddKidsToArray() {
        New_Kids.clear();
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
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
        if (con == null) {
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
        } else {
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

    public ArrayList GetKidsArray() {
        return New_Kids;
    }

    public int GetKidsArraySize() {
        return New_Kids.size();
    }

    public void MessageToCounselor() {
        AddKidsToArray();
        if (New_Kids.size() == 0) {
            System.out.println("You didn't added any kid yet");
            System.out.println("You need to add kid first");
        } else {
            Connection con = ZeroDawnDatabase.GetDbCon();
            if (con == null) {
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

    public void Simulation_test() {
        String[][] AnswersCntainer = new String[10][5];
        Scanner in = new Scanner(System.in);
        int CH;
        System.out.println("---------------Welcome---------------");
        System.out.println("to start a new test Press 1");
        System.out.println("to exit Press 0");
        CH = in.nextInt();
        while (CH != 0 && CH != 1) {
            System.out.println("You have selected a wrong number, please select again:");
            CH = in.nextInt();
            System.out.println("to start a test Press 1");
            System.out.println("to exit Press 0");
        }
        if (CH == 1) {
            System.out.println("enter student grade");
            int CHG=in.nextInt();
            ArrayList<Quiz> Quizzes = Quiz.GetQuizList();
            System.out.println("----List Of Tests----");
            ArrayList<Integer> id_test = new ArrayList<>();
            for (Quiz quiz : Quizzes) {
                if (quiz.isActive() && quiz.getGrade() == CHG) {
                    System.out.println(quiz.getId() + ". " + quiz.getName());
                    id_test.add(quiz.getId());
                }
            }
            int choice;
            int j = 0, flag1 = 0;
            System.out.println("Choose test:");
            Quiz quiz = new Quiz();
            while (j < 1) {
                choice = in.nextInt();
                for (int num : id_test) {
                    if (num == choice) {
                        flag1 = 1;
                        quiz.GetSpecificQuizFromDB(choice);
                        if (quiz.getId() == 0) {
                            System.out.println("Invalid Test ID");
                        }
                    }
                }
                if (flag1 == 1) {
                    break;
                }
                System.out.println("You entered a wrong number, please select again");
            }

            int a = 1;
            for (int i = 0; i < quiz.getQuestions().size(); i++) {
                int Q_id = quiz.getQuestions().get(i).getId();
                String Q = quiz.getQuestions().get(i).getQuestion();
                int quiz_id = quiz.getId();
                AnswersCntainer[i][0] = String.valueOf(quiz_id);
                AnswersCntainer[i][1] = String.valueOf(UserID);
                AnswersCntainer[i][2] = String.valueOf(Q_id);
                System.out.println("Q number " + a + " : ");
                System.out.println(Q);
                System.out.println("Please select an answer: ");
                System.out.println("1. is a ");
                System.out.println("2. as a ");
                System.out.println("3. dont know");
                System.out.println("4. all");
                int Ans = in.nextInt();
                while (Ans != 1 && Ans != 2 && Ans != 3 && Ans != 4) {
                    System.out.println("You have selected a wrong number, please select again:");
                    System.out.println("Q number " + a + " : ");
                    System.out.println(Q);
                    System.out.println("Please select an answer: ");
                    System.out.println("1. is a ");
                    System.out.println("2. as a ");
                    System.out.println("3. dont know");
                    System.out.println("4. all");
                    Ans = in.nextInt();
                }
                AnswersCntainer[i][3] = String.valueOf(Ans);
                AnswersCntainer[i][4] = Q;
                int flag = 1;
                int temp_choice = 0;
                while (flag == 1) {
                    System.out.println("if you want to update your last answer press 1");
                    System.out.println("if you want to continue to the next Question press 2");
                    temp_choice = in.nextInt();
                    if (temp_choice == 1 || temp_choice == 2) {
                        flag = 0;
                    }
                }
                if (temp_choice == 1) {
                    while (Ans != 1 && Ans != 2 && Ans != 3 && Ans != 4) {
                        System.out.println("You have selected a wrong number, please select again:");
                        System.out.println("Q number " + a + " : ");
                        System.out.println(Q);
                        System.out.println("Please select an answer: ");
                        System.out.println("1. is a ");
                        System.out.println("2. as a ");
                        System.out.println("3. dont know");
                        System.out.println("4. all");
                        Ans = in.nextInt();
                        AnswersCntainer[i][3] = String.valueOf(Ans);
                    }
                }
                a++;
                if (i + 1 == quiz.getQuestions().size()) {
                    System.out.println("You have completed the quiz ");
                }
            }
        }
    }

    public void MessageFromCounselor() {
        ArrayList<String> msg_reports = new ArrayList<String>();
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "SELECT student_ID,msg FROM cmessage WHERE parent_id = " + getUserID();
            PreparedStatement stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(HadResult){
                ResultSet res = stmt.getResultSet();
                while(res.next()){
                    msg_reports.add(res.getString("student_id"));
                    msg_reports.add(res.getString("msg"));
                }
                if (msg_reports.size() == 0) {
                    System.out.println("there is no messages");
                }
                else {
                    int i, j = 1;
                    for (i = 0; i < msg_reports.size(); i += 2) {
                        System.out.println("Message number " + j);
                        j++;
                        System.out.println("Kid ID: " + msg_reports.get(i));
                        System.out.println("The Message: " + msg_reports.get(i + 1));
                    }
                }
                res.close();
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void Show_Riview(){
        Scanner in = new Scanner(System.in);
        System.out.println("select kid to show review: ");
        AddKidsToArray();
        ShowMyKids();
        String s=in.nextLine();
        Tests_my_kid_done(s);
        System.out.println("select Teset :");
        int n = in.nextInt();
        ArrayList<String> msg_reports = new ArrayList<String>();
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "select user_id, review_for_parents from review where user_id = (select student_id from kids)" +
                    "and user_id="+s+" and test_id="+n+";";
            PreparedStatement stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(HadResult){
                ResultSet res = stmt.getResultSet();
                while(res.next()){
                    msg_reports.add(res.getString(1));
                    msg_reports.add(res.getString(2));
                }
                res.close();
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        int k=1;
        for (int j=0;j<msg_reports.size();j+=4) {
            System.out.println(k+".");
            System.out.println("Student ID: "+msg_reports.get(j));
            System.out.println("Review: "+msg_reports.get(j+1));
            k++;
        }
    }

    public void Tests_my_kid_done(String st){
        Scanner in = new Scanner(System.in);
        ArrayList<Integer> Quiz_i_done = new ArrayList<Integer>();
        Connection con = ZeroDawnDatabase.GetDbCon();
        int i=0;
        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "select test_id from start_test where user_id =" + st + ";";
            PreparedStatement stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(HadResult){
                ResultSet res = stmt.getResultSet();
                while(res.next()){
                    Quiz_i_done.add(res.getInt(1));
                    i++;
                }
                res.close();
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Set<Integer> s = new LinkedHashSet<>(Quiz_i_done);
        System.out.println("----List Of Tests----");
        for (Integer integer : s) {
            System.out.println(integer);
        }
    }

    public void Get_Status(){
        Scanner in = new Scanner(System.in);
        System.out.println("select kid to show Status: ");
        AddKidsToArray();
        ShowMyKids();
        String s=in.nextLine();
        ArrayList<String> msg_reports = new ArrayList<String>();
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "select user_id, update_parents from student_monitoring where user_id = (select student_id from kids)" +
                    "and user_id="+s+";";
            PreparedStatement stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(HadResult){
                ResultSet res = stmt.getResultSet();
                while(res.next()){
                    msg_reports.add(res.getString(1));
                    msg_reports.add(res.getString(2));
                }
                res.close();
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        int k=1;
        for (int j=0;j<msg_reports.size();j+=4) {
            System.out.println(k+".");
            System.out.println("Student ID: "+msg_reports.get(j));
            System.out.println("Status: "+msg_reports.get(j+1));
            k++;
        }
    }
}
