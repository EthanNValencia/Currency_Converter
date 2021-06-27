package CC_Server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class UnitTests {

    /***
     * This verifies that a connection is being made to the server. This is a good troubleshooting test to use when attempting to establish a connection.
     */
    @Test
    public void testConnect(){
        try {
            Connect.createTable();
        } catch (Exception e) {
            fail();
            e.printStackTrace();
        }
    }

}
