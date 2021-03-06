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
    Scanner in = new Scanner(System.in);

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
        int i=0,k=0,flag3=0;
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
                if(Quiz_with_feedback.size()==0){
                    System.out.println("there is no feedback to show");
                    flag3=1;
                }
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(flag3==0) {
            Set<Integer> s = new LinkedHashSet<>(Quiz_with_feedback);
            System.out.println("----List Of Tests----");
            for (Integer integer : s) {
                System.out.println(integer);
            }
            System.out.println("Please select test number to see the feedback: ");
            int CH = in.nextInt();
            int flag = 0;
            while (i < 1) {
                for (Integer integer : s) {
                    if (integer == CH) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 1) {
                    break;
                }
                System.out.println("Please select test number from this list: ");
                System.out.println("----List Of Tests----");
                for (Integer integ : s) {
                    System.out.println(integ);
                }
                CH = in.nextInt();
            }
            try {
                String query = "select * from feedback where test_id=" + CH + ";";
                PreparedStatement stmt = con.prepareCall(query);
                boolean HadResult = stmt.execute();
                if (HadResult) {
                    ResultSet res = stmt.getResultSet();
                    while (res.next()) {
                        strin[i] = (res.getString(2));
                        i++;
                    }
                    res.close();
                    System.out.println("The Feedbacks: ");
                    while (k < i) {
                        int t = k + 1;
                        System.out.println(t + "." + strin[k]);
                        k++;
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    public void Alerted_Kids()
    {
        ArrayList<String> msg_reports = new ArrayList<String>();
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "select msg,student_name,student_Lname,grade from report;";
            PreparedStatement stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(HadResult) {
                ResultSet res = stmt.getResultSet();
                while (res.next()) {
                    msg_reports.add(res.getString(1));
                    msg_reports.add(res.getString(2));
                    msg_reports.add(res.getString(3));
                    msg_reports.add(res.getString(4));
                }
                if (msg_reports.size() == 0) {
                    System.out.println("there is no Students to show");
                }
                res.close();
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        int k=1;
        for (int j=0;j<msg_reports.size();j+=4) {
            System.out.println(k+".");
            System.out.println("name: "+msg_reports.get(j+1) + " " + msg_reports.get(j+2));
            System.out.println("grade: "+msg_reports.get(j+3));
            System.out.println("Alert: " +msg_reports.get(j));
            k++;
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

    public void Show_Student_list() {
        Scanner in = new Scanner(System.in);
        ArrayList<String> Student_list = new ArrayList<String>();
        Connection con = ZeroDawnDatabase.GetDbCon();
        int i = 0;
        if (con == null) {
            System.exit(1);
        }
        try {
            String query = "select user_id from student;";
            PreparedStatement stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if (HadResult) {
                ResultSet res = stmt.getResultSet();
                while (res.next()) {
                    Student_list.add(res.getString(1));
                    i++;
                }
                res.close();

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Set<String> s = new LinkedHashSet<>(Student_list);
        System.out.println("----ID List Of Students----");
        for (String string : s) {
            System.out.println(string);
        }
        if (Student_list.size() == 0) {
            System.out.println("there is no student to show");
        }
    }

    public void AddAllKidsToArray() {
        All_Kids.clear();
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        try {
            String query = "SELECT distinct student_id FROM kids";
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
        ArrayList<String> msg_reports = new ArrayList<String>();
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "select parent_id,student_ID,msg from message;";
            PreparedStatement stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(HadResult){
                ResultSet res = stmt.getResultSet();
                while(res.next()){
                    msg_reports.add(res.getString(1));
                    msg_reports.add(res.getString(2));
                    msg_reports.add(res.getString(3));
                }
                if (msg_reports.size() == 0) {
                    System.out.println("there is no messages");
                }
                else {
                    int i, j = 1;
                    for (i = 0; i < msg_reports.size(); i += 3) {
                        System.out.println("Message number " + j);
                        j++;
                        System.out.println("Parent ID: " + msg_reports.get(i) + ",  Student ID: " + msg_reports.get(i + 1));
                        System.out.println("The Message: " + msg_reports.get(i + 2));
                    }
                }
                res.close();
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void Add_Review(){
        Tests_And_Students();
        Scanner in = new Scanner(System.in);
        ArrayList<Integer> Quiz_Student_Done = new ArrayList<Integer>();
        Connection con = ZeroDawnDatabase.GetDbCon();
        int i=0,flag1=0;
        if (con == null) {
            System.exit(1);
        }
        System.out.println("Enter student ID to add a review: ");
        String S_ID=in.nextLine();
        try{
            String query = "select test_id from start_test where user_id =" + S_ID + ";";
            PreparedStatement stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(HadResult){
                ResultSet res = stmt.getResultSet();
                while(res.next()){
                    Quiz_Student_Done.add(res.getInt(1));
                    i++;
                }
                res.close();
                if (Quiz_Student_Done.size() == 0) {
                    System.out.println("No student with this ID has taken a test");
                    flag1=1;
                }
            }
        }catch (SQLException throwables) {
            System.out.println("Student not found");
            throwables.printStackTrace();
        }
        if(flag1==0) {
            Set<Integer> s = new LinkedHashSet<>(Quiz_Student_Done);
            System.out.println("----List Of Tests----");
            for (Integer integer : s) {
                System.out.println(integer);
            }
            System.out.println("Please select test number: ");
            int CH = in.nextInt();
            int flag = 0;
            while (i < 100) {
                for (Integer integer : s) {
                    if (integer == CH) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 1) {
                    break;
                }
                System.out.println("Please select test number from this list: ");
                System.out.println("----List Of Tests----");
                for (Integer integ : s) {
                    System.out.println(integ);
                }
                CH = in.nextInt();
            }
            System.out.println("\n\nThe Test: ");
            Show_Student_Ans(S_ID);
            System.out.println();
            System.out.println("Enter a review for school use:");
            in.nextLine();
            String FD = in.nextLine();
            System.out.println("Enter a review for Parents:");
            String FD1 = in.nextLine();
            System.out.println("do you want to mark this student?");
            System.out.println("press 1 for yes");
            System.out.println("press 0 for no");
            int p = in.nextInt();
            while (p != 1 && p != 0) {
                System.out.println("You entered an incorrect number, please select again:");
                System.out.println("do you want to mark this student?");
                System.out.println("press 1 for yes");
                System.out.println("press 0 for no");
                p = in.nextInt();
            }
            try {
                String query = "INSERT IGNORE INTO review(test_id,user_id ,review, review_for_parents) VALUES(?,?,?,?)";
                PreparedStatement stmt = con.prepareCall(query);
                stmt.setInt(1, CH);
                stmt.setString(2, S_ID);
                stmt.setString(3, FD);
                stmt.setString(4, FD1);
                stmt.execute();

                System.out.println("Review was successfully entered");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                String query2 = "INSERT IGNORE INTO student_monitoring(user_id, marked, care, test_id) VALUES(?,?,?,?);";
                PreparedStatement stmt1 = con.prepareCall(query2);
                stmt1.setString(1, S_ID);
                stmt1.setInt(2, p);
                stmt1.setString(3, FD);
                stmt1.setInt(4, CH);
                stmt1.execute();
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    public void Show_Student_Ans(String S){
        ArrayList<String> msg_reports = new ArrayList<String>();
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "select question_id,ans from start_test where user_id= "+S+";";
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
        int k=1,t=0;
        for (int j=0;j<msg_reports.size();j+=2) {
            System.out.println(k+".");
            System.out.println("Question: "+msg_reports.get(j));
            System.out.println("Answer: "+msg_reports.get(j+1));
            t+=Integer.parseInt(msg_reports.get(j+1));
            k++;
        }
        System.out.println("\n\nTotal score: "+t);

    }

    public void Tests_And_Students(){
        System.out.println("List of Tests made by Students: ");
        ArrayList<String> msg_reports = new ArrayList<String>();
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "SELECT DISTINCT test_id,user_id from start_test;";
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
        int k=1,t=0;
        for (int j=0;j<msg_reports.size();j+=2) {
            System.out.println(k+".");
            System.out.println("Test Number: "+msg_reports.get(j));
            System.out.println("Student ID: "+msg_reports.get(j+1));
            t+=Integer.parseInt(msg_reports.get(j+1));
            k++;
        }
    }

    public void Monitoring_Students(){
        int flag=1;
        System.out.println("List of Marked student:");
        ArrayList<String> msg_reports = new ArrayList<String>();
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        try{
            String query = "select User_id,care from student_monitoring where marked=1;";
            PreparedStatement stmt = con.prepareCall(query);
            boolean HadResult = stmt.execute();
            if(HadResult){
                ResultSet res = stmt.getResultSet();
                while(res.next()){
                    msg_reports.add(res.getString(1));
                    msg_reports.add(res.getString(2));
                }
                res.close();
                if (msg_reports.size() == 0) {
                    System.out.println("there is no marked students");
                    flag=0;
                }
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        int k=1;
        for (int j=0;j<msg_reports.size();j+=2) {
            System.out.println(k+".");
            System.out.println("Student ID: "+msg_reports.get(j));
            System.out.println("Action required: "+msg_reports.get(j+1));
            k++;
        }

        if(flag==0)
        {
            System.out.println("To change the student marking press 1");
            System.out.println("To Exit Press 2");
            int CH1=in.nextInt();

            while (CH1!=2 && CH1!=1){
                System.out.println("Select one of the following options: ");
                System.out.println("To change the student marking press 1");
                System.out.println("To Exit Press 2");
                CH1=in.nextInt();
            }
            if(CH1==1) {
                System.out.println("Please enter the ID number of the student you would like to address: ");
                String CH0 = in.nextLine();
                System.out.println("To add the student to the list of marked students Press 1, " +
                        "To remove the student from the list of marked students Press 0.");
                in.nextLine();
                String CH5=in.nextLine();
                while (!CH5.equals("1") && !CH5.equals("0"))
                {
                    System.out.println("You entered an incorrect number, please select again:");
                    System.out.println("To add the student to the list of marked students Press 1, " +
                            "To remove the student from the list of marked students Press 0.");
                    CH5=in.nextLine();
                }
                try {
                    String query = "update student_monitoring set marked=? where user_id=?;";
                    PreparedStatement stmt = con.prepareCall(query);
                    stmt.setInt(1,Integer.parseInt(CH5));
                    stmt.setString(2,CH0);
                    stmt.execute();
                    con.close();
                    System.out.println("Change made successfully!");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
            System.out.println("Please enter the ID number of the student you would like to address: ");
            String CH0 = in.nextLine();
            System.out.println("Please select one of the following options:\n" +
                    "1. To edit student care press 1\n" +
                    "2. To change the student marking press 2\n" +
                    "3. To update the student parent press 3");
            int CH1 = in.nextInt();

            while (CH1 != 1 && CH1 != 2 && CH1 != 3) {
                System.out.println("Please select one of the following options:\n" +
                        "1. To edit student care press 1\n" +
                        "2. To change the student marking press 2\n" +
                        "3. To update the student parent press 3");
                CH1 = in.nextInt();
            }

        if(CH1==1) {
            try {
                System.out.println("Please enter new review: ");
                in.nextLine();
                String CH2 = in.nextLine();
                String query = "update student_monitoring set care=? where user_id=?;";
                PreparedStatement stmt = con.prepareCall(query);
                stmt.setString(1,CH2);
                stmt.setString(2,CH0);
                stmt.execute();
                con.close();
                System.out.println("successfully updated!");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if(CH1==2) {
            System.out.println("To add the student to the list of marked students Press 1, " +
                    "To remove the student from the list of marked students Press 0.");
            in.nextLine();
            String CH5=in.nextLine();
            while (!CH5.equals("1") && !CH5.equals("0"))
            {
                System.out.println("You entered an incorrect number, please select again:");
                System.out.println("To add the student to the list of marked students Press 1, " +
                        "To remove the student from the list of marked students Press 0.");
                CH5=in.nextLine();
            }
            try {
                String query = "update student_monitoring set marked=? where user_id=?;";
                PreparedStatement stmt = con.prepareCall(query);
                stmt.setInt(1,Integer.parseInt(CH5));
                stmt.setString(2,CH0);
                stmt.execute();
                con.close();
                System.out.println("Change made successfully!");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(CH1==3)
        {
            System.out.println("What would you like to update the parent on:");
            in.nextLine();
            String CH4=in.nextLine();
            try {
                String query = "update student_monitoring set update_parents=? where user_id=?;";
                PreparedStatement stmt = con.prepareCall(query);
                stmt.setString(1,CH4);
                stmt.setString(2,CH0);
                stmt.execute();
                con.close();
                System.out.println("parents update successfully!");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}




