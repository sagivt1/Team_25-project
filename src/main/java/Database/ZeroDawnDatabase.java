package Database;

import java.sql.*;

public class ZeroDawnDatabase {




    /*
    * Creating new connection to the database and return conn for future use
    * of the database
    *
    * before use this class change the password to your localhost password
    *  */
    public static Connection GetDbCon(){
        Connection conn = null;
        try {

            String url = "jdbc:mysql://freedb.tech/freedbtech_zerodawn?useSSL=false";
            String user = "root";
            String password = "!bB7WJAyj%O!";//Edit to your mysql password


            conn = DriverManager.getConnection(url, user, password);

            return conn;

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }

    }





}
