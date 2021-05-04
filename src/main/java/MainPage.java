import Database.ZeroDawnDatabase;
import users.Counselor;
import users.Parent;
import users.Student;
import users.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            if (type == '3') {
                Scanner pScan = new Scanner(System.in);
                System.out.println("Please enter password for counselor creation: ");
                String tPass = pScan.nextLine();
                while (!tPass.equals("1111")) {
                    System.out.println("Wrong password, try again..");
                    System.out.println("To cancel the action and exit the system enter 1 in the password");
                    System.out.println("Please enter password for counselor creation: ");
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
                student.SignUp(ID,pass, fName, lName, BirthDate, email,1);
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

    public static void StudentMenu(Student student) {
        Scanner scanM = new Scanner(System.in);
        String Opt;
        while(true) {
            System.out.println("1.Edit profile");
            System.out.println("2.Exit");
            Opt = scanM.next();


            switch (Opt) {
                case "1":
                    Edit(student);
                    break;
                case "2":
                    student = null;
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public static void CounselorMenu(Counselor counselor) {
        Scanner scanM = new Scanner(System.in);
        String Opt;

        while(true) {
            System.out.println("1.Add new test");
            System.out.println("2.Edit test");
            System.out.println("3.Remove/Inactive test");
            System.out.println("4.Present Potential marked students");
            System.out.println("5.Manage users");
            System.out.println("6.Run simulate");
            System.out.println("7.Edit admin information");
            System.out.println("8.exit");
            Opt = scanM.next();
            switch (Opt) {
                case "1":
                    counselor.AddNewTest();
                    break;
                case "2":
                    //Edit test function
                    break;
                case "3":
                    counselor.RemoveOrHaltQuiz();
                    break;
                case "4":
                    //marked student function
                    break;
                case "5":
                    //manage users function
                    break;
                case "6":
                    //simulate function
                    break;
                case "7":
                    Edit(counselor);
                    break;
                case "8":
                    counselor = null;
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }

    }

    public static void ParentMenu(Parent parent) {
        Scanner scanM = new Scanner(System.in);
        String Opt;
        System.out.println("1.Edit profile");
        System.out.println("2.Add child");
        System.out.println("3.Remove child");
        System.out.println("4.Exit");
        Opt = scanM.next();

        while(true) {
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
            System.out.println("1.Edit password");
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
