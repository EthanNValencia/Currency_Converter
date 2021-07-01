package CC_Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
     * @throws Exception I doubt it can throw an exception, but the tutorial told me to keep this here.
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