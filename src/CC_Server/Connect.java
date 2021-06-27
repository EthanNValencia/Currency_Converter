package CC_Server;

import CC_Directory.Currency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

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
        PreparedStatement createTable = con.prepareStatement("CREATE TABLE IF NOT EXISTS currency (id int NOT NULL AUTO_INCREMENT, currency_name varchar(25), currency_rate int, PRIMARY KEY(id))");
        createTable.executeUpdate();
    }

    /***
     * This method is used to insert data fields that are stored in a currency object into the database.
     * @param currencyName This is the name of the currency object or the short hand for that currency.
     * @param currencyConversion This is the currency conversion rate as compared to USD.
     * @throws Exception It can throw an exception.
     */
    public static void insertCurrency(String currencyName, double currencyConversion) throws Exception {
        final String currency_name = currencyName;
        final double currency_rate = currencyConversion;
        String sql = "INSERT INTO word (currency_name, currency_rate) VALUES('" + currency_name + "' , '" + currency_rate + "')";
        Connection con = getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
    }

    /***
     * Method that is used to delete all the contents of the currency table.
     * @throws Exception I doubt it can throw an exception, but the tutorial told me to keep this here.
     */
    public static void deleteAll() throws Exception{
        String sql = "DELETE FROM ??(put database location here)??;";
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
        String sql = "DROP TABLE ??(put database location here)??;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
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