package CC_Server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import CC_Directory.WebReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;
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

    /***
     * This test passes as long as an exception is not thrown.
     * @throws Exception An exception here is likely due to a database connection problem.
     */
    @Test
    public void testWebReader() throws Exception {
        ServerWebReader serverWebReader = new ServerWebReader();
        HashSet<ServerCurrency> currencyList = serverWebReader.getDBPage("https://www.x-rates.com/historical/?from=USD&amount=1&date=2021-06-16");
        if(!Connect.checkEntries(serverWebReader.getDate())) // If entries with this date already exist, then cancel the insertion.
            Connect.insertList(currencyList);
    }

    /***
     * Verifies that the identical(seemingly) objects are not being stored in the hashset.
     */
    @Test
    public void testServerCurrencyHashSet() {
        ServerCurrency sc1 = new ServerCurrency("USD", "10", "2000-4-21");
        ServerCurrency sc2 = new ServerCurrency("USD", "22", "2000-4-21");
        ServerCurrency sc3 = new ServerCurrency("USD", "2", "2000-4-22");
        HashSet<ServerCurrency> hs = new HashSet();
        hs.add(sc1);
        hs.add(sc2);
        hs.add(sc3);
        Assertions.assertEquals(2, hs.size());
    }

    /***
     *
     */
    @Test
    public void testCheckEntries() throws Exception {
        Assertions.assertFalse(Connect.checkEntries("1000-4-21"));
        Assertions.assertTrue(Connect.checkEntries("2021-06-16"));
    }
}
