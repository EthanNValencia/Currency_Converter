/*
Ethan J. Nephew
July 1, 2021
Database connection class.
*/

package CC_Server;

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
     * @return This method returns the connection object.
     * @throws Exception It can throw an exception.
     */
    public static Connection getConnection() throws Exception {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/cur_db";
        String username = "Currency_User";
        String password = "EFtkgT%gt44De";
        Class.forName(driver);

        return DriverManager.getConnection(url, username, password);
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
     * Since USD is the base rate, it will always be 1. Therefore there is no need to scan any USD data directly. USD data can simply be derived from calculations.
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
     * @param date It requires a LocalDate object that is to be inserted.
     * @throws Exception
     */
    public static void insertCurrencyDate(LocalDate date) throws Exception {
        String sql = "INSERT IGNORE INTO cur_db.cur_date (currency_date) " +
                "VALUES('" + date + "');";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
    }

    /***
     *
     * @param date
     * @throws Exception
     */
    public static void insertCurrencyDate(String date) throws Exception {
        String sql = "INSERT IGNORE INTO cur_db.cur_date (currency_date) " +
                "VALUES('" + date + "');";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
    }

    /***
     * This is a singular server currency object inserter. Ultimately, this program will be doing so many insertions that the runtime of this method will be problematic. I'll keep this method for testing related purposes.
     * @param serverCurrency It requires the server-side version of an instantiated currency object as a parameter.
     * @throws Exception A variety of different exceptions can be thrown by this method.
     */
    public static void insertCurrency(ServerCurrency serverCurrency) throws Exception {
        final String currency_name = serverCurrency.getName();
        final String currency_rate = serverCurrency.getRawRate();
        final String currency_date = serverCurrency.getDate();
        String sql = "INSERT IGNORE INTO cur_db.cur (currency_name, currency_rate, currency_date) " +
                     "VALUES('" + currency_name + "' , '" + currency_rate + "' , '" + currency_date + "');";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
    }

    /***
     *
     * @param serverCurrency
     * @throws Exception
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
     * This method is used to drop the table from the db.
     * @throws Exception This method will throw an exception, especially if something goes wrong during the db connection and updating processes.
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
     * @throws Exception An exception is likely caused by a database connection related problem.
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
     * @throws Exception If an exception is thrown here it will likely be from database connectivity related problems.
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
            //System.out.println(ps.toString());
            ps.addBatch();
        }
        ps.executeBatch();
    }

    /***
     * This is a simple method that performs a SQL query that returns all the unique currency names and writes them to a list.
     * @return It returns a List that contains strings.
     * @throws Exception It can throw an exception from a variety of database interactive ways.
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
     * This method pulls the USD-to-X rate.
     * @param serverCurrency
     * @return
     * @throws Exception
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
     * This method takes the data object and fills the description parameters in the contained objects.
     * @param serverCurrency
     * @return
     * @throws Exception
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

    public static List<ServerCurrency> generateList(ServerCurrency serverCurrency) throws Exception {
        String sql = "SELECT * FROM cur_db.cur WHERE currency_name = '" + serverCurrency.getName() + "' ORDER BY currency_date;";
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

    /***
     * This is an overridden toString method that is useful for testing purposes.
     * @return Returns a string of the object name.
     */
    @Override
    public String toString() {
        return "CurrencyConversionServer.Connect";
    }
}