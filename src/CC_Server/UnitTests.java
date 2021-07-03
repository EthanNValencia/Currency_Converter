/*
Ethan J. Nephew
July 1, 2021
Unit tests for the server package.
*/

package CC_Server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.List;

import static CC_Server.CONSTANTS.DATE_TODAY;
import static CC_Server.CONSTANTS.WEBSITE_URL;
import static org.junit.jupiter.api.Assertions.*;

public class UnitTests {

    /***
     * This verifies that a connection is being made to the server. This is a good troubleshooting test to use when attempting to establish a connection.
     */
    @Test
    public void testConnection_getConnection(){
        try {
            Connect.getConnection();
        } catch (Exception e) {
            fail();
        }
    }

    /***
     * This verifies that the Connection.createTable() method will not throw an exception.
     */
    @Test
    public void testConnection_createTable(){
        try {
            Connect.dropTable();
            Connect.createTable();
        } catch (Exception e) {
            fail();
        }
    }

    /***
     *
     */
    @Test
    public void testDatabaseBuild(){

        try {
            Connect.createTable();
        } catch (Exception e) {
            fail();
        }

        ServerWebReader serverWebReader = new ServerWebReader();
        HashSet<ServerCurrency> currencyHashSet = serverWebReader.getPage(WEBSITE_URL + DATE_TODAY);

        try {
            Connect.insertCurrencyNames(currencyHashSet);
        } catch (Exception e) {
            System.out.println("You probably tried to insert duplicate currency names.");
            fail();
        }

        try {
            Connect.insertCurrencyDate(DATE_TODAY);
        } catch (Exception e) {
            System.out.println("You probably tried to insert a duplicate currency date.");
            fail();
        }

        try {
            Connect.insertList(currencyHashSet);
        } catch (Exception e) {
            System.out.println("You probably tried to insert duplicate currency fields.");
            fail();
        }


    }

    /***
     * This test passes as long as an exception is not thrown. This verifies that if existing data is in the db that the insertion will not be executed.
     * @throws Exception An exception here is likely due to a database connection problem.
     */
    @Test
    public void testWebReader_getDBPage_AND_Connect_insertList() throws Exception {
        ServerWebReader serverWebReader = new ServerWebReader();
        HashSet<ServerCurrency> currencyHashSet = serverWebReader.getPage(WEBSITE_URL + DATE_TODAY);
        if(!Connect.checkEntries(serverWebReader.getDate())) // If entries with this date already exist, then cancel the insertion.
            Connect.insertList(currencyHashSet);
    }

    /***
     * Verifies that the identical(seemingly) objects are not being stored in the hashset.
     */
    @Test
    public void testServerCurrency_hashCode_AND_ServerCurrency_equals() {
        ServerCurrency sc1 = new ServerCurrency("USD", "10", "2000-4-21", "USA");
        ServerCurrency sc2 = new ServerCurrency("USD", "22", "2000-4-21", "USA");
        ServerCurrency sc3 = new ServerCurrency("USD", "2", "2000-4-22", "USA");
        HashSet<ServerCurrency> hs = new HashSet<>();
        hs.add(sc1);
        hs.add(sc2);
        hs.add(sc3);
        Assertions.assertEquals(2, hs.size());
    }

    /***
     * This tests that two ServerCurrency objects with the same names and dates will be considered equal.
     */
    @Test
    public void testServerCurrency_equals_1(){
        ServerCurrency sc1 = new ServerCurrency("USD", "10", "2000-04-21", "USA");
        ServerCurrency sc2 = new ServerCurrency("USD", "22", "2000-04-21", "USA");
        Assertions.assertEquals(sc1, sc2);
    }

    /***
     * This tests that two ServerCurrency objects with different names but same dates will be considered not equal.
     */
    @Test
    public void testServerCurrency_equals_2(){
        ServerCurrency sc1 = new ServerCurrency("US", "10", "2000-04-21", "USA");
        ServerCurrency sc2 = new ServerCurrency("USD", "22", "2000-04-21", "USA");
        Assertions.assertNotEquals(sc1, sc2);
    }

    /***
     * This tests that two ServerCurrency objects that have the same name but different dates will be considered not equal.
     */
    @Test
    public void testServerCurrency_equals_3(){
        ServerCurrency sc1 = new ServerCurrency("USD", "10", "200-04-21", "USA");
        ServerCurrency sc2 = new ServerCurrency("USD", "22", "2000-04-21", "USA");
        Assertions.assertNotEquals(sc1, sc2);
    }

    /***
     * This tests a simple condition that should always fail. There should never be currency data from the year 1000.
     */
    @Test
    public void testConnect_checkEntries() throws Exception {
        Assertions.assertFalse(Connect.checkEntries("1000-03-12"));
        // Assertions.assertTrue(Connect.checkEntries("2021-06-16"));
    }

    /***
     * This tests that the ServerWebReader.setDate() method is functioning correctly.
     */
    @Test
    public void testServerWebReader_getDate(){
        ServerWebReader testServerWebReader = new ServerWebReader();
        assertNull(testServerWebReader.getDate());
    }

    /***
     * This tests that the ServerWebReader.setDate() method is functioning correctly.
     */
    @Test
    public void testServerWebReader_setDate(){
        ServerWebReader testServerWebReader = new ServerWebReader();
        testServerWebReader.setDate("1000");
        assertEquals("1000", testServerWebReader.getDate());
    }

    /***
     *
     */
    @Test
    public void testServerWebReader_FormatDate(){
        ServerWebReader testServerWebReader = new ServerWebReader();
        String date = testServerWebReader.formatDate("<input type=\"text\" value=\"2000-04-14\" name=\"date\" id=\"historicalDate\">");
        assertEquals("2000-04-14", date);
    }

    /***
     * This tests that the ServerWebReader.removeStringContent() is functioning as intended.
     */
    @Test
    public void testServerWebReader_removeStringContent(){
        ServerWebReader testServerWebReader = new ServerWebReader();
        String content = "<td class='rtRates'><a href='https://www.x-rates.com/graph/?from=USD&amp;to=EUR'>0.825263</a></td>";
        content = testServerWebReader.removeStringContent(content);
        assertEquals("EUR 0.825263", content);
    }

    /***
     * Verifies that the ServerWebReader.createCurrencyList() is correctly adding data into a hashset and returning the hashset.
     */
    @Test
    public void testServerWebReader_createCurrencyList(){
        ServerWebReader testServerWebReader = new ServerWebReader();
        testServerWebReader.setDate("10");
        String content = "XXX 1 \n YYY 2 \n ZZZ 3";
        HashSet<ServerCurrency> hashSet = testServerWebReader.createCurrencyList(content);
        ServerCurrency XXX = new ServerCurrency("XXX", "1", "10", "XXXX");
        ServerCurrency YYY = new ServerCurrency("YYY", "2", "10", "YYYY");
        ServerCurrency ZZZ = new ServerCurrency("ZZZ", "3", "10", "ZZZZ");
        assertTrue(hashSet.contains(XXX));
        assertTrue(hashSet.contains(YYY));
        assertTrue(hashSet.contains(ZZZ));
    }

    /***
     * Tests that the ServerWebReader.findCurrencyRate() is scanning the string, editing it, and returning the desired string.
     */
    @Test
    public void testServerWebReader_findCurrencyRate(){
        ServerWebReader testServerWebReader = new ServerWebReader();
        String content = "EUR 3.4000";
        assertEquals("3.4000", testServerWebReader.findCurrencyRate(content));
    }

    /***
     * Tests that the ServerWebReader.findCurrencyName() is scanning the string, editing it, and returning the desired string.
     */
    @Test
    public void testServerWebReader_findCurrencyName(){
        ServerWebReader testServerWebReader = new ServerWebReader();
        String content = "EUR 3.4000";
        assertEquals("EUR", testServerWebReader.findCurrencyName(content));
    }

    /***
     * Tests that the ServerWebReader.containsDBLineContent() is correctly returning true.
     */
    @Test
    public void testServerWebReader_containsDBLineContent_TRUE(){
        ServerWebReader testServerWebReader = new ServerWebReader();
        String content = "from=USD 12 <td> <td class='rtRates'> 5034";
        assertTrue(testServerWebReader.containsLineContent(content));
        content = "from=USD<tag>0.3241 <td class='rtRates'>";
        assertTrue(testServerWebReader.containsLineContent(content));
    }

    /***
     * Tests that the ServerWebReader.containsLineContent() is correctly returning false.
     */
    @Test
    public void testServerWebReader_containsDBLineContent_FALSE(){
        ServerWebReader testServerWebReader = new ServerWebReader();
        String content = "from=USD 12 <td> <td class=''> 5034";
        assertFalse(testServerWebReader.containsLineContent(content));
        content = "from=<tag>0.3241 <td class='rtRates'>";
        assertFalse(testServerWebReader.containsLineContent(content));
    }

    /***
     * This verifies that the ServerWebReader.containsDate() is correctly returning true.
     */
    @Test
    public void testServerWebReader_containsDate_TRUE(){
        ServerWebReader testServerWebReader = new ServerWebReader();
        String content = "<input type=\"text\" value=\"2021-06-16\" name=\"date\" id=\"historicalDate\">";
        assertTrue(testServerWebReader.containsDate(content));
    }

    /***
     * This verifies that the ServerWebReader.containsDate() is correctly returning false.
     */
    @Test
    public void testServerWebReader_containsDate_FALSE(){
        ServerWebReader testServerWebReader = new ServerWebReader();
        String content = "<input type=\"text\" value=\"2021-06-16\" name=\"date\" id=\"\">";
        assertFalse(testServerWebReader.containsDate(content));
    }

    /***
     * This test checks whether or not a years worth of prior currency rates, for each currency, is contained in the database. If a day is missing then it insert that days currency data.
     */
    @Test
    public void testServerWebReader_insertAnnualCurrencyData(){
        ServerWebReader testServerWebReader = new ServerWebReader();
        try {
            testServerWebReader.insertAnnualCurrencyData();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testConnect_retrieveCurrencyList() throws Exception {
        List<String> testCurrencyList = Connect.retrieveCurrencyList();
        assertNotNull(testCurrencyList);
    }
}
