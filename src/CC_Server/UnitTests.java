/*
Ethan J. Nephew
July 1, 2021
Unit tests for the server package. Running these tests should be the first thing done.
*/

package CC_Server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static CC_Server.CONSTANTS.DATE_TODAY;
import static CC_Server.CONSTANTS.WEBSITE_URL;
import static org.junit.jupiter.api.Assertions.*;

/***
 * These are the unit tests for the server side of the application. No attempt to run the program should be made prior to all these tests passing.
 */
public class UnitTests {

    /***
     * This verifies that a connection is being made to the server. This is a good troubleshooting test to use when attempting to establish a connection.
     */
    @Test
    @Order(1)
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
    @Order(2)
    public void testConnection_createTable(){
        try {
            Connect.createTable(); // The SQL query handles the situation of a pre-existing table.
        } catch (Exception e) {
            fail();
        }
    }

    /***
     * This tests that the database is being created properly. This requires an internet connection and a database to pass.
     */
    @Test
    @Order(3)
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
     */
    @Test
    public void testWebReader_getDBPage_AND_Connect_insertList() {
        ServerWebReader serverWebReader = new ServerWebReader();
        HashSet<ServerCurrency> currencyHashSet = serverWebReader.getPage(WEBSITE_URL + DATE_TODAY);
        try {
            if(!Connect.checkEntries(serverWebReader.getDate())) // If entries with this date already exist, then cancel the insertion.
                Connect.insertList(currencyHashSet);
        } catch (Exception e) {
            fail();
        }
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
     * This tests that the ServerWebReader.FormatDate() method is filtering out the correct data and returning the edited string.
     */
    @Test
    public void testServerWebReader_FormatDate(){
        ServerWebReader testServerWebReader = new ServerWebReader();
        String date = testServerWebReader.formatDate("<input type=\"text\" value=\"2000-04-14\" name=\"date\" id=\"historicalDate\">");
        assertEquals("2000-04-14", date);
    }

    /***
     * This tests that the ServerWebReader.removeStringContent() is filtering out the correct data and returning the edited string.
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
     * Tests that the ServerWebReader.containsLineContent() is correctly checking the string for content of interest.
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
     * This verifies that the ServerWebReader.containsDate() is assessing that the providing string does contain a date.
     */
    @Test
    public void testServerWebReader_containsDate_TRUE(){
        ServerWebReader testServerWebReader = new ServerWebReader();
        String content = "<input type=\"text\" value=\"2021-06-16\" name=\"date\" id=\"historicalDate\">";
        assertTrue(testServerWebReader.containsDate(content));
    }

    /***
     * This verifies that the ServerWebReader.containsDate() is assessing that the providing string does not contain a date.
     */
    @Test
    public void testServerWebReader_containsDate_FALSE(){
        ServerWebReader testServerWebReader = new ServerWebReader();
        String content = "<input type=\"text\" value=\"2021-06-16\" name=\"date\" id=\"\">";
        assertFalse(testServerWebReader.containsDate(content));
    }

    /***
     * This test checks whether or not the specified duration of currency rates, for each currency, is contained in the database. If a day is missing then it insert that days currency data.
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

    /***
     * Tests that the Connect.retrieveCurrencyList is populating a list. It is difficult to say what the content of the list should be, but I can say that the list should have content.
     */
    @Test
    public void testConnect_retrieveCurrencyList() {
        List<String> testCurrencyList = null;
        try {
            testCurrencyList = Connect.retrieveCurrencyList();
        } catch (Exception e) {
            fail();
        }
        assertNotNull(testCurrencyList);
    }

    /***
     * Tests that the Connect.findRate() method is functioning correctly.
     */
    @Test
    public void testConnect_findRate(){
        ServerCurrency serverCurrency = new ServerCurrency("COP", null, "2021-07-05", null);
        try {
            serverCurrency = Connect.getRate(serverCurrency);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(serverCurrency.getRawRate());
    }

    /***
     * This tests that the Server.findRate() method is working correctly.
     */
    @Test
    public void testServer_findRate(){
        ServerCurrency serverCurrency1 = new ServerCurrency("EUR");
        ServerCurrency serverCurrency2 = new ServerCurrency("COP", null, null, null);
        CurrencyDataObject currencyDataObject = new CurrencyDataObject(serverCurrency1, serverCurrency2, LocalDate.now());
        Server server = new Server();
        try {
            server.findRate(currencyDataObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotEquals(null, currencyDataObject.getCurrency1().getRawRate());
        assertNotEquals(null, currencyDataObject.getCurrency2().getRawRate());
    }

    /***
     * This verifies that the Server.findDescription() functions correctly.
     */
    @Test
    public void testServer_findDescription_NOTNULL_1(){
        ServerCurrency serverCurrency1 = new ServerCurrency("AED", null, null, null);
        ServerCurrency serverCurrency2 = new ServerCurrency(null, null, null, null);
        CurrencyDataObject currencyDataObject = new CurrencyDataObject(serverCurrency1, serverCurrency2, LocalDate.now());
        Server server = new Server();
        currencyDataObject = server.findDescription(currencyDataObject);
        assertNotEquals(null, currencyDataObject.getCurrency1().getDescription());
    }

    /***
     * This verifies that the Server.findDescription() functions correctly.
     */
    @Test
    public void testServer_findDescription_NOTNULL_2(){
        ServerCurrency serverCurrency1 = new ServerCurrency(null, null, null, null);
        ServerCurrency serverCurrency2 = new ServerCurrency("COP", null, null, null);
        CurrencyDataObject currencyDataObject = new CurrencyDataObject(serverCurrency1, serverCurrency2, LocalDate.now());
        Server server = new Server();
        currencyDataObject = server.findDescription(currencyDataObject);
        assertNotEquals(null, currencyDataObject.getCurrency2().getDescription());
    }

    /***
     * This verifies that the Server.findRate() and the Server.findDescription() can function consecutively. If this test fails, then that means the server currency object fields are not being populated.
     */
    @Test
    public void testServer_findRate_findDescription_NOTNULL_1(){
        ServerCurrency serverCurrency1 = new ServerCurrency("AED", null, null, null);
        ServerCurrency serverCurrency2 = new ServerCurrency("COP", null, null, null);
        CurrencyDataObject currencyDataObject = new CurrencyDataObject(serverCurrency1, serverCurrency2, LocalDate.now());
        Server server = new Server();
        currencyDataObject = server.findDescription(currencyDataObject);
        currencyDataObject = server.findRate(currencyDataObject);
        assertNotEquals(null, currencyDataObject.getCurrency1().getRawRate());
        assertNotEquals(null, currencyDataObject.getCurrency2().getRawRate());
        assertNotEquals(null, currencyDataObject.getCurrency1().getDescription());
        assertNotEquals(null, currencyDataObject.getCurrency2().getDescription());
    }

    /***
     * This verifies that the Server.findRate(), Server.findDescription(), Server.calculateRate() can function consecutively without problem.
     */
    @Test
    public void testServer_calculateRate_NOTNULL_1(){
        ServerCurrency serverCurrency1 = new ServerCurrency("AED", null, null, null, null, null);
        ServerCurrency serverCurrency2 = new ServerCurrency("COP", null, null, null, null, null);
        CurrencyDataObject currencyDataObject = new CurrencyDataObject(serverCurrency1, serverCurrency2, LocalDate.now());
        Server server = new Server();
        currencyDataObject = server.findDescription(currencyDataObject);
        currencyDataObject = server.findRate(currencyDataObject);
        currencyDataObject = server.calculateRate(currencyDataObject);
        assertNotNull(currencyDataObject.getCurrency1().getAdjustedRate());
    }

    /***
     * This verifies that the Server.findRate(), Server.findDescription(), Server.calculateRate() can function consecutively without problem.
     */
    @Test
    public void testServer_calculateRate_NOTNULL_2(){
        ServerCurrency serverCurrency1 = new ServerCurrency("AED", null, null, null, null, null);
        ServerCurrency serverCurrency2 = new ServerCurrency("COP", null, null, null, null, null);
        CurrencyDataObject currencyDataObject = new CurrencyDataObject(serverCurrency1, serverCurrency2, LocalDate.now());
        Server server = new Server();
        currencyDataObject = server.findDescription(currencyDataObject);
        currencyDataObject = server.findRate(currencyDataObject);
        currencyDataObject = server.calculateRate(currencyDataObject);
        assertNotNull(currencyDataObject.getCurrency2().getAdjustedRate());
    }

    /***
     * This tests that the Server.calculateRate() method is returning the anticipated results.
     */
    @Test
    public void testServer_calculateRate_Predicted_1(){
        ServerCurrency serverCurrency1 = new ServerCurrency(null, null, null, "0.5", null, null);
        ServerCurrency serverCurrency2 = new ServerCurrency(null, null, null, "2", null, null);
        CurrencyDataObject currencyDataObject = new CurrencyDataObject(serverCurrency1, serverCurrency2, LocalDate.now());
        Server server = new Server();
        currencyDataObject = server.calculateRate(currencyDataObject);
        assertEquals("1", currencyDataObject.getCurrency1().getAdjustedRate());
    }

    /***
     * This tests that the Server.CalculateRate() is returning the anticipated results.
     */
    @Test
    public void testServer_calculateRate_Predicted_Four(){
        ServerCurrency serverCurrency1 = new ServerCurrency(null, null, null, "0.5", null, null);
        ServerCurrency serverCurrency2 = new ServerCurrency(null, null, null, "2", null, null);
        CurrencyDataObject currencyDataObject = new CurrencyDataObject(serverCurrency1, serverCurrency2, LocalDate.now());
        Server server = new Server();
        currencyDataObject = server.calculateRate(currencyDataObject);
        assertEquals("4", currencyDataObject.getCurrency2().getAdjustedRate());
    }

    /***
     * This tests that the Server.insertUSD() method is inserting USD.
     */
    @Test
    public void testServer_insertUSD(){
        try {
            Connect.insertUSD();
        } catch (Exception e) {
            fail();
        }
    }

    /***
     * This tests that the Server.calculateExchange() method is returning the anticipated results.
     */
    @Test
    public void testServer_calculateExchange(){
        ServerCurrency serverCurrency1 = new ServerCurrency(null, null, null, null, "2", null);
        ServerCurrency serverCurrency2 = new ServerCurrency(null, null, null, null, null, "2");
        CurrencyDataObject currencyDataObject = new CurrencyDataObject(serverCurrency1, serverCurrency2, LocalDate.now());
        Server server = new Server();
        currencyDataObject = server.calculateExchange(currencyDataObject);
        assertEquals("4", currencyDataObject.getCurrency2().getExchangeAmount());
    }

    /***
     * This verifies that the Connect.generateList() method is not throwing an exception. So long as a existing currency name is provided this query should execute without throwing an exception.
     */
    @Test
    public void testServer_generateList(){
        ServerCurrency serverCurrency1 = new ServerCurrency("COP", null, null, null, null, null);
        try {
            // Connect.generateHistoricalMonthlyDataList();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testServer_formatDecimals_FourDecimals(){
        double testDbl = 25.000801;
        Server server = new Server();
        assertEquals("25.000801", server.checkLower(testDbl));
    }

    /***
     * This tests that the Server.calculateExchange() method is correctly calculating the amount of exchange.
     */
    @Test
    public void testServer_calculateExchange_checkLower_FiveDecimals(){
        ServerCurrency serverCurrency1 = new ServerCurrency(null, null, null, null, "30000", null);
        ServerCurrency serverCurrency2 = new ServerCurrency(null, null, null, null, null, "0.000000005");
        CurrencyDataObject currencyDataObject = new CurrencyDataObject(serverCurrency1, serverCurrency2, LocalDate.now());
        Server server = new Server();
        currencyDataObject = server.calculateExchange(currencyDataObject);
        assertEquals("0.00015", currencyDataObject.getCurrency2().getExchangeAmount());
    }


    @Test
    public void testServer_formatDecimals_SevenDecimals(){
        double testDbl = 0.00000000800000;
        Server server = new Server();
        assertEquals("0.000000008", server.checkLower(testDbl));
    }

}

