package users;

import Database.ZeroDawnDatabase;
import test.Question;
import test.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/*
Student class
 */
public class Student extends User {
    int grade;

    public Student() {
    }

    public Student(String StudentsID, String password, String lname, String fname, Date birth_date, String email, int grade) {
        super(StudentsID, password, lname, fname, birth_date, email);
        this.grade = grade;
    }

    public void SignUp(String Id, String Password, String FirstName, String LastName, Date BirthDate,
                       String email, int grade) {
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

    public void start_test1(String ans[][]){
        Scanner in = new Scanner(System.in);
        Quiz quiz = new Quiz();
        quiz.GetSpecificQuizFromDB(Integer.parseInt(ans[0][0]));
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            String quiz_id=ans[i][0];
            String UserID = ans[i][1];
            String Q_id = ans[i][2];
            String ansr = ans[i][3];
            Connection con = ZeroDawnDatabase.GetDbCon();
            if (con == null) {
                System.exit(1);
            }
            try {
                String query = "INSERT INTO start_test(test_id,user_id,question_id,ans) Values(?,?,?,?)";
                PreparedStatement stmt = con.prepareCall(query);
                stmt.setString(1, String.valueOf(quiz_id));
                stmt.setString(2, UserID);
                stmt.setString(3,Q_id);
                stmt.setInt(4, Integer.parseInt(ansr));
                stmt.execute();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public String[][] start_test() {
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
            ArrayList<Quiz> Quizzes = Quiz.GetQuizList();
            System.out.println("----List Of Tests----");
            ArrayList<Integer> id_test = new ArrayList<>();
            for (Quiz quiz : Quizzes) {
                if (quiz.isActive() && quiz.getGrade() == grade) {
                    System.out.println(quiz.getId() + ". " + quiz.getName());
                    id_test.add(quiz.getId());
                }
            }
            int choice;
            int j=0,flag1=0;
            System.out.println("Choose test:");
            Quiz quiz = new Quiz();
            while (j<1) {
                choice = in.nextInt();
                for (int num : id_test) {
                    if (num == choice) {
                        flag1=1;
                        quiz.GetSpecificQuizFromDB(choice);
                        if (quiz.getId() == 0) {
                            System.out.println("Invalid Test ID");
                            return AnswersCntainer;
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
                while(Ans!=1 && Ans!=2 && Ans!=3 && Ans!=4)
                {
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
                int flag=1;
                int temp_choice=0;
                while (flag==1) {
                    System.out.println("if you want to update your last answer press 1");
                    System.out.println("if you want to continue to the next Question press 2");
                    temp_choice = in.nextInt();
                    if(temp_choice==1 || temp_choice==2){
                        flag=0;
                    }
                }
                if(temp_choice==1){
                    while(Ans!=1 && Ans!=2 && Ans!=3 && Ans!=4) {
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
                if(i+1==quiz.getQuestions().size())
                {
                    System.out.println("You have completed the quiz ");
                }
            }
            return AnswersCntainer;
        }
        else{
            return AnswersCntainer;
        }
    }


    public void start_test2()
    {
        start_test1(start_test());
    }
}