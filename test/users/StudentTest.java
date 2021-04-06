package users;

import Database.ZeroDawnDatabase;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class StudentTest {

    @BeforeEach
    void setUp() {

    }

    @AfterEach
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

    @Test
    void signUp() {
        Student student = new Student();
        student.SignUp("000000000", "123456", "A", "B", Date.valueOf("2013-12-05"), "abcd@gmail.com" );
        Student GetFromDb = new Student();
        GetFromDb = (Student) User.Login("000000000", "123456");
        Assert.assertEquals(student, GetFromDb);
    }
}