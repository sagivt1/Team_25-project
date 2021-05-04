package users;/*
users.Counselor
*/

import Database.ZeroDawnDatabase;
import test.Quiz;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;

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
        test.AddNewQuizToDB();
    }

    public void RemoveOrHaltQuiz(){

        Scanner in = new Scanner(System.in);
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }

        ArrayList<Quiz> Quizzes = new ArrayList<Quiz>();

        try{
            String query = "select test_id, is_active, test_name from test;";
            PreparedStatement stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(HadResult){
                ResultSet res = stmt.getResultSet();
                while(res.next()){
                    Quizzes.add(new Quiz(res.getInt(1), res.getBoolean(2), res.getString(3)));
                }
            }
            con.close();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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

        System.out.println("----Choose----");
        System.out.println("1.Remove Test");
        System.out.println("2.Inactive Test");
        System.out.println("3.Exit without change");

        choice = in.nextInt();


        while(true) {
            switch (choice) {
                case 1:
                    quiz.RemoveSpecificQuiz();
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






}
