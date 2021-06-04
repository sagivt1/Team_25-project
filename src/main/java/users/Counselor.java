package users;/*
users.Counselor
*/

import Database.ZeroDawnDatabase;
import test.Quiz;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class Counselor extends users.User {
    ArrayList<String> All_Kids;
    ArrayList<String> Kids_Parents;


    public Counselor() {
        All_Kids = new ArrayList<String>();
        Kids_Parents = new ArrayList<String>();
    }

    public Counselor(String CounselorID, String password, String lname, String fname, Date birth_date, String email){
        super(CounselorID, password, lname, fname, birth_date, email);
        All_Kids = new ArrayList<String>();
        Kids_Parents = new ArrayList<String>();
        AddAllKidsToArray();
    }

    public void SignUp(String Id, String Password, String FirstName, String LastName,  Date BirthDate,
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

            query = "INSERT INTO counselor Values(?)";
            stmt = con.prepareCall(query);
            stmt.setString(1, Id);
            stmt.execute();
            con.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void AddNewTest(){
        Quiz test = new Quiz();
        test.InitNewQuiz();
    }

    public void RemoveOrHaltQuiz(){

        Scanner in = new Scanner(System.in);
        ArrayList<Quiz> Quizzes = Quiz.GetQuizList();

        System.out.println("----List Of Tests----");
        for(Quiz quiz : Quizzes){
            if(quiz.isActive())
                System.out.println(quiz.getId() + ". " + quiz.getName());
        }

        int choice;
        System.out.println("Choose test");
        choice = in.nextInt();
        Quiz quiz = new Quiz();
        quiz.GetSpecificQuizFromDB(choice);
        if(quiz.getId() == 0)
        {
            System.out.println("Invalid Test ID");
            return;
        }


        while(true) {

            System.out.println("----Choose----");
            System.out.println("1.Remove Test");
            System.out.println("2.Inactive Test");
            System.out.println("3.Exit");
            choice = in.nextInt();

            switch (choice) {
                case 1:
                    quiz.RemoveThisQuiz();
                    return;
                case 2:
                    quiz.UpdateIsActive();
                    return;
                case 3:
                    return;
                default:
                    break;
            }
        }
    }

    public void EditTest(){

        Scanner in = new Scanner(System.in);
        ArrayList<Quiz> Quizzes = Quiz.GetQuizList();

        System.out.println("----List Of Tests----");
        for(Quiz quiz : Quizzes){
            if(quiz.isActive())
                System.out.println(quiz.getId() + ". " + quiz.getName());
        }

        int choice;
        System.out.println("Choose test");
        choice = in.nextInt();
        in.nextLine();
        Quiz quiz = new Quiz();
        quiz.GetSpecificQuizFromDB(choice);
        if(quiz.getId() == 0)
        {
            System.out.println("Invalid Test ID");
            return;
        }


        while(true) {

            System.out.println("----Choose----");
            System.out.println("1.Edit Test name");
            System.out.println("2.Edit Questions");
            System.out.println("3.Exit");
            String Option = in.nextLine();

            switch (Option){

                case "1" :
                    String confirm;
                    String NewName;
                    do{
                        System.out.println("Enter the Test Name");
                        NewName = in.nextLine();
                        System.out.println("Press Y to confirm to change enter any other button");
                        confirm = in.nextLine();
                        confirm = confirm.toLowerCase();
                    }while(!confirm.equals("y"));
                    quiz.EditName(NewName);
                    break;
                case "2" :
                    quiz.EditQuestions();
                    break;
                case "3":
                    return;
                default:
                    break;

            }
        }



    }

    public void Show_Feedback ()
    {
        Scanner in = new Scanner(System.in);
        ArrayList<Integer> Quiz_with_feedback = new ArrayList<Integer>();
        String[] strin = new String[1000];
        Connection con = ZeroDawnDatabase.GetDbCon();
        int i=0,k=0;
        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "select test_id from feedback;";
            PreparedStatement stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(HadResult){
                ResultSet res = stmt.getResultSet();
                while(res.next()){
                    Quiz_with_feedback.add(res.getInt(1));
                }
                res.close();
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Set<Integer> s = new LinkedHashSet<>(Quiz_with_feedback);
        System.out.println("----List Of Tests----");
        for (Integer integer : s) {
            System.out.println(integer);
        }
        System.out.println("Please select test number to see the feedback: ");
        int CH=in.nextInt();
        int flag=0;
        while(i<1) {
            for (Integer integer : s) {
                if(integer == CH){
                    flag=1;
                    break;
                }
            }
            if(flag==1){
                break;
            }
            System.out.println("Please select test number from this list: ");
            System.out.println("----List Of Tests----");
            for (Integer integ : s) {
                System.out.println(integ);
            }
            CH=in.nextInt();
        }
        try{
            String query = "select * from feedback where test_id=" + CH + ";";
            PreparedStatement stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(HadResult){
                ResultSet res = stmt.getResultSet();
                while(res.next()){
                    strin[i]=(res.getString(2));
                    i++;
                }
                res.close();
                System.out.println("The Feedbacks: ");
                while (k<i){
                    int t=k+1;
                    System.out.println(t+"."+strin[k]);
                    k++;
                }
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void Update_Student_Grade(){
        int flag=0;
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        Scanner in = new Scanner(System.in);
        Show_Student_list();
        System.out.println("Enter the ID of the student you want to update:");
        String IDS = in.nextLine();
        System.out.println("Enter the Grade you want to change to:");
        String NG = in.nextLine();
        try{
            String query1 = "UPDATE student set grade="+NG+" where user_id =" +IDS+";";
            PreparedStatement stmt1 = con.prepareCall(query1);
            stmt1.execute();
            con.close();
        }catch (SQLException throwables) {
            System.out.println("Student not found");
            flag=1;
            throwables.printStackTrace();
        }
        if(flag==0)
        {
            System.out.println("Student details changed successfully");

        }
    }

    //Delete student from student data base
    public void Delete_Student(){
        int flag=0;
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        Scanner in = new Scanner(System.in);
        Show_Student_list();
        System.out.println("Enter the ID of the student you want to delete:");
        String IDS = in.nextLine();
        try{
            String query = "delete from users where id="+IDS+";";
            PreparedStatement stmt = con.prepareCall(query);
            stmt.execute();
            con.close();
        }catch (SQLException throwables) {
            System.out.println("Student not found");
            flag=1;
            //throwables.printStackTrace();
        }
        if(flag==0){
            System.out.println("Student delete successfully");
        }
    }

    public void Show_Student_list(){
        Scanner in = new Scanner(System.in);
        ArrayList<String> Student_list = new ArrayList<String>();
        Connection con = ZeroDawnDatabase.GetDbCon();
        int i=0;
        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "select user_id from student;";
            PreparedStatement stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(HadResult){
                ResultSet res = stmt.getResultSet();
                while(res.next()){
                    Student_list.add(res.getString(1));
                    i++;
                }
                res.close();
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Set<String> s = new LinkedHashSet<>(Student_list);
        System.out.println("----ID List Of Students----");
        for (String string : s) {
            System.out.println(string);
        }
    }

    public void AddAllKidsToArray() {
        All_Kids.clear();
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        try {
            String query = "SELECT student_id FROM kids";
            PreparedStatement stmt = con.prepareCall(query);
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                All_Kids.add(res.getString("student_id"));
            }
            res.close();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void ShowALLKids() {
        int i;
        for (i = 0; i < All_Kids.size(); i++) {
            System.out.println(i + 1 + ".ID: " + All_Kids.get(i));
        }
    }

    public String ChooseFromKidsArray() {
        Scanner scanM = new Scanner(System.in);
        int Opt = scanM.nextInt();
        while (Opt < 1 && Opt > All_Kids.size()) {
            System.out.print("Wrong Input, try again: ");
            Opt = scanM.nextInt();
        }
        return All_Kids.get(Opt -1);
    }

    public void AddParentsToArray(String Opt) {
        Kids_Parents.clear();
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        try {
            String query = "SELECT parent_id FROM kids WHERE student_id = " + Opt;
            PreparedStatement stmt = con.prepareCall(query);
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                Kids_Parents.add(res.getString("parent_id"));
            }
            res.close();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void ShowKidsParents() {
        int i;
        for (i = 0; i < Kids_Parents.size(); i++) {
            System.out.println(i + 1 + ".ID: " + Kids_Parents.get(i));
        }
    }

    public String ChooseFromParentsArray() {
        Scanner scanM = new Scanner(System.in);
        int Opt = scanM.nextInt();
        while (Opt < 1 && Opt > Kids_Parents.size()) {
            System.out.print("Wrong Input, try again: ");
            Opt = scanM.nextInt();
        }
        return Kids_Parents.get(Opt -1);
    }

    public void MessageToParent() {
        AddAllKidsToArray();
        if (All_Kids.size() == 0) {
            System.out.println("You didn't added any kid yet");
            System.out.println("You need to add kid first");
        } else {
            Connection con = ZeroDawnDatabase.GetDbCon();
            if (con == null) {
                System.exit(1);
            }
            try {
                System.out.println("Choose a child from the list you would like to send a message to the counselor about");
                ShowALLKids();
                String kid = ChooseFromKidsArray();
                AddParentsToArray(kid);
                ShowKidsParents();
                String parent = ChooseFromParentsArray();
                System.out.println("Enter your message to the counselor");
                Scanner scan = new Scanner(System.in);
                String msg = scan.nextLine();
                String query = "INSERT INTO cmessage(student_id,parent_id,msg) Values(?,?,?)";
                PreparedStatement stmt = con.prepareCall(query);
                stmt.setString(1, kid);
                stmt.setString(2, parent);
                stmt.setString(3, msg);
                stmt.execute();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void ShowMessagesFromParents() {

    }

    public void AlertedKids() {

    }
}




