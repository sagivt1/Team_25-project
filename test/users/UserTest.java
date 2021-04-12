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

class UserTest {

    User test;
    Parent parent;

    @BeforeEach
    void setUp() {
        Connection con = ZeroDawnDatabase.GetDbCon();
        if (con == null) {
            System.exit(1);
        }
        try {
            String query = "INSERT INTO users Values(?,?,?,?,?,?)";
            PreparedStatement stmt = con.prepareCall(query);

            stmt.setString(1, "000000000");
            stmt.setString(2, "123456");
            stmt.setString(3, "A");
            stmt.setString(4, "B");
            stmt.setString(5, Date.valueOf("2005-06-12").toString());
            stmt.setString(6, "abc@gmail.com");
            stmt.execute();

            query = "INSERT INTO student Values(?)";
            stmt = con.prepareCall(query);
            stmt.setString(1, "000000000");
            stmt.execute();

            query = "INSERT INTO users Values(?,?,?,?,?,?)";
            stmt = con.prepareCall(query);

            stmt.setString(1, "111111111");
            stmt.setString(2, "123456");
            stmt.setString(3, "A");
            stmt.setString(4, "B");
            stmt.setString(5, Date.valueOf("2005-06-12").toString());
            stmt.setString(6, "abc@gmail.com");
            stmt.execute();

            query = "INSERT INTO parent Values(?)";
            stmt = con.prepareCall(query);
            stmt.setString(1,"111111111");
            stmt.execute();

            query = "INSERT INTO student Values(?)";
            stmt = con.prepareCall(query);
            stmt.setString(1, "111111111");
            stmt.execute();


            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        test = new User("000000000", "123456", "A", "B", Date.valueOf("2005-06-12"), "abc@gmail.com");
        parent = new Parent("111111111", "123456", "A", "B", Date.valueOf("2005-06-12"), "abc@gmail.com");

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

            query = "DELETE FROM users WHERE id = '111111111'; ";
            stmt = con.prepareCall(query);
            stmt.execute();

            con.close();
    }

    @Test
    void login() {

        User user = new User();
        user = User.Login("000000001", "123456");
        Assert.assertNotEquals(user, test);
        user = User.Login("111111111", "123456");
        Assert.assertEquals(user, parent);
    }

    @Test
    void Disconnect() {
        User user = new User();
        user = User.Login("000000000", "123456");
        //if user chose to Disconnect then user = null
        user = null;
        Assert.assertNull(user);
    }

    @Test
    void EditPassword() {

        User user = new User();
        user = User.Login("000000000", "123456");
        user.EditPassword("654321");
        user = User.Login("000000000", "654321");
        test.password = "654321";
        Assert.assertEquals(user,test);
    }

    @Test
    void EditFirstName() {

        User user = new User();
        user = User.Login("000000000", "123456");
        user.EditFristName("S");
        user = User.Login("000000000", "123456");
        test.fname = "S";
        Assert.assertEquals(user,test);
    }

    @Test
    void EditLastName() {

        User user = new User();
        user = User.Login("000000000", "123456");
        user.EditLastName("G");
        user = User.Login("000000000", "123456");
        test.lname = "G";
        Assert.assertEquals(user,test);
    }

    @Test
    void EditEmail() {

        User user = new User();
        user = User.Login("000000000", "123456");
        user.EditEmail("aaa@gmail.com");
        user = User.Login("000000000", "123456");
        test.email = "aaa@gmail.com";
        Assert.assertEquals(user,test);
    }





}