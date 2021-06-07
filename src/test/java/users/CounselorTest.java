package users;

import Database.ZeroDawnDatabase;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class CounselorTest {

    @BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() throws SQLException {

        Connection con = ZeroDawnDatabase.GetDbCon();
        if(con == null)
        {
            System.exit(1);
        }
        String query = "DELETE FROM users WHERE id = '000000000'; ";
        PreparedStatement stmt = con.prepareCall(query);
        stmt.execute();

        con.close();
    }

    @org.junit.jupiter.api.Test
    void signUp() {
        Counselor counselor = new Counselor();
        counselor.SignUp("000000000", "123456", "A", "B", Date.valueOf("2013-12-05"), "abcd@gmail.com" );
        Counselor GetFromDb = new Counselor();
        GetFromDb = (Counselor) User.Login("000000000", "123456");
        Assert.assertEquals(counselor, GetFromDb);

    }




}