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

    String COP = "https://www.exchange-rates.org/converter/USD/COP/1"; // Colombia
    String EUR = "https://www.exchange-rates.org/converter/USD/EUR/1/Y"; // Euro
    String MXN = "https://www.exchange-rates.org/converter/USD/MXN/1/Y"; // Mexico
    String JPY = "https://www.exchange-rates.org/converter/USD/JPY/1/Y"; // Japan
    String VES = "https://www.exchange-rates.org/converter/USD/VES/1/Y"; // Venezuela
    String GBP = "https://www.exchange-rates.org/converter/USD/GBP/1/Y"; // British
    String PHP = "https://www.exchange-rates.org/converter/USD/PHP/1/Y"; // Philippines
    String RUB = "https://www.exchange-rates.org/converter/USD/RUB/1/Y"; // Russia
    String CNY = "https://www.exchange-rates.org/converter/USD/CNY/1/Y"; // China
    String INR = "https://www.exchange-rates.org/converter/USD/INR/1/Y"; // India
    String[] CURRENCYNAMES = {"COP", "EUR", "USD", "MXN", "JPY", "VES", "GBP", "PHP", "RUB", "CNY"};

    // I'm not sure if there is a better way to create this hashmap. I would like it to be more automated, but at some point I would have to type raw data.
    HashMap<String, Currency> nationHashMap = new HashMap<>(24, 0.5f) {{
        put("USD", new Currency("USD", "1", "the United States"));
        put("COP", new Currency("COP", WebReader.getPage(CONSTANTS.COP), "Colombia"));
        put("EUR", new Currency("EUR", WebReader.getPage(CONSTANTS.EUR), "Europe"));
        put("MXN", new Currency("MXN", WebReader.getPage(CONSTANTS.MXN), "Mexico"));
        put("JPY", new Currency("JPY", WebReader.getPage(CONSTANTS.JPY), "Japan"));
        put("VES", new Currency("VES", WebReader.getPage(CONSTANTS.VES), "Venezuela"));
        put("GBP", new Currency("GBP", WebReader.getPage(CONSTANTS.GBP), "the United Kingdom"));
        put("PHP", new Currency("PHP", WebReader.getPage(CONSTANTS.PHP), "the Philippines"));
        put("RUB", new Currency("RUB", WebReader.getPage(CONSTANTS.RUB), "Russia"));
        put("CNY", new Currency("CNY", WebReader.getPage(CONSTANTS.CNY), "China"));
        put("INR", new Currency("INR", WebReader.getPage(CONSTANTS.INR), "India"));
    }};



}
