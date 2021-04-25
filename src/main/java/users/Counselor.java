package users;/*
users.Counselor
*/

import Database.ZeroDawnDatabase;

import java.sql.*;
import java.sql.Date;
import java.util.*;

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

    public void AddNewTest() throws SQLException {
        Scanner in = new Scanner(System.in);
        System.out.println("----Adding new Test----");
        System.out.println("Enter the passing score");
        int TestId = 0;
        int QuestId = 0;
        int score = 0;
        ArrayList<String> answers = new ArrayList<String>();
        Connection con = ZeroDawnDatabase.GetDbCon();

        do{
            System.out.println("Enter number between 0 - 100");
            score = in.nextInt();
        }while(score > 100 || score < 0);


        if (con == null) {
            System.exit(1);
        }

        try{

            String query = "INSERT INTO test(is_active,pass_score) VALUES(?,?);";
            PreparedStatement stmt = con.prepareCall(query);

            stmt.setString(1, String.valueOf(1));
            stmt.setString(2, String.valueOf(score));
            stmt.execute();


            query = "SELECT LAST_INSERT_ID();" ;
            stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(!HadResult){
                System.exit(1);
            }
            ResultSet res = stmt.getResultSet();
            res.next();
            TestId = res.getInt(1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Question for the test
        System.out.println("----Enter question for the test----");
        String next = "n";
        int countQuestion = 0;
        while(!next.equals("x")){
            String question;
            String confirm;
            int questScore;
            System.out.println("Enter the Question");
            do{
                question = in.next();

                System.out.println("Press Y to confirm");
                confirm = in.next();
                confirm = confirm.toLowerCase();
            }while(!confirm.equals("y"));

            System.out.println("Enter How many answer you want");
            int numberOfAnswer = 1;
            do{
                System.out.println("question can have minimum 2 answer and maximum 7 answer");
                numberOfAnswer = in.nextInt();
            }while (numberOfAnswer < 2 || numberOfAnswer > 7);

            try{
                String query = "INSERT INTO question(test_id) VALUES(?);";
                PreparedStatement stmt = con.prepareCall(query);

                stmt.setString(1, String.valueOf(TestId));
                stmt.execute();

                query = "SELECT LAST_INSERT_ID();" ;
                stmt = con.prepareCall(query);
                boolean HadResult = stmt.execute();
                if(!HadResult){
                    System.exit(1);
                }
                ResultSet res = stmt.getResultSet();
                res.next();
                QuestId = res.getInt(1);

                for(int i = 0 ; i < numberOfAnswer ; i++){
                    int questionScore;
                    System.out.println("Enter answer");
                    answers.add(in.next());
                    System.out.println("Enter the answer score");
                    do{
                        System.out.println("Enter score between 1-10");
                        questionScore = in.nextInt();
                    }while(questionScore < 1 || questionScore > 10);

                    query = "INSERT INTO answer(score,question_id) VALUES(?,?);";
                    stmt = con.prepareCall(query);
                    stmt.setString(1, String.valueOf(questionScore));
                    stmt.setString(2, String.valueOf(QuestId));
                    stmt.execute();
                }
            }catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            countQuestion++;
            System.out.println("To Enter more question Enter any button to sto Enter x");
            next = in.next();

        }
        try {
            con.close();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }




}
