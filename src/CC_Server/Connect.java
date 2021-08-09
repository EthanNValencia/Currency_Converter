/*
Ethan J. Nephew
July 1, 2021
Database connection class.
*/

package CC_Server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/***
 * Connect class for connecting the server application to a database. The methods of this class are mostly defined by their SQL queries.
 */
public class Connect extends CC_Server.CONSTANTS {

    /***
     * This is the connection method. It is used to connect to the server.
     * @return This method returns the database connection object.
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(CONSTANTS.DRIVER);
        return DriverManager.getConnection(CONSTANTS.URL, CONSTANTS.USERNAME, CONSTANTS.PASSWORD);
    }

    /***
     * This is the method that is used to create the tables for the database. If the tables are already created then this method will not execute.
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static void createTable() throws SQLException, ClassNotFoundException {
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
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static void insertCurrencyNames(HashSet<ServerCurrency> currencyHashSet) throws SQLException, ClassNotFoundException {
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
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur.
     */
    public static void insertUSD() throws SQLException, ClassNotFoundException { // this method assumes that the date entries exist in the database.
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
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static void insertCurrencyDate(LocalDate date) throws SQLException, ClassNotFoundException {
        String sql = "INSERT IGNORE INTO cur_db.cur_date (currency_date) " +
                "VALUES('" + date + "');";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
    }

    /***
     * This method is used to insert currency dates into the date table.
     * @param date It requires a string that contains the date. The format must be: "YYYY-MM-DD"
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static void insertCurrencyDate(String date) throws SQLException, ClassNotFoundException {
        String sql = "INSERT IGNORE INTO cur_db.cur_date (currency_date) VALUES('" + date + "');";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
    }

    /***
     * This is a singular server currency object inserter. Ultimately, this program will be doing so many insertions that the runtime of this method will be problematic. I'll keep this method for testing related purposes.
     * @param serverCurrency It requires the server-side version of an instantiated currency object as a parameter.
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static void insertCurrency(ServerCurrency serverCurrency) throws SQLException, ClassNotFoundException {
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
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static void deleteCurrency(ServerCurrency serverCurrency) throws SQLException, ClassNotFoundException {
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
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static void deleteAll() throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM cur_db.cur;";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
    }

    /***
     * This method is used to drop all the tables from the db. CONSIDER DELETING
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static void dropTable() throws SQLException, ClassNotFoundException {
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
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static boolean checkEntries(String date) throws SQLException, ClassNotFoundException {
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
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static void insertList(HashSet<ServerCurrency> currencyHashSet) throws SQLException, ClassNotFoundException {
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
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static List<String> retrieveCurrencyList() throws SQLException, ClassNotFoundException {
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
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static ServerCurrency getRate(ServerCurrency serverCurrency) throws SQLException, ClassNotFoundException {
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
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static ServerCurrency getDescription(ServerCurrency serverCurrency) throws SQLException, ClassNotFoundException {
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
     * @param chartObj It requires the specific server currency data object.
     * @return This method returns a list of server currency objects. This list is passed up to the GUI chart.
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static List<ServerCurrency> generateHistoricalMonthlyDataList(CurrencyChartObj chartObj) throws SQLException, ClassNotFoundException {
        String sql =
                "SELECT cur1.currency_name, AVG(((1 / cur1.currency_rate) * cur2.currency_rate)), DATE_FORMAT(cur1.currency_date, '%Y-%M') AS date FROM \n" +
                "cur_db.cur cur1, cur_db.cur cur2\n" +
                "WHERE cur1.currency_name = '" + chartObj.getFirstCurrencyName() + "' \n" +
                "AND cur2.currency_name = '" + chartObj.getSecondCurrencyName() + "'\n" +
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
        System.out.println(serverCurrencyList);
        return serverCurrencyList;
    }

    /***
     * This is used to generate the monthly average rate of change over the database historical data set.
     * @param chartObj It takes a CurrencyDataObject.
     * @return It returns the populated list of monthly average rate of change.
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static List<ServerCurrency> generateHistoricalMonthlyRateOfChangeList(CurrencyChartObj chartObj) throws SQLException, ClassNotFoundException {
        String sql = "SELECT dt2.currency_name, FORMAT(AVG((dt2.adjusted_exchange_rate / dt1.adjusted_exchange_rate) - 1), 15) AS avg_rate_of_change, DATE_FORMAT(dt2.currency_date, '%Y-%M') AS date\n" +
                "FROM (SELECT cur2.currency_name, ((1 / cur1.currency_rate) * cur2.currency_rate) AS adjusted_exchange_rate, cur1.currency_date\n" +
                "FROM cur_db.cur cur1, cur_db.cur cur2\n" +
                "WHERE cur1.currency_name = '" + chartObj.getFirstCurrencyName() + "'\n" +
                "AND cur2.currency_name = '" + chartObj.getSecondCurrencyName()+ "'\n" +
                "AND cur1.currency_date = cur2.currency_date\n" +
                "ORDER BY cur1.currency_date ASC) AS dt1, \n" +
                "(SELECT cur2.currency_name, ((1 / cur1.currency_rate) * cur2.currency_rate) AS adjusted_exchange_rate, cur1.currency_date\n" +
                "FROM cur_db.cur cur1, cur_db.cur cur2\n" +
                "WHERE cur1.currency_name = '" + chartObj.getFirstCurrencyName() + "'\n" +
                "AND cur2.currency_name = '" + chartObj.getSecondCurrencyName() + "'\n" +
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
        // System.out.println("Connect: " + serverCurrencyList);
        return serverCurrencyList;
    }

    /***
     * This returns the number of currency entries as an int.
     * @return The number of currency entries.
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static int getDescriptionCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM cur_db.cur_description;";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    /***
     * This returns the number of currency date entries as an int.
     * @return The number of currency date entries.
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static int getDateCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM cur_db.cur_date;"; // count dates
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    /***
     * Extracts a unique list of currency names from the database and returns the list in an array.
     * @return The string array of currency names.
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static String[] getCurrencyNameArray() throws SQLException, ClassNotFoundException {
        String[] nameArray = new String[getDescriptionCount()]; // should be 54
        String sql = "SELECT currency_name FROM cur_db.cur_description ORDER BY currency_name;";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        int counter = 0;
        while (rs.next()){
            nameArray[counter] = rs.getString(1);
            counter++;
        }
        return nameArray;
    }

    /***
     * Extracts a unique list of currency descriptions from the database and returns the list in an array.
     * @return The string array of currency descriptions.
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static String[] getCurrencyDescriptionArray() throws SQLException, ClassNotFoundException {
        String[] nameArray = new String[getDescriptionCount()]; // should be 54
        String sql = "SELECT currency_description FROM cur_db.cur_description ORDER BY currency_name;";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        int counter = 0;
        while (rs.next()){
            nameArray[counter] = rs.getString(1);
            counter++;
        }
        return nameArray;
    }

    /***
     * Extracts a unique list of currency dates from the database and returns the list in an array.
     * @return The string array of currency dates.
     * @throws SQLException A SQL related exception can occur. 
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static String[] getDates() throws SQLException, ClassNotFoundException {
        String[] dateArray = new String[getDateCount()]; // should be 54
        String sql = "SELECT currency_date FROM cur_db.cur_date ORDER BY currency_date ASC;";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        int counter = 0;
        while (rs.next()){
            dateArray[counter] = rs.getString(1);
            counter++;
        }
        return dateArray;
    }

    /***
     * This uses a sql query to calculate the currency rate even if the USD base rate is not being used. This will ultimately be depreciated when the cur_calc table is fully implemented.
     * @param currency1 The base rate currency name.
     * @param currency2 The comparative rate currency name.
     * @return The array of historical currency exchange rates.
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur.  
     */
    public static String[] getRates(String currency1, String currency2) throws SQLException, ClassNotFoundException { // Currency1 is the basis for comparison, normally this is the dollar
        String[] rateArray = new String[getDateCount()]; // should be 1000+
        String sql = "SELECT  REPLACE(FORMAT(((1 / cur1.currency_rate) * cur2.currency_rate), 10), ',', '') AS currency_rate, \n" +
                     "cur1.currency_date\n" +
                     "FROM cur_db.cur cur1, cur_db.cur cur2\n" +
                     "WHERE cur1.currency_name = '" + currency1 + "' \n" +
                     "AND cur2.currency_name = '" + currency2 + "'\n" +
                     "AND cur1.currency_date = cur2.currency_date\n" +
                     "ORDER BY cur1.currency_date ASC;";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        int counter = 0;
        while (rs.next()){
            rateArray[counter] = rs.getString(1);
            counter++;
        }
        return rateArray;
    }

    /***
     * Used to count the number of entries in the cur_calc table that occur between the two specified dates.
     * @param lowerLocalDate The lower date or begin date in the range.
     * @param upperLocalDate The upper date or the end date in the range.
     * @return Returns the number of entries that are within the specified date.
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static int countCalculationTableEntries(LocalDate lowerLocalDate, LocalDate upperLocalDate) throws SQLException, ClassNotFoundException {
        Connection con = getConnection();
        String sql = "SELECT COUNT(cal.first_currency_name)\n" +
                     "FROM cur_db.cur_description des, cur_db.cur_calc cal, cur_db.cur_date dt\n" +
                     "WHERE cal.first_currency_name = des.currency_name\n" +
                     "AND cal.currency_date = dt.currency_date\n" +
                     "AND cal.currency_date BETWEEN '" + lowerLocalDate + "' AND '" + upperLocalDate + "';";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    /***
     * 
     * @param firstCurrency
     * @param secondCurrency
     * @param rateArray
     * @param dateArray
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static void insertCalculatedAnnualRates(String firstCurrency, String secondCurrency, String[] rateArray, String[] dateArray) throws SQLException, ClassNotFoundException {
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

    /***
     * 
     * @param currencyNames
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     */
    public static void loadInFile(String[] currencyNames) throws SQLException, ClassNotFoundException {
        for (int i = 0; i < currencyNames.length; i++) {
            Connection con = getConnection();
            String sql = "LOAD DATA INFILE 'C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\cur_calc_table_" + currencyNames[i] + ".txt' \n" +
                         "IGNORE INTO TABLE cur_db.cur_calc \n" +
                         "FIELDS TERMINATED BY ','\n" +
                         "ENCLOSED BY '\"'\n" +
                         "LINES TERMINATED BY '\\n'\n" +
                         "IGNORE 1 LINES;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeQuery();
        }
    }

    /***
     * 
     * @throws SQLException A SQL related exception can occur.
     * @throws ClassNotFoundException A class not found exception can occur. 
     * @throws IOException A input output exception can occur.
     */
    public static void createPopulateDatabaseTextFiles() throws SQLException, ClassNotFoundException, IOException {
        String[] currencyNames = Connect.getCurrencyNameArray();
        Connect.createFiles(currencyNames);
        String[] currencyDescr = Connect.getCurrencyDescriptionArray();
        String[] dateArray = Connect.getDates();
        String[] rateArray; // COP and USD
        for (int i = 0; i < currencyNames.length; i++) {
            for (int j = 0; j < currencyNames.length; j++) {
                rateArray = Connect.getRates(currencyNames[i], currencyNames[j]); // gets annual rate of every combination
                Connect.writeToFile(currencyNames[i], currencyNames[j], rateArray, dateArray);
            }
        }
        readOnlyFiles(currencyNames);
    }

    /***
     * 
     * @param currencyName
     * @return
     */
    public static String getFilePathName(String currencyName){
        return LOAD_FILE_PART_ONE + currencyName + LOAD_FILE_PART_TWO;
    }

    /***
     * 
     * @param currencyNames
     * @throws IOException A input output exception can occur.
     */
    public static void createFiles(String[] currencyNames) throws IOException {
        for(int i = 0; i < currencyNames.length; i++) {
            File newFile = new File(getFilePathName(currencyNames[i]));
            if (!newFile.exists()) {
                PrintWriter out = new PrintWriter(new FileWriter(newFile, true));
                String beginFile = "first_currency_name, second_currency_name, currency_rate, currency_date\n";
                out.write(beginFile);  //Replace with the string
                out.close();
            }
        }
    }

    /***
     * 
     * @param currencyNames
     * @throws IOException A input output exception can occur.
     */
    public static void deleteFiles(String[] currencyNames) throws IOException {
        for(int i = 0; i < currencyNames.length; i++) {
            File newFile = new File(getFilePathName(currencyNames[i]));
            if (!newFile.exists()) {
                newFile.delete();
            }
        }
    }

    /***
     * 
     * @param currencyNames
     * @return
     * @throws IOException A input output exception can occur.
     */
    public static boolean doesTodayEntryExist(String[] currencyNames) throws IOException {
        for (int i = 0; i < currencyNames.length; i++) {
            File newFile = new File(getFilePathName(currencyNames[i]));
            Path file = newFile.toPath();
            if(newFile.exists()) {
                BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
                if (attr.creationTime().toString().contains(DATE_TODAY.toString())) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /***
     * 
     * @param currencyNames
     */
    public static void readOnlyFiles(String[] currencyNames) {
        for(int i = 0; i < currencyNames.length; i++) {
            File newFile = new File(getFilePathName(currencyNames[i]));
            if(newFile.exists()) {
                newFile.setReadOnly();
            }
        }
    }

    /***
     * 
     * @param firstCurrency
     * @param secondCurrency
     * @param rateArray
     * @param dateArray
     * @throws IOException A input output exception can occur.
     */
    public static void writeToFile(String firstCurrency, String secondCurrency, String[] rateArray, String[] dateArray) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(getFilePathName(firstCurrency), true));
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