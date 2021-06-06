import Database.ZeroDawnDatabase;
import users.Counselor;
import users.Parent;
import users.Student;
import users.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainPage {

    public static void main(String[] args) {

        Scanner scanM = new Scanner(System.in);
        User user;

        while(true) {

            String newLine = System.getProperty("line.separator");
            System.out.println("------------Welcome To Zero Dawn------------");
            System.out.println("1.Sign Up");
            System.out.println("2.Login");
            System.out.println("3.Exit");

            int mSelect = scanM.nextInt();
            while (mSelect != 1 && mSelect != 2 && mSelect != 3)
            {
                System.out.print("Wrong Input, try again: ");
                mSelect = scanM.nextInt();
            }
            System.out.print(newLine);
            if (mSelect == 1) {
                user = sign_up();
                if(user != null) {
                    if (user.getClass().equals(Student.class))
                        StudentMenu((Student) user);
                    if (user.getClass().equals(Counselor.class))
                        CounselorMenu((Counselor) user);
                    if (user.getClass().equals(Parent.class))
                        ParentMenu((Parent) user);
                }
            }
            if (mSelect == 2) {
                user = login();
                if(user != null) {
                    if (user.getClass().equals(Student.class))
                        StudentMenu((Student) user);
                    if (user.getClass().equals(Counselor.class))
                        CounselorMenu((Counselor) user);
                    if (user.getClass().equals(Parent.class))
                        ParentMenu((Parent) user);
                }
            }
            if (mSelect == 3) {
                System.exit(0);
            }
        }
    }

    public static User sign_up(){

        String ID;
        String pass;
        String fName;
        String lName;
        Date BirthDate;
        String email;
        int cls = 0;
        char type;

        Scanner scanObj = new Scanner(System.in);

        //Id input
        while (true) {
            System.out.print("Enter user id: ");
            ID = scanObj.nextLine();
            String regex = "[0-9]+";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(ID);
            if (ID.length() == 9 && m.matches()) {
                break;
            }
            //add check if id is already in the system
            else {
                System.out.println("you didn't insert an ID, please try again..");
            }
        }

        //Password input
        while (true) {
            System.out.print("Enter user password: ");
            pass = scanObj.nextLine();
            if (!pass.isEmpty()) {
                break;
            }
            else {
                System.out.println("you didn't insert a password, please try again..");
            }
        }

        //First name input
        while (true) {
            System.out.print("Enter user first name: ");
            fName = scanObj.nextLine();
            if (!fName.isEmpty()) {
                break;
            }
            else {
                System.out.println("you didn't insert your first name, please try again..");
            }
        }

        //Last name input
        while (true) {
            System.out.print("Enter user last name: ");
            lName = scanObj.nextLine();
            if (!lName.isEmpty()) {
                break;
            }
            else {
                System.out.println("you didn't insert your last name, please try again..");
            }
        }

        //Date input
        while (true) {
            System.out.print("Enter user birth date (example: 1999-02-23 ): ");
            String bDate = scanObj.nextLine();
            try {
                BirthDate = Date.valueOf(bDate);
            }
            catch (Exception exception){
                BirthDate = null;
            }
            if(BirthDate != null)
                break;
            else {
                System.out.println("Invalid date format");
            }
        }

        //Email input
        while (true) {
            String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
            Pattern pattern = Pattern.compile(regex);
            System.out.print("Enter user email: ");
            email = scanObj.nextLine();
            Matcher match = pattern.matcher(email);
            if (!email.isEmpty() && match.matches()) {
                break;
            }
            else {
                System.out.println("you didn't insert an email or the email is Invalid, please try again..");
            }
        }

        //User type input
        while (true) {
            String newLine = System.getProperty("line.separator");
            System.out.print("Enter 1 for student" + newLine + "Enter 2 for parent" + newLine + "Enter 3 for counselor" + newLine + "Enter Type: ");
            type = scanObj.next().charAt(0);
            while (type != '1' && type != '2' && type != '3') {
                System.out.println("Wrong input, try again..");
                System.out.print("Enter 1 for student" + newLine + "Enter 2 for parent" + newLine + "Enter 3 for counselor" + newLine + "Enter Type: ");
                type = scanObj.next().charAt(0);
            }
            if (type == '1') {
                Scanner classScan = new Scanner(System.in);
                System.out.print("Please enter your class: ");
                cls = classScan.nextInt();
                while (cls < 1 || cls > 12) {
                    System.out.println("Wrong input, try again");
                    System.out.print("Please enter your class: ");
                    cls = classScan.nextInt();
                }
            }
            if (type == '3') {
                Scanner pScan = new Scanner(System.in);
                System.out.print("Please enter password for counselor creation: ");
                String tPass = pScan.nextLine();
                while (!tPass.equals("1111")) {
                    System.out.println("Wrong password, try again..");
                    System.out.println("To cancel the action and exit the system enter 1 in the password");
                    System.out.print("Please enter password for counselor creation: ");
                    tPass = pScan.nextLine();
                    if(tPass.equals("1")) {
                        System.exit(0);
                    }
                }
            }
            if (type != ' ') {
                break;
            }
            else {
                System.out.println("you didn't insert your type, please try again..");
            }
        }
        switch (type){
            case '1':
                Student student = new Student();
                student.SignUp(ID, pass, fName, lName, BirthDate, email, cls);
                return student;
            case '2':
                Parent parent = new Parent();
                parent.SignUp(ID, pass, fName, lName, BirthDate, email);
                return parent;
            case '3':
                Counselor counselor = new Counselor();
                counselor.SignUp(ID, pass, fName, lName, BirthDate, email);
                return counselor;
        }
        return null;
    }

    public static User login() {

        Scanner scan = new Scanner(System.in);
        String ID;
        String pass;

        //Id input
        while (true) {
            System.out.print("Enter user id: ");
            ID = scan.nextLine();
            String regex = "[0-9]+";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(ID);
            if (ID.length() == 9 && m.matches()) {
                break;
            }
            else {
                System.out.println("you didn't insert an ID, please try again..");
            }
        }

        //Password input
        while (true) {
            System.out.print("Enter user password: ");
            pass = scan.nextLine();
            if (!pass.isEmpty()) {
                break;
            }
            else {
                System.out.println("you didn't insert a password, please try again..");
            }
        }

        return User.Login(ID,pass);

    }

    public static void StudentMenu(Student student){
        Scanner scanM = new Scanner(System.in);
        String Opt;
        while(true) {
            System.out.println("\n1.Start test");
            System.out.println("2.Edit profile");
            System.out.println("3.Add feedback");
            System.out.println("4.Report distressed friend");
            System.out.println("5.Quizzes I have already taken");
            System.out.println("6.Exit");
            Opt = scanM.next();
            switch (Opt) {
                case "1":
                    student.start_test2();
                    break;
                case "2":
                    Edit(student);
                    break;
                case "3":
                    student.Add_feedback();
                    break;
                case "4":
                    student.Warn_about_friend_in_distress();
                    break;
                case "5":
                    student.Test_i_made();
                    break;
                case "6":
                    student = null;
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public static void CounselorMenu(Counselor counselor) {
        Scanner scanM = new Scanner(System.in);
        String Opt,Opt1;

        while(true) {
            System.out.println("\n1.Add new test");
            System.out.println("2.Edit test");
            System.out.println("3.Remove/Inactive test");
            System.out.println("4.Present Potential marked students");
            System.out.println("5.Manage users");
            System.out.println("6.Show Students list");
            System.out.println("7.Edit admin information");
            System.out.println("8.Show Feedback on Quiz");
            System.out.println("9.Show alerted kids");
            System.out.println("10.Send message to parent");
            System.out.println("11.Show messages from parents");
            System.out.println("12.Add review on Quiz");
            System.out.println("13.exit");
            Opt = scanM.next();
            switch (Opt) {
                case "1":
                    counselor.AddNewTest();
                    break;
                case "2":
                    counselor.EditTest();
                    break;
                case "3":
                    counselor.RemoveOrHaltQuiz();
                    break;
                case "4":
                    //marked student function
                    break;
                case "5":
                    System.out.println("enter your selection:");
                    System.out.println("to edit student grade press 1");
                    System.out.println("to delete student press 2");
                    Opt1=scanM.next();
                    if(Opt1.equals("1")){
                        counselor.Update_Student_Grade();
                        break;
                    }
                    if(Opt1.equals("2"))
                    {
                        counselor.Delete_Student();
                        break;
                    }
                    break;
                case "6":
                    counselor.Show_Student_list();
                    break;
                case "7":
                    Edit(counselor);
                    break;
                case "8":
                    counselor.Show_Feedback();
                    break;
                case "9":
                    counselor.Alerted_Kids();
                    break;
                case "10":
                    counselor.MessageToParent();
                    break;
                case "11":
                    counselor.ShowMessagesFromParents();
                    break;
                case "12":
                    counselor.Add_Review();
                    break;
                case "13":
                    counselor = null;
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public static int GetKidGrade(String kidId) {
        int check = 0;
        Connection con = ZeroDawnDatabase.GetDbCon();
        if(con == null)
        {
            System.exit(1);
        }
        try {
            String query = "SELECT grade FROM student WHERE user_id = " + kidId;
            PreparedStatement stmt = con.prepareCall(query);
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                check = res.getInt("grade");
            }
            res.close();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return check;
    }

    public static ArrayList<Integer> GetTestsByGrade(int testClass) {
        ArrayList<Integer> tests;
        tests = new ArrayList<Integer>();
        tests.clear();
        Connection con = ZeroDawnDatabase.GetDbCon();
        if(con == null)
        {
            System.exit(1);
        }
        try {
            String query = "SELECT test_id FROM test WHERE grade = " + testClass + " AND is_active = 1 order by test_id desc limit 3";
            PreparedStatement stmt = con.prepareCall(query);
            ResultSet res = stmt.executeQuery(query);
            while (res.next()) {
                tests.add(res.getInt("test_id"));
            }
            res.close();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return tests;
    }

    public static ArrayList<Integer> GetKidTests(String kidId) {
        ArrayList<Integer> kidTests;
        kidTests = new ArrayList<Integer>();
        kidTests.clear();
        Connection con = ZeroDawnDatabase.GetDbCon();
        if(con == null)
        {
            System.exit(1);
        }
        ResultSet kid_res = null;
        try {
            String kid_query = "SELECT test_id FROM start_test WHERE user_id = " + kidId;
            PreparedStatement kid_stmt = con.prepareCall(kid_query);
            kid_res = kid_stmt.executeQuery(kid_query);
            while (kid_res.next()) {
                kidTests.add(kid_res.getInt("test_id"));
            }
            kid_res.close();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return kidTests;
    }

    public static boolean QuizKidsClass(String kidId) {
        int kidGrade = GetKidGrade(kidId);
        ArrayList<Integer> tests;
        tests = new ArrayList<Integer>();
        tests.clear();
        tests = GetTestsByGrade(kidGrade);
        ArrayList<Integer> kidTests;
        kidTests = new ArrayList<Integer>();
        kidTests.clear();
        kidTests = GetKidTests(kidId);
        for (int i = 0; i < tests.size(); i++) {
            for (int j = 0; j < kidTests.size(); j++) {
                if (tests.get(i) == kidTests.get(j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String NameFromId(String kidId) {
        Connection con = ZeroDawnDatabase.GetDbCon();
        if(con == null)
        {
            System.exit(1);
        }
        ResultSet kid_res = null;
        String name = null;
        try {
            String kid_query = "SELECT first_name FROM users WHERE id = " + kidId;
            PreparedStatement kid_stmt = con.prepareCall(kid_query);
            kid_res = kid_stmt.executeQuery(kid_query);
            while (kid_res.next()) {
                name = kid_res.getString("first_name");
            }
            kid_res.close();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return name;
    }

    public static void ThreeQuizAlert(Parent parent) {
        parent.AddKidsToArray();
        if (parent.GetKidsArraySize() != 0) {
            for (int i = 0; i < parent.GetKidsArraySize(); i++) {
                if (QuizKidsClass(parent.GetKidsArray().get(i).toString())) {
                    String kidName = NameFromId(parent.GetKidsArray().get(i).toString());
                    System.out.println("Your kid " + kidName + " didn't do any of the last 3 tests he got");
                }
            }
        }
    }

    public static void ParentMenu(Parent parent) {
        String newLine = System.getProperty("line.separator");
        Scanner scanM = new Scanner(System.in);
        String Opt;
        while(true) {
            ThreeQuizAlert(parent);
            System.out.println("\n1.Edit profile");
            System.out.println("2.Add child");
            System.out.println("3.Remove child");
            System.out.println("4.Send message to counselor");
            System.out.println("5.Show message from counselor");
            System.out.println("6.Simulation Quiz");
            System.out.println("7.Exit");
            Opt = scanM.next();
            System.out.print(newLine);
            switch (Opt) {
                case "1":
                    Edit(parent);
                    break;
                case "2":
                    parent.AddChild();
                    break;
                case "3":
                    parent.RemoveChild();
                    break;
                case "4":
                    parent.MessageToCounselor();
                    break;
                case "5":
                    parent.MessageFromCounselor();
                    break;
                case "6":
                    parent.Simulation_test();
                    break;
                case "7":
                    parent = null;
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public static void Edit(User user){
        Scanner scanM = new Scanner(System.in);
        int Opt;
        while (true) {
            System.out.println("\n1.Edit password");
            System.out.println("2.Edit first name");
            System.out.println("3.Edit last name");
            System.out.println("4.Edit email");
            System.out.println("5.Back to main menu");

            Opt = scanM.nextInt();

            switch (Opt) {
                case 1:
                    scanM = new Scanner(System.in);
                    String NewPassword;

                    while (true) {
                        System.out.print("Enter new password: ");
                        NewPassword = scanM.nextLine();
                        if (!NewPassword.isEmpty()) {
                            break;
                        }
                        else {
                            System.out.println("you didn't insert a new password, please try again..");
                        }
                    }
                    user.EditPassword(NewPassword);
                    break;
                case 2:
                    scanM = new Scanner(System.in);
                    String NewFirstName;

                    while (true) {
                        System.out.print("Enter user new first name: ");
                        NewFirstName = scanM.nextLine();
                        if (!NewFirstName.isEmpty()) {
                            break;
                        }
                        else {
                            System.out.println("you didn't insert your new first name, please try again..");
                        }
                    }
                    user.EditFristName(NewFirstName);
                    break;
                case 3:
                    scanM = new Scanner(System.in);
                    String NewLastName;

                    while (true) {
                        System.out.print("Enter user new last name: ");
                        NewLastName = scanM.nextLine();
                        if (!NewLastName.isEmpty()) {
                            break;
                        }
                        else {
                            System.out.println("you didn't insert your new last name, please try again..");
                        }
                    }
                    user.EditLastName(NewLastName);
                    break;
                case 4:
                    scanM = new Scanner(System.in);
                    String NewMail;

                    while (true) {
                        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
                        Pattern pattern = Pattern.compile(regex);
                        System.out.print("Enter user email: ");
                        NewMail = scanM.nextLine();
                        Matcher match = pattern.matcher(NewMail);
                        if (!NewMail.isEmpty() && match.matches()) {
                            break;
                        }
                        else {
                            System.out.println("you didn't insert an email or the email is Invalid, please try again..");
                        }
                    }
                    user.EditEmail(NewMail);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
}
