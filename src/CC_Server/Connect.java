package CC_Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Iterator;

/***
 * Connect class for connecting the server application to a database.
 */
public class Connect {

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

        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    /***
     * This is the method that is used to create the table for the database. If the table is already created then this method will not execute.
     * @throws Exception It can throw an exception.
     */
    public static void createTable() throws Exception {
        Connection con = getConnection();
        assert con != null;
        PreparedStatement createTable = con.prepareStatement("CREATE TABLE IF NOT EXISTS cur_db.currency (currency_name varchar(25), currency_rate varchar(25), currency_date date, PRIMARY KEY(currency_name, currency_date));");
        createTable.executeUpdate();
    }

    /***
     * This is a singular server currency object inserter. Ultimately, this program will be doing so many insertions that the runtime of this method will be problematic. I'll keep this method for testing related purposes.
     * @param serverCurrency It requires the server-side version of an instantiated currency object as a parameter.
     * @throws Exception A variety of different exceptions can be thrown by this method.
     */
    public static void insertCurrency(ServerCurrency serverCurrency) throws Exception {
        final String currency_name = serverCurrency.getName();
        final String currency_rate = serverCurrency.getRate();
        final String currency_date = serverCurrency.getDate();
        String sql = "INSERT INTO word (currency_name, currency_rate, currency_date) VALUES('" + currency_name + "' , '" + currency_rate + "' , '" + currency_date + "')";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
    }

    /***
     * Method that is used to delete all the contents of the currency table.
     * @throws Exception If an exception is thrown, it is likely due to a failure to connect to the database.
     */
    public static void deleteAll() throws Exception{
        String sql = "DELETE FROM currency;";
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
        String sql = "DROP TABLE currency;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
    }

    /***
     * The idea behind this method is to verify that new inserted data will not conflict with prior existing data within the database. The composite key will cause insertion problems. I don't want Java to attempt to insert data that already exists in the database.
     * @param date It requires the date as a parameter. This date parameter refers to the established ServerWebReader date field that is established for batch inserts.
     * @return If the date has already exists in the database, then I can assume attempting to insert will cause exceptions to be thrown, and this method will then return false.
     * @throws Exception An exception is likely caused by a database connection related problem.
     */
    public static boolean checkEntries(String date) throws Exception {
        String sql = "SELECT * FROM currency WHERE currency_date = '" + date + "';";
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
    public static void insertList(HashSet<ServerCurrency> currencyHashSet) throws Exception{
        Connection con = getConnection();
        String sql = "INSERT INTO currency (currency_name, currency_rate, currency_date) VALUES(?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        Iterator<ServerCurrency> iterator = currencyHashSet.iterator();

        while(iterator.hasNext()){
            ServerCurrency serverCurrency = iterator.next();
            ps.setString(1, serverCurrency.getName());
            ps.setString(2, serverCurrency.getRate());
            ps.setString(3, serverCurrency.getDate());
            //System.out.println(ps.toString());
            ps.executeUpdate();
        }
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