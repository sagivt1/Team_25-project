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



    public Counselor() {
    }

    public Counselor(String CounselorID, String password, String lname, String fname, Date birth_date, String email){
        super(CounselorID, password, lname, fname, birth_date, email);
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
        //in.nextLine();
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

}




