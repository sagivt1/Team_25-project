package test;

import Database.ZeroDawnDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Quiz {

    int Id;
    String Name;
    ArrayList<Question> Questions;
    boolean isActive;
    int grade;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<Question> getQuestions() {
        return Questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        Questions = questions;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Quiz() {
        Questions = new ArrayList<Question>();
    }

    public Quiz(int id, boolean isActive, int grade) {
        this.Id = id;
        Questions = new ArrayList<Question>();
        this.isActive = isActive;
        this.grade = grade;
    }

    public Quiz(int id, boolean isActive, String name, int grade) {
        this.Name = name;
        this.Id = id;
        this.isActive = isActive;
        this.grade = grade;
    }

    public Quiz(String name) {
        Name = name;
        Questions = new ArrayList<Question>();
        isActive = true;
    }

    public void InitNewQuiz(){
        Scanner in = new Scanner(System.in);
        System.out.println("----Adding new Test----");

        String confirm;
        do{
            System.out.println("Enter the Test Name");
            Name = in.nextLine();
            System.out.println("Press Y to confirm to change enter any other button");
            confirm = in.next();
            confirm = confirm.toLowerCase();
        }while(!confirm.equals("y"));

        do{
            System.out.println("Enter for which grade for this test");
            grade = in.nextInt();
            System.out.println("Press Y to confirm to change enter any other button");
            confirm = in.next();
            confirm = confirm.toLowerCase();
        }while(!confirm.equals("y"));

        //Question for the test
        System.out.println("----Enter question for the test----");
        String next = "n";
        int countQuestion = 0;
        while(!next.equals("x")){
            String question;

            do{
                in.nextLine();
                System.out.println("Enter the Question");
                question = in.nextLine();
                System.out.println("Press Y to confirm");
                confirm = in.next();
                in.nextLine();
                confirm = confirm.toLowerCase();
            }while(!confirm.equals("y"));

            Questions.add(new Question(question));

            System.out.println("To Enter more question Enter any button to sto Enter x");
            next = in.next();
        }

        AddNewQuizToDB();

    }

    /**
     * Adding new Quiz to the database
     */
    public void AddNewQuizToDB()
    {
        Connection con = ZeroDawnDatabase.GetDbCon();

        if (con == null) {
            System.exit(1);
        }

        try{
            String query = "INSERT INTO test(is_active,test_name,grade) VALUES(?,?,?);";
            PreparedStatement stmt = con.prepareCall(query);

            stmt.setString(1, String.valueOf(1));
            stmt.setString(2, String.valueOf(Name));
            stmt.setString(3, String.valueOf(grade));
            stmt.execute();


            query = "SELECT LAST_INSERT_ID();" ;
            stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(!HadResult){
                System.exit(1);
            }
            ResultSet res = stmt.getResultSet();
            res.next();
            Id = res.getInt(1);


            for(Question quest : Questions)
            {
                query = "INSERT INTO question(test_id,question) VALUES(?,?);";
                stmt = con.prepareCall(query);
                stmt.setString(1, String.valueOf(Id));
                stmt.setString(2, String.valueOf(quest.question));
                stmt.execute();

                query = "SELECT LAST_INSERT_ID();" ;
                stmt = con.prepareCall(query);
                HadResult = stmt.execute();
                if(!HadResult){
                    System.exit(1);
                }
                res = stmt.getResultSet();
                res.next();
                int TempId = res.getInt(1);
                quest.setId(TempId);
            }
            res.close();
            con.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Get specific quiz from the database by quiz id
     * @param id
     */
    public void GetSpecificQuizFromDB(int id)
    {
        Connection con = ZeroDawnDatabase.GetDbCon();

        if (con == null) {
            System.exit(1);
        }

        try{
            String query = "select * from test where test_id = ?;";
            PreparedStatement stmt = con.prepareCall(query);
            stmt.setInt(1, id);
            boolean HadResult = stmt.execute();
            if(HadResult){
                ResultSet res = stmt.getResultSet();
                res.next();
                Id = res.getInt(1);
                isActive = res.getBoolean(2);
                Name = res.getString(3);
                grade = res.getInt(4);
            }

            query = "select * from question where test_id = ?;";
            stmt = con.prepareCall(query);
            stmt.setInt(1, id);
            HadResult = stmt.execute();
            if(HadResult){
                ResultSet res = stmt.getResultSet();
                while(res.next()) {
                    Questions.add(new Question(
                        res.getInt(1), res.getString(3), res.getInt(2)
                    ));
                }
                res.close();
            }

            con.close();
        }catch (SQLException throwables) {
            if(throwables.getErrorCode() == 0){
                this.Id = 0;
            }
            else{
                throwables.printStackTrace();
            }
        }

    }

    /**
     * Remove specific quiz from the database
     * @param id
     */
    public static void RemoveSpecificQuiz(int id)
    {

        Connection con = ZeroDawnDatabase.GetDbCon();

        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "delete from test where test_id = ?;";
            PreparedStatement stmt = con.prepareCall(query);
            stmt.setString(1, String.valueOf(id));
            stmt.execute();
            con.close();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Remove This quiz from the database
     */
    public void RemoveThisQuiz()
    {

        Connection con = ZeroDawnDatabase.GetDbCon();

        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "delete from test where test_id = ?;";
            PreparedStatement stmt = con.prepareCall(query);
            stmt.setString(1, String.valueOf(this.getId()));
            stmt.execute();
            con.close();
        }catch (SQLException throwables) {

            throwables.printStackTrace();
        }


    }

    /**
     * Update this isActive to false in the database
     */
    public void UpdateIsActive(){
        Connection con = ZeroDawnDatabase.GetDbCon();

        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "update test set is_active = 0 where test_id = ?;";
            PreparedStatement stmt = con.prepareCall(query);
            stmt.setString(1, String.valueOf(this.getId()));
            stmt.execute();
            con.close();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void EditName(String NewName){

        Connection con = ZeroDawnDatabase.GetDbCon();

        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "update test set test_name = ? where test_id = ?;";
            PreparedStatement stmt = con.prepareCall(query);
            stmt.setString(1, NewName);
            stmt.setString(2, String.valueOf(this.getId()));
            stmt.execute();
            con.close();
            this.Name = NewName;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static ArrayList<Quiz> GetQuizList(){

        ArrayList<Quiz> Quizzes = new ArrayList<Quiz>();

        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }

        try{
            String query = "select test_id, is_active, test_name, grade from test;";
            PreparedStatement stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(HadResult){
                ResultSet res = stmt.getResultSet();
                while(res.next()){
                    Quizzes.add(new Quiz(res.getInt(1), res.getBoolean(2), res.getString(3),res.getInt(4)));
                }
                res.close();
            }
            con.close();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Quizzes;

    }


    public static ArrayList<Quiz> GetQuizListByGrade(int G){

        ArrayList<Quiz> Quizzes = new ArrayList<Quiz>();

        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }

        try{
            String query = "select test_id, is_active, test_name, grade from test where grade="+G+";";
            PreparedStatement stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(HadResult){
                ResultSet res = stmt.getResultSet();
                while(res.next()){
                    Quizzes.add(new Quiz(res.getInt(1), res.getBoolean(2), res.getString(3),res.getInt(4)));
                }
                res.close();
            }
            con.close();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return Quizzes;

    }

    public void EditQuestions(){

        Scanner in = new Scanner(System.in);
        System.out.println("Select Question to Edit");
        int i = 1;
        for(Question Quest : this.Questions){
            System.out.println(i + ". " +Quest.question);
            i++;
        }

        int choice;
        System.out.println("Please choice question to edit");
        choice = in.nextInt();
        while(choice < 1 || choice > i){
            System.out.println("Invalid choice please try again");
            choice = in.nextInt();
        }
        in.nextLine();
        String confirm, NewQuestion;
        do{
            System.out.println("Enter New Question");
            NewQuestion = in.nextLine();
            System.out.println("Press Y to confirm");
            confirm = in.nextLine();
            confirm = confirm.toLowerCase();
        }while(!confirm.equals("y"));

        Questions.get(choice-1).EditQuestion(NewQuestion);
        System.out.println("Question Has Been Update");
    }




}
