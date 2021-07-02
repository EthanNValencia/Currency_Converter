package CC_Server;

import java.time.LocalDate;

public interface CONSTANTS {
    LocalDate DATE_TODAY = LocalDate.now();
    String WEBSITE_URL = "https://www.x-rates.com/historical/?from=USD&amount=1&date=";
    Integer DAYS_IN_YEAR = 365;
}

/*
The website is https://www.x-rates.com/historical/?from=USD&amount=1&date=2021-06-16
The date of the page can be easily manipulated.
*/
