import Database.ZeroDawnDatabase;
import java.sql.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.*;
import java.util.*;

public class MainPage {

    public static void main(String[] args) throws SQLException {

        ZeroDawnDatabase db = new ZeroDawnDatabase();
        Connection con = db.GetDb();
        Scanner scanM = new Scanner(System.in);
        String newLine = System.getProperty("line.separator");
        System.out.printf("Welcome to zero dawn" + newLine + "for sign-up enter 1" + newLine + "for login enter 2" + newLine + "to exit the system press 3" + newLine);
        int mSelect = scanM.nextInt();
        while (mSelect != 1 && mSelect != 2 && mSelect != 3)
        {
            System.out.print("Wrong Input, try again: ");
            mSelect = scanM.nextInt();
        }
        if(mSelect == 1)
        {
            sign_up(con);
            //we need to change the sign-up function from void to return
        }
        if(mSelect == 2)
        {
            login(con);
            //we need to change the login function from void to return
        }
        if(mSelect == 3)
        {
            System.exit(0);
        }
    }

    public static void sign_up(Connection con) throws SQLException {

        while (true) {
            try {
                Scanner scanObj = new Scanner(System.in);
                PreparedStatement stmt = con.prepareStatement("Insert into users values (?,?,?,?,?,?,?)");

                while (true) {
                    System.out.print("Enter user id: ");
                    String ID = scanObj.nextLine();
                    if (!ID.isEmpty()) {
                        stmt.setString(1, ID);
                        break;
                    }
                    else {
                        System.out.println("you didn't insert an ID, please try again..");
                    }
                }

                while (true) {
                    System.out.print("Enter user password: ");
                    String pass = scanObj.nextLine();
                    if (!pass.isEmpty()) {
                        stmt.setString(2, pass);
                        break;
                    }
                    else {
                        System.out.println("you didn't insert a password, please try again..");
                    }
                }

                while (true) {
                    System.out.print("Enter user first name: ");
                    String fName = scanObj.nextLine();
                    if (!fName.isEmpty()) {
                        stmt.setString(3, fName);
                        break;
                    }
                    else {
                        System.out.println("you didn't insert your first name, please try again..");
                    }
                }

                while (true) {
                    System.out.print("Enter user last name: ");
                    String lName = scanObj.nextLine();
                    if (!lName.isEmpty()) {
                        stmt.setString(4, lName);
                        break;
                    }
                    else {
                        System.out.println("you didn't insert your last name, please try again..");
                    }
                }


                /* Date working without testing of values
                System.out.print("Enter user birth date (example: 1999-02-23 ): ");
                String bDate = scanObj.nextLine();
                stmt.setString(5, bDate);
                */


                /* Date test 1 not working, smart way
                while (true) {
                    //Scanner dscan = new Scanner(System.in);
                    Date bt = new Date();
                    DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        System.out.print("Enter user birth date (example: 1999-02-23 ): ");
                        String bDate = scanObj.nextLine();
                        bDate = dt.format(bt);
                        stmt.setString(5, bDate);
                        break;
                    } catch (Exception dex) {
                        System.out.println("Date is Invalid" + dex.getMessage());
                    }
                }
                */


                /* Date test 2 not working, not smart way
                while (true) {
                    System.out.print("Enter user birth year (yyyy): ");
                    String yDate = scanObj.nextLine();
                    int check = Integer.valueOf(yDate);
                    if(!yDate.isEmpty() && check > 1900 && check < 2015)
                    {
                        while ()
                    }
                    else {
                        System.out.println("you didn't insert a year or the year is Invalid, please try again..");
                    }
                }
                */




                while (true) {
                    String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
                    Pattern pattern = Pattern.compile(regex);
                    System.out.print("Enter user email: ");
                    String email = scanObj.nextLine();
                    Matcher match = pattern.matcher(email);
                    if (!email.isEmpty() && match.matches()) {
                        stmt.setString(6, email);
                        break;
                    }
                    else {
                        System.out.println("you didn't insert an email or the email is Invalid, please try again..");
                    }
                }

                while (true) {
                    String newLine = System.getProperty("line.separator");
                    System.out.printf("Enter 1 for student" + newLine + "Enter 2 for parent" + newLine + "Enter 3 for counselor" + newLine + "Enter Type: ");
                    char type = scanObj.next().charAt(0);
                    while (type != '1' && type != '2' && type != '3') {
                        System.out.println("Wrong input, try again..");
                        System.out.printf("Enter 1 for student" + newLine + "Enter 2 for parent" + newLine + "Enter 3 for counselor" + newLine + "Enter Type: ");
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
                        stmt.setString(7, String.valueOf(type));
                        break;
                    }
                    else {
                        System.out.println("you didn't insert your type, please try again..");
                    }
                }

                stmt.executeUpdate();
                break;
            } catch (SQLIntegrityConstraintViolationException exc) {
                System.out.println("the ID already exist: " + exc.getMessage());
                System.out.println("Try again..");
                continue;
            }
        }
        System.out.println("Welcome to the system");
        con.close();
    }

    public static void login(Connection con) throws SQLException {

        Scanner scanID = new Scanner(System.in);
        Scanner scanPass = new Scanner(System.in);
        PreparedStatement stmt = con.prepareStatement("Insert into users values (?,?,?,?,?,?)");
        
        while (true) {
            try {
                System.out.print("Enter your id: ");
                String ID = scanID.nextLine();
                System.out.print("Enter your password: ");
                String pass = scanPass.nextLine();
                String sql = "Select * from users Where ID='" + ID + "' and password='" + pass + "'";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    System.out.println("ID and password are correct");
                    break;
                } else {
                    System.out.println("invalid ID and password");
                }
            } catch (SQLException exc) {
                System.out.println("Error " + exc.getMessage());
                continue;
            }
        }
    }
}
