/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import java.time.LocalDate;
import java.util.HashMap;

/***
 * Constants for the program. The constants are the website URLs and the currency names.
 */
public interface CONSTANTS {

    LocalDate DATE_TODAY = LocalDate.now();
    String[] CURRENCY_NAMES = {"USD","AED", "ARS", "AUD", "BGN", "BHD", "BND", "BRL", "BWP", "CAD", "CHF", "CLP", "CNY", "COP", "CZK", "DKK", "EUR", "GBP", "HKD", "HRK", "HUF", "IDR", "ILS", "INR", "IRR", "ISK", "JPY", "KRW", "KWD", "KZT", "LKR", "LYD", "MUR", "MXN", "MYR", "NOK", "NPR", "NZD", "OMR", "PHP", "PKR", "PLN", "QAR", "RON", "RUB", "SAR", "SEK", "SGD", "THB", "TRY", "TTD", "TWD", "VEF", "ZAR"};
    String[] COMPLETE_CURRENCY_NAMES = {"AED", "ARS", "AUD", "BGN", "BHD", "BND", "BRL", "BWP", "CAD", "CHF", "CNY", "CZK", "DKK", "EUR", "GBP", "HKD", "HRK", "HUF", "ILS", "INR", "ISK", "JPY", "KWD", "KZT", "LKR", "LYD", "MUR", "MXN", "MYR", "NZD", "OMR", "PHP", "PKR", "PLN", "QAR", "RON", "RUB", "SAR", "SEK", "SGD", "THB", "TRY", "TTD", "TWD", "ZAR"};

    /*
    String COP = "https://www.exchange-rates.org/converter/USD/COP/1"; // Colombia
    String EUR = "https://www.exchange-rates.org/converter/USD/EUR/1/Y"; // Euro
    String MXN = "https://www.exchange-rates.org/converter/USD/MXN/1/Y"; // Mexico
    String JPY = "https://www.exchange-rates.org/converter/USD/JPY/1/Y"; // Japan
    String GBP = "https://www.exchange-rates.org/converter/USD/GBP/1/Y"; // British
    String PHP = "https://www.exchange-rates.org/converter/USD/PHP/1/Y"; // Philippines
    String RUB = "https://www.exchange-rates.org/converter/USD/RUB/1/Y"; // Russia
    String CNY = "https://www.exchange-rates.org/converter/USD/CNY/1/Y"; // China
    String INR = "https://www.exchange-rates.org/converter/USD/INR/1/Y"; // India
    HashMap<String, Currency> nationHashMap = new HashMap<>(24, 0.5f) {{
        put("USD", new Currency("USD", "1", "the United States"));
        put("COP", new Currency("COP", WebReader.getPage(CONSTANTS.COP), "Colombia"));
        put("EUR", new Currency("EUR", WebReader.getPage(CONSTANTS.EUR), "Europe"));
        put("MXN", new Currency("MXN", WebReader.getPage(CONSTANTS.MXN), "Mexico"));
        put("JPY", new Currency("JPY", WebReader.getPage(CONSTANTS.JPY), "Japan"));
        put("GBP", new Currency("GBP", WebReader.getPage(CONSTANTS.GBP), "the United Kingdom"));
        put("PHP", new Currency("PHP", WebReader.getPage(CONSTANTS.PHP), "the Philippines"));
        put("RUB", new Currency("RUB", WebReader.getPage(CONSTANTS.RUB), "Russia"));
        put("CNY", new Currency("CNY", WebReader.getPage(CONSTANTS.CNY), "China"));
        put("INR", new Currency("INR", WebReader.getPage(CONSTANTS.INR), "India"));
    }};
    */


}
