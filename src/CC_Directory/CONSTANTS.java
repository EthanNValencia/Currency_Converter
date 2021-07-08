/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import java.time.LocalDate;

/***
 * Constants for the program. The constants are the website URLs and the currency names. The front end relies on two things. It needs to know what the date is and it needs to know the currency short-handed names.
 */
public interface CONSTANTS {
    LocalDate DATE_TODAY = LocalDate.now();
    String[] CURRENCY_NAMES = {"USD","AED", "ARS", "AUD", "BGN", "BHD", "BND", "BRL", "BWP", "CAD", "CHF", "CLP", "CNY", "COP", "CZK", "DKK", "EUR", "GBP", "HKD", "HRK", "HUF", "IDR", "ILS", "INR", "IRR", "ISK", "JPY", "KRW", "KWD", "KZT", "LKR", "LYD", "MUR", "MXN", "MYR", "NOK", "NPR", "NZD", "OMR", "PHP", "PKR", "PLN", "QAR", "RON", "RUB", "SAR", "SEK", "SGD", "THB", "TRY", "TTD", "TWD", "VEF", "ZAR"};
}
