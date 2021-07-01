package CC_Server;

import org.junit.jupiter.api.Test;
import CC_Directory.WebReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

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

    @Test
    public void testWebReader(){
        ServerWebReader webReader = new ServerWebReader();
        String content = webReader.getDBPage("https://www.x-rates.com/historical/?from=USD&amount=1&date=2021-06-16");

    }

    // https://www.x-rates.com/historical/?from=USD&amount=1&date=2021-06-16
    // <td class='rtRates'>
    @Test
    public void testingHTTPInteraction(){

    }

}
