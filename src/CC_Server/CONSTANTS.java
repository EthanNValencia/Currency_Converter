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
public interface CONSTANTS {
    String DRIVER = "com.mysql.cj.jdbc.Driver";
    String URL = "jdbc:mysql://localhost:3306/cur_db";
    String USERNAME = "Currency_User";
    String PASSWORD = "EFtkgT%gt44De";
    LocalDate DATE_TODAY = LocalDate.now();
    String WEBSITE_URL = "https://www.x-rates.com/historical/?from=USD&amount=1&date=";
    Integer RANGE_OF_DAYS_TO_SCAN = 1600; // For development purposes, I will limit this to the present day.
                                         // I do not want to cause egregious network traffic for x-rates.
    String[] CREATE_TABLES = {"CREATE TABLE IF NOT EXISTS cur_db.cur_description (currency_name VARCHAR(25), currency_description VARCHAR(250), PRIMARY KEY(currency_name));",
                              "CREATE TABLE IF NOT EXISTS cur_db.cur_date (currency_date DATE, PRIMARY KEY(currency_date));",
                              "CREATE TABLE IF NOT EXISTS cur_db.cur (currency_name VARCHAR(25), currency_rate varchar(25), currency_date DATE, PRIMARY KEY(currency_name, currency_date), CONSTRAINT fk_cur_name FOREIGN KEY (currency_name) REFERENCES cur_description(currency_name), CONSTRAINT fk_cur_date FOREIGN KEY (currency_date) REFERENCES cur_date(currency_date));",
                              "CREATE TABLE IF NOT EXISTS cur_db.cur_calc (first_currency_name VARCHAR(25), second_currency_name VARCHAR(25), currency_rate DECIMAL(30, 15), currency_date DATE, PRIMARY KEY(first_currency_name, second_currency_name, currency_date), CONSTRAINT fk_calc_fir_cur_name FOREIGN KEY (first_currency_name) REFERENCES cur_description(currency_name), CONSTRAINT fk_calc_sec_cur_name FOREIGN KEY (second_currency_name) REFERENCES cur_description(currency_name), CONSTRAINT fk_calc_cur_date FOREIGN KEY (currency_date) REFERENCES cur_date(currency_date));"};
    String[] DROP_TABLES = {"DROP TABLE cur_db.cur;", "DROP TABLE cur_db.cur_date;", "DROP TABLE cur_db.cur_description;"};
}
