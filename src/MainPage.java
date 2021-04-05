import Database.ZeroDawnDatabase;
import java.sql.*;
import java.util.Scanner;

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
            //צריך לפנות פה לתפריט של המחלקה הספציפית שהבן אדם נרשם
            System.out.println("sign-up select is: " + mSelect);
        }
        if(mSelect == 2)
        {
            login(con);
            //צריך לפנות פה לתפריט של המחלקה הספציפית שהבן אדם התחבר
            System.out.println("login select is: " + mSelect);
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
                PreparedStatement stmt = con.prepareStatement("Insert into users values (?,?,?,?,?,?)");
                System.out.print("Enter user id: ");
                String ID = scanObj.nextLine();
                stmt.setString(1, ID);

                System.out.print("Enter user first name: ");
                String fName = scanObj.nextLine();
                stmt.setString(2, fName);

                System.out.print("Enter user last name: ");
                String lName = scanObj.nextLine();
                stmt.setString(3, lName);

                System.out.print("Enter user birth date (example: 1999-02-23 ): ");
                String bDate = scanObj.nextLine();

                stmt.setString(4, bDate);

                System.out.print("Enter user password: ");
                String pass = scanObj.nextLine();
                stmt.setString(5, pass);

                System.out.print("Enter user type: ");
                String type = scanObj.nextLine();
                stmt.setString(6, type);

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
