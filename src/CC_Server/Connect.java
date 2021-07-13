/*
Ethan J. Nephew
July 1, 2021
Database connection class.
*/

package CC_Server;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/***
 * Connect class for connecting the server application to a database. The methods of this class are mostly defined by their SQL queries.
 */
public class Connect implements CC_Server.CONSTANTS {

    /***
     * This is the connection method. It is used to connect to the server.
     * @return This method returns the database connection object.
     * @throws Exception It can throw an exception.
     */
    public static Connection getConnection() throws Exception {
        Class.forName(CONSTANTS.DRIVER);
        return DriverManager.getConnection(CONSTANTS.URL, CONSTANTS.USERNAME, CONSTANTS.PASSWORD);
    }

    /***
     * This is the method that is used to create the tables for the database. If the tables are already created then this method will not execute.
     * @throws Exception It can throw an exception.
     */
    public static void createTable() throws Exception {
        Connection con = getConnection();
        assert con != null;
        for (int i = 0; i < CREATE_TABLES.length; i++) {
            PreparedStatement create = con.prepareStatement(CREATE_TABLES[i]);
            create.executeUpdate();
        }
    }

    /***
     * This inserts currency data by using a HashSet that is generated during the page reading and page processing stage.
     * @param currencyHashSet It requires the populated HashSet.
     * @throws Exception A exception would likely be caused by database connectivity related problems.
     */
    public static void insertCurrencyNames(HashSet<ServerCurrency> currencyHashSet) throws Exception {
        Connection con = getConnection();
        String sql = "INSERT IGNORE INTO cur_db.cur_description (currency_name, currency_description) VALUES(?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        Iterator<ServerCurrency> iterator = currencyHashSet.iterator();

        while(iterator.hasNext()){
            ServerCurrency serverCurrency = iterator.next();
            ps.setString(1, serverCurrency.getName());
            ps.setString(2, serverCurrency.getDescription());
            ps.addBatch();
        }
        ps.executeBatch();
    }

    /***
     * Since USD is the base rate, it will always be 1. Therefore there is no need to scan any USD data directly. USD data can simply be derived.
     */
    public static void insertUSD() throws Exception { // this method assumes that the date entries exist in the database.
        Connection con = getConnection();
        String sql = "INSERT IGNORE INTO cur_db.cur_description (currency_name, currency_description) VALUES ('USD', 'United States Dollar');";
        PreparedStatement ps = con.prepareStatement(sql); // adds the USD info into the cur_description table.
        ps.executeUpdate();

        // This block issues a database request to retrieve the dates of the missing fields.
        sql = "SELECT d.currency_date FROM cur_db.cur_date d WHERE NOT EXISTS (SELECT c.currency_date FROM cur_db.cur c WHERE c.currency_date = d.currency_date AND c.currency_name = 'USD');";
        ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // The point of this block is to generated and insert missing fields.
        sql = "INSERT IGNORE INTO cur_db.cur (currency_name, currency_rate, currency_date) VALUES (?,?,?);";
        ps = con.prepareStatement(sql);
        while (rs.next()){
            ps.setString(1, "USD");
            ps.setString(2, "1");
            ps.setString(3, rs.getString(1));
            ps.addBatch();
        }
        ps.executeBatch();
    }

    /***
     * This method inserts the currency dates into the database.
     * @param date It requires a LocalDate object that is to be inserted. The format must be: "YYYY-MM-DD"
     * @throws Exception A database related exception will can be thrown.
     */
    public static void insertCurrencyDate(LocalDate date) throws Exception {
        String sql = "INSERT IGNORE INTO cur_db.cur_date (currency_date) " +
                "VALUES('" + date + "');";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
    }

    /***
     * This method is used to insert currency dates into the date table.
     * @param date It requires a string that contains the date. The format must be: "YYYY-MM-DD"
     * @throws Exception A database related exception will can be thrown.
     */
    public static void insertCurrencyDate(String date) throws Exception {
        String sql = "INSERT IGNORE INTO cur_db.cur_date (currency_date) VALUES('" + date + "');";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
    }

    /***
     * This is a singular server currency object inserter. Ultimately, this program will be doing so many insertions that the runtime of this method will be problematic. I'll keep this method for testing related purposes.
     * @param serverCurrency It requires the server-side version of an instantiated currency object as a parameter.
     * @throws Exception A database related exception will can be thrown.
     */
    public static void insertCurrency(ServerCurrency serverCurrency) throws Exception {
        final String currency_name = serverCurrency.getName();
        final String currency_rate = serverCurrency.getRawRate();
        final String currency_date = serverCurrency.getDate();
        final String currency_description = serverCurrency.getDescription();
        String sql = "INSERT IGNORE INTO cur_db.cur_description WHERE " +
                     "currency_name = '" + currency_name + "' " +
                     "AND currency_description = '" + currency_description + "';";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
        sql = "INSERT IGNORE INTO cur_db.cur (currency_name, currency_rate, currency_date) " +
              "VALUES('" + currency_name + "' , '" + currency_rate + "' , '" + currency_date + "');";
        con = getConnection();
        ps = con.prepareStatement(sql);
        ps.executeUpdate();
    }

    /***
     * This deletes a specific currency entry from the cur table and deletes its description from the description table.
     * @param serverCurrency It requires a server currency with the specified name, raw rate, and date.
     * @throws Exception A database related exception will can be thrown.
     */
    public static void deleteCurrency(ServerCurrency serverCurrency) throws Exception {
        final String currency_name = serverCurrency.getName();
        final String currency_rate = serverCurrency.getRawRate();
        final String currency_date = serverCurrency.getDate();
        String sql = "DELETE FROM cur_db.cur WHERE " +
                     "currency_name = '" + currency_name +  "' AND " +
                     "currency_rate = '" + currency_rate +  "' " +
                     "AND currency_date = '" + currency_date +  "';";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
        sql = "DELETE FROM cur_db.cur_description WHERE " +
                "currency_name = '" + currency_name +  "';";
        con = getConnection();
        ps = con.prepareStatement(sql);
        ps.executeUpdate();
    }

    /***
     * Method that is used to delete all the contents of the currency table.
     * @throws Exception If an exception is thrown, it is likely due to a failure to connect to the database.
     */
    public static void deleteAll() throws Exception {
        String sql = "DELETE FROM cur_db.cur;";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
    }

    /***
     * This method is used to drop all the tables from the db. CONSIDER DELETING
     * @throws Exception A database related exception will can be thrown.
     */
    public static void dropTable() throws Exception {
        Connection con = getConnection();
        assert con != null;
        for(int i = 0; i < DROP_TABLES.length; i++) {
            PreparedStatement ps = con.prepareStatement(DROP_TABLES[i]);
            ps.executeUpdate();
        }
    }

    /***
     * The idea behind this method is to verify that new inserted data will not conflict with prior existing data within the database. The composite key will cause insertion problems. I don't want Java to attempt to insert data that already exists in the database.
     * @param date It requires the date as a parameter. This date parameter refers to the established ServerWebReader date field that is established for batch inserts.
     * @return If the date has already exists in the database, then I can assume attempting to insert will cause exceptions to be thrown, and this method will then return false.
     * @throws Exception A database related exception will can be thrown.
     */
    public static boolean checkEntries(String date) throws Exception {
        String sql = "SELECT de.currency_description, cu.currency_rate, da.currency_date " +
                     "FROM cur_db.cur_date da, cur_db.cur cu, cur_db.cur_description de " +
                     "WHERE da.currency_date = cu.currency_date " +
                     "AND cu.currency_name = de.currency_name " +
                     "AND da.currency_date = '" + date + "';";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if(rs.next() == false)
            return false;
        else
            return true;
    }

    /***
     * This is a batch insert method that is used to insert all currency related objects from a specific page. The data is pulled from pages that are indexed by date.
     * @param currencyHashSet This takes a HashSet. I moved over to a hashset, because it is a data structure that naturally eliminates duplicates. Attempting to insert data that contains duplicate composite keys is something I want to avoid.
     * @throws Exception A database related exception will can be thrown.
     */
    public static void insertList(HashSet<ServerCurrency> currencyHashSet) throws Exception {
        Connection con = getConnection();
        String sql = "INSERT IGNORE INTO cur_db.cur (currency_name, currency_rate, currency_date) VALUES(?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        Iterator<ServerCurrency> iterator = currencyHashSet.iterator();

        while(iterator.hasNext()){
            ServerCurrency serverCurrency = iterator.next();
            ps.setString(1, serverCurrency.getName());
            ps.setString(2, serverCurrency.getRawRate());
            ps.setString(3, serverCurrency.getDate());
            ps.addBatch();
        }
        ps.executeBatch();
    }

    /***
     * This is a simple method that performs a SQL query that returns all the unique currency names and writes them to a list. I used this to generate a complete list of currencies for the client side CONSTANTS.
     * @return It returns a List that contains every definite currency name that is contained in the database.
     * @throws Exception A database related exception will can be thrown.
     */
    public static List<String> retrieveCurrencyList() throws Exception {
        String sql = "SELECT DISTINCT currency_name FROM cur_db.cur;";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<String> currencyList = new ArrayList<>();
        while (rs.next()){
            currencyList.add(rs.getString(1));
        }
        return currencyList;
    }

    /***
     * This method pulls the present day USD-to-X rate from the database. Which rate is determined by the server currency name.
     * @param serverCurrency It requires a server currency object that has a name.
     * @return It returns the modified server currency object that should contain the raw rate (the rate from USD to the server currency object name).
     * @throws Exception A database related exception will can be thrown.
     */
    public static ServerCurrency getRate(ServerCurrency serverCurrency) throws Exception {
        String sql = "SELECT currency_rate FROM cur_db.cur WHERE currency_name = '" + serverCurrency.getName() + "' AND currency_date = '" + serverCurrency.getDate() + "';";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        rs.next();
        serverCurrency.setRawRate(rs.getString(1));
        return serverCurrency;
    }

    /***
     * This method takes the data object and fills the description parameters in the contained objects. The currency description will be displayed both in labels and as tooltips in the main GUI.
     * @param serverCurrency Requires a specific server currency object. The object must contain a name that is contained in the database.
     * @return It returns a modified server currency object that should contain a description.
     * @throws Exception A database related exception will can be thrown.
     */
    public static ServerCurrency getDescription(ServerCurrency serverCurrency) throws Exception {
        String sql = "SELECT currency_description FROM cur_db.cur_description WHERE currency_name = '" + serverCurrency.getName() + "';";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        rs.next();
        serverCurrency.setDescription(rs.getString(1));
        return serverCurrency;
    }

    /***
     * This method uses name of a server currency object to generate a list of historical currency rates. The currency rates will be averaged by the month.
     * @param currencyDataObject It requires the specific server currency data object.
     * @return This method returns a list of server currency objects. This list is passed up to the GUI chart.
     * @throws Exception A database related exception can be thrown.
     */
    public static List<ServerCurrency> generateHistoricalMonthlyDataList(CurrencyDataObject currencyDataObject) throws Exception {
        String sql =
                "SELECT cur1.currency_name, AVG(((1 / cur1.currency_rate) * cur2.currency_rate)), DATE_FORMAT(cur1.currency_date, '%Y-%M') AS date FROM \n" +
                "cur_db.cur cur1, cur_db.cur cur2\n" +
                "WHERE cur1.currency_name = '" + currencyDataObject.getCurrency1().getName() + "' \n" +
                "AND cur2.currency_name = '" + currencyDataObject.getCurrency2().getName() + "'\n" +
                "AND cur1.currency_date = cur2.currency_date\n" +
                "GROUP BY DATE_FORMAT(cur1.currency_date,'%Y-%M-&D')\n" +
                "ORDER BY cur1.currency_date ASC;";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<ServerCurrency> serverCurrencyList = new ArrayList<>();
        while(rs.next()) {
            ServerCurrency sc = new ServerCurrency();
            sc.setName(rs.getString(1));
            sc.setRawRate(rs.getString(2));
            sc.setDate(rs.getString(3));
            serverCurrencyList.add(sc);
        }
        return serverCurrencyList;
    }


    public static List<ServerCurrency> generateHistoricalMonthlyRateOfChangeList(CurrencyDataObject currencyDataObject) throws Exception {
        String sql = "SELECT dt2.currency_name, FORMAT(AVG((dt2.adjusted_exchange_rate / dt1.adjusted_exchange_rate) - 1), 15) AS avg_rate_of_change, DATE_FORMAT(dt2.currency_date, '%Y-%M') AS date\n" +
                "FROM (SELECT cur2.currency_name, ((1 / cur1.currency_rate) * cur2.currency_rate) AS adjusted_exchange_rate, cur1.currency_date\n" +
                "FROM cur_db.cur cur1, cur_db.cur cur2\n" +
                "WHERE cur1.currency_name = '" + currencyDataObject.getCurrency1().getName() + "'\n" +
                "AND cur2.currency_name = '" + currencyDataObject.getCurrency2().getName() + "'\n" +
                "AND cur1.currency_date = cur2.currency_date\n" +
                "ORDER BY cur1.currency_date ASC) AS dt1, \n" +
                "(SELECT cur2.currency_name, ((1 / cur1.currency_rate) * cur2.currency_rate) AS adjusted_exchange_rate, cur1.currency_date\n" +
                "FROM cur_db.cur cur1, cur_db.cur cur2\n" +
                "WHERE cur1.currency_name = '" + currencyDataObject.getCurrency1().getName() + "'\n" +
                "AND cur2.currency_name = '" + currencyDataObject.getCurrency2().getName() + "'\n" +
                "AND cur1.currency_date = cur2.currency_date\n" +
                "ORDER BY cur1.currency_date ASC) AS dt2\n" +
                "WHERE DATEDIFF(dt1.currency_date, dt2.currency_date) = '1'\n" +
                "AND dt1.currency_name = dt2.currency_name\n" +
                "GROUP BY DATE_FORMAT(dt2.currency_date,'%Y-%M-&D')\n" +
                "ORDER BY dt1.currency_date ASC;";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<ServerCurrency> serverCurrencyList = new ArrayList<>();
        while(rs.next()) {
            ServerCurrency sc = new ServerCurrency();
            sc.setName(rs.getString(1));
            sc.setRawRate(rs.getString(2));
            sc.setDate(rs.getString(3));
            serverCurrencyList.add(sc);
        }
        return serverCurrencyList;
    }

    public static String[] getCurrencyNameArray() throws Exception {
        String sql = "SELECT COUNT(*) FROM cur_db.cur_description;";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String[] nameArray = new String[rs.getInt(1)]; // should be 54
        sql = "SELECT currency_name FROM cur_db.cur_description ORDER BY currency_name;";
        con = getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        int counter = 0;
        while (rs.next()){
            nameArray[counter] = rs.getString(1);
            counter++;
        }
        return nameArray;
    }

    public static String[] getCurrencyDescriptionArray() throws Exception {
        // SELECT currency_description FROM cur_db.cur_description ORDER BY currency_name;
        String sql = "SELECT COUNT(*) FROM cur_db.cur_description;";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String[] nameArray = new String[rs.getInt(1)]; // should be 54
        sql = "SELECT currency_description FROM cur_db.cur_description ORDER BY currency_name;";
        con = getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        int counter = 0;
        while (rs.next()){
            nameArray[counter] = rs.getString(1);
            counter++;
        }
        return nameArray;
    }

    public static String[] getDates() throws Exception {
        String sql = "SELECT COUNT(*) FROM cur_db.cur_date;"; // count db
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String[] dateArray = new String[rs.getInt(1)]; // should be 54
        sql = "SELECT currency_date FROM cur_db.cur_date ORDER BY currency_date ASC;";
        con = getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        int counter = 0;
        while (rs.next()){
            dateArray[counter] = rs.getString(1);
            counter++;
        }
        return dateArray;
    }

    public static String[] getRates(String currency1, String currency2) throws Exception { // Currency1 is the basis for comparison, normally this is the dollar
        String sql = "SELECT COUNT(*) FROM cur_db.cur_date;"; // count db
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String[] rateArray = new String[rs.getInt(1)]; // should be 1000+
        sql = "SELECT FORMAT(((1 / cur1.currency_rate) * cur2.currency_rate), 10) AS currency_rate, \n" +
                "cur1.currency_date\n" +
                "FROM cur_db.cur cur1, cur_db.cur cur2\n" +
                "WHERE cur1.currency_name = '" + currency1 + "' \n" +
                "AND cur2.currency_name = '" + currency2 + "'\n" +
                "AND cur1.currency_date = cur2.currency_date\n" +
                "ORDER BY cur1.currency_date ASC;";
        con = getConnection();
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        int counter = 0;
        while (rs.next()){
            rateArray[counter] = rs.getString(1);
            counter++;
        }
        return rateArray;
    }

    public static void insertCalculatedAnnualRates(String firstCurrency, String secondCurrency, String[] rateArray, String[] dateArray) throws Exception {
        Connection con = getConnection();
        String sql = "INSERT IGNORE INTO cur_db.cur_calc (first_currency_name, second_currency_name, currency_rate, currency_date) VALUES(?, ?, ? ,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        for(int i = 0; i < rateArray.length; i++) {
            ps.setString(1, firstCurrency);
            ps.setString(2, secondCurrency);
            ps.setString(3, rateArray[i]);
            ps.setString(4, dateArray[i]);
            ps.addBatch();
        }
        ps.executeBatch();
    }
// "src\\CC_Server\\cur_calc_table.txt"


    public static void createPopulateDatabaseTextFiles() throws Exception {
        String[] currencyNames = Connect.getCurrencyNameArray();
        Connect.createFiles(currencyNames);
        String[] currencyDescr = Connect.getCurrencyDescriptionArray();
        String[] dateArray = Connect.getDates();
        String[] rateArray = Connect.getRates(currencyNames[12], currencyNames[51]); // COP and USD
        // Connect.writeToFile(currencyNames[12], currencyNames[51], rateArray, dateArray); // This takes too long.

        for (int i = 0; i < currencyNames.length; i++) {
            for (int j = 0; j < currencyNames.length; j++) {
                // adjustedNames[counter] = currencyNames[i] + " to " + currencyNames[j];
                // adjustedDescr[counter] = currencyDescr[i] + " to " + currencyDescr[j];
                rateArray = Connect.getRates(currencyNames[i], currencyNames[j]); // gets annual rate of every combination
                Connect.writeToFile(currencyNames[i], currencyNames[j], rateArray, dateArray);
            }
        }
        readOnlyFiles(currencyNames);
    }

    public static void createFiles(String[] currencyNames) throws IOException {
        for(int i = 0; i < currencyNames.length; i++) {
            File newFile = new File("C:\\CC_DatabaseTextFiles\\cur_calc_table_" + currencyNames[i] + ".txt");
            if(!newFile.exists()) {
                PrintWriter out = new PrintWriter(new FileWriter(newFile, true));
                String beginFile = "first_currency_name, second_currency_name, currency_rate, currency_date\n";
                out.write(beginFile);  //Replace with the string
                out.close();
            }
        }
    }

    public static void readOnlyFiles(String[] currencyNames) throws IOException {
        for(int i = 0; i < currencyNames.length; i++) {
            File newFile = new File("C:\\CC_DatabaseTextFiles\\cur_calc_table_" + currencyNames[i] + ".txt");
            if(newFile.exists()) {
                newFile.setReadOnly();
            }
        }
    }


    public static void writeToFile(String firstCurrency, String secondCurrency, String[] rateArray, String[] dateArray) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("C:\\CC_DatabaseTextFiles\\cur_calc_table_" + firstCurrency + ".txt", true));
        StringBuilder writeThis = new StringBuilder();
        if(rateArray.length == dateArray.length) {
            for(int i = 0; i < rateArray.length; i++){
                writeThis.append("\"").append(firstCurrency).append("\",\"").append(secondCurrency).append("\",\"").append(rateArray[i]).append("\",\"").append(dateArray[i]).append("\"\n");
            }
        }
        out.write(String.valueOf(writeThis));  //Replace with the string
        out.close();
    }

    /***
     * This is an overridden toString method that is useful for testing purposes.
     * @return Returns a string of the object name.
     */
    @Override
    public String toString() {
        return "CurrencyConversionServer.Connect";
    }
}