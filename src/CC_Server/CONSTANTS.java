/*
Ethan J. Nephew
July 9, 2021
Server constants. Used as a centralized repository for vital server related variables.
*/

package CC_Server;

import java.time.LocalDate;

/***
 * This effectively serves as a center for sql statements and important variables.
 */
public abstract class CONSTANTS {
    static String LOAD_FILE_PART_ONE = "C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\cur_calc_table_";
    static String LOAD_FILE_PART_TWO = ".txt";
    static String DRIVER = "com.mysql.cj.jdbc.Driver";
    static String URL = "jdbc:mysql://localhost:3306/cur_db";
    // String USERNAME = "Currency_User";
    static String USERNAME = "Currency_User";
    static String PASSWORD = "EFtkgT%gt44De";
    static LocalDate DATE_TODAY = LocalDate.now();
    static String WEBSITE_URL = "https://www.x-rates.com/historical/?from=USD&amount=1&date=";
    static Integer[] DAYS_RANGE = {1, 10, 30, 50, 100, 1000};
    static Integer DAYS_TO_SCAN = 1000; // For development purposes, I will limit this to the present day.
                                         // I do not want to cause egregious network traffic for x-rates.
    static String[] CREATE_TABLES = {"CREATE TABLE IF NOT EXISTS cur_db.cur_description (currency_name VARCHAR(25), currency_description VARCHAR(250), PRIMARY KEY(currency_name));",
                              "CREATE TABLE IF NOT EXISTS cur_db.cur_date (currency_date DATE, PRIMARY KEY(currency_date));",
                              "CREATE TABLE IF NOT EXISTS cur_db.cur (currency_name VARCHAR(25), currency_rate varchar(25), currency_date DATE, PRIMARY KEY(currency_name, currency_date), CONSTRAINT fk_cur_name FOREIGN KEY (currency_name) REFERENCES cur_description(currency_name), CONSTRAINT fk_cur_date FOREIGN KEY (currency_date) REFERENCES cur_date(currency_date));",
                              "CREATE TABLE IF NOT EXISTS cur_db.cur_calc (first_currency_name VARCHAR(25), second_currency_name VARCHAR(25), currency_rate DECIMAL(30, 15), currency_date DATE, PRIMARY KEY(first_currency_name, second_currency_name, currency_date), CONSTRAINT fk_calc_fir_cur_name FOREIGN KEY (first_currency_name) REFERENCES cur_description(currency_name), CONSTRAINT fk_calc_sec_cur_name FOREIGN KEY (second_currency_name) REFERENCES cur_description(currency_name), CONSTRAINT fk_calc_cur_date FOREIGN KEY (currency_date) REFERENCES cur_date(currency_date));"};
    static String[] DROP_TABLES = {"DROP TABLE cur_db.cur;", "DROP TABLE cur_db.cur_date;", "DROP TABLE cur_db.cur_description;"};
}
