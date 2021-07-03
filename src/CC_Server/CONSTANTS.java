package CC_Server;

import java.time.LocalDate;

public interface CONSTANTS {
    LocalDate DATE_TODAY = LocalDate.now();
    String WEBSITE_URL = "https://www.x-rates.com/historical/?from=USD&amount=1&date=";
    Integer DAYS_IN_YEAR = 3; // For development purposes, I will limit this to the present day.
                              // I do not want to cause egregious network traffic for x-rates.

    String[] CREATE_TABLES = {"CREATE TABLE IF NOT EXISTS cur_db.cur_description (currency_name VARCHAR(25), currency_description VARCHAR(250), PRIMARY KEY(currency_name));",
                             "CREATE TABLE IF NOT EXISTS cur_db.cur_date (currency_date DATE, PRIMARY KEY(currency_date));",
                             "CREATE TABLE IF NOT EXISTS cur_db.cur (currency_name VARCHAR(25), currency_rate varchar(25), currency_date DATE, PRIMARY KEY(currency_name, currency_date), CONSTRAINT fk_cur_name FOREIGN KEY (currency_name) REFERENCES cur_description(currency_name), CONSTRAINT fk_cur_date FOREIGN KEY (currency_date) REFERENCES cur_date(currency_date));"};

    String[] DROP_TABLES = {"DROP TABLE cur_db.cur;", "DROP TABLE cur_db.cur_date;", "DROP TABLE cur_db.cur_description;"};
    // String[] CURRENCY_LIST = {"AED", "ARS", "AUD", "BGN", "BHD", "BND", "BRL", "BWP", "CAD", "CHF", "CLP", "CNY", "COP", "CZK", "DKK", "EUR", "GBP", "HKD", "HRK", "HUF", "IDR", "ILS", "INR", "IRR", "ISK", "JPY", "KRW", "KWD", "KZT", "LKR", "LYD", "MUR", "MXN", "MYR", "NOK", "NPR", "NZD", "OMR", "PHP", "PKR", "PLN", "QAR", "RON", "RUB", "SAR", "SEK", "SGD", "THB", "TRY", "TTD", "TWD", "VEF", "ZAR"};
}
