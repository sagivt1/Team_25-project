package users;

import Database.ZeroDawnDatabase;
//import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import test.Question;
import test.Quiz;

import java.sql.*;
import java.sql.Date;
import java.util.*;

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


    public void Add_feedback(){
        Scanner in = new Scanner(System.in);
        ArrayList<Integer> Quiz_i_done = new ArrayList<Integer>();
        Connection con = ZeroDawnDatabase.GetDbCon();
        int i=0;
        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "select test_id from start_test where user_id =" + UserID + ";";
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
        System.out.println("Please select test number: ");
        int CH=in.nextInt();
        int flag=0;
        while(i<100) {
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
        System.out.println("enter your feedback:");
        String FD1 = in.nextLine();
        String FD = in.nextLine();
        try {
            String query = "INSERT IGNORE INTO feedback(test_id, feedback_on_test, user_id) VALUES(?,?,?)";
            PreparedStatement stmt = con.prepareCall(query);
            stmt.setInt(1,CH);
            stmt.setString(2,FD);
            stmt.setString(3, UserID);
            stmt.execute();
            con.close();
            System.out.println("Thanks for your feedback!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        }



    public void Warn_about_friend_in_distress()
    {
        Scanner in = new Scanner(System.in);
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }

        System.out.println("Enter the student details you would like to report:");
        System.out.println("Student name: ");
        String name = in.nextLine();
        System.out.println("Student Last name: ");
        String Lname = in.nextLine();
        System.out.println("Student Grade: ");
        String grade1 = in.nextLine();
        System.out.println("What you would like to report on: ");
        String report = in.nextLine();
        try {
            String query = "INSERT IGNORE INTO Alert(msg,user_id, student_name, student_Lname , grade) " +
                    "VALUES(?,?,?,?,?)";
            PreparedStatement stmt = con.prepareCall(query);
            stmt.setString(1,report);
            stmt.setString(2, UserID);
            stmt.setString(3,name);
            stmt.setString(4,Lname);
            stmt.setString(5,grade1);
            stmt.execute();
            con.close();
            System.out.println("Thanks for your report!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void Test_i_made(){
        Scanner in = new Scanner(System.in);
        ArrayList<Integer> Quiz_i_done = new ArrayList<Integer>();
        Connection con = ZeroDawnDatabase.GetDbCon();
        int i=0;
        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "select test_id from start_test where user_id =" + UserID + ";";
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
}
