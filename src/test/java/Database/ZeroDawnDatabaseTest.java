package Database;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class ZeroDawnDatabaseTest {

    Connection con;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getDbCon() throws SQLException {

        Connection con = ZeroDawnDatabase.GetDbCon();
        Assert.assertNotNull(con);
        con.close();
    }
}