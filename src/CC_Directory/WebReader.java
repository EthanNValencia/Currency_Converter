/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import CC_Server.ServerCurrency;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/***
 * WebReader class that pulls simple data from websites.
 */
public class WebReader implements CONSTANTS {

    private static String date;

    /***
     * Method used to scan the specified HTML source page and conduct basic text filtering. Filters text based on individual line contents.
     */
    public static String getPage(String websiteURL) {
        try {
            URL url = new URL(websiteURL);
            Scanner scan = new Scanner(url.openStream());
            String content = "";
            content = readText(scan);
            content = editString(content);
            return content;
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        return "-1";
    }

    /***
     * This method scans the text from the specified webpage. It will scan all the text until there are no more lines to scan.
     * @param scan The parameter is a scanner.
     * @return It will return the content that was scanned.
     */
    public static String readText(Scanner scan){
        String reader = "";
        String content = "";
        while (scan.hasNextLine()) {
            reader = scan.nextLine() + "\n";
            if (checkLineContent(reader))
                content = content.concat(reader);
        }
        return content;
    }

    /***
     * This method reads a String and returns true if it contains String of interest.
     * @param reader This is the String that will be evaluated.
     * @return It will return true if the String contains the String of interest.
     */
    public static boolean checkLineContent(String reader){
        if (reader.contains("ctl00_M_lblToAmount")) // specifies textual markers
            return true;
        else
            return false;
    }

    /***
     * Method that takes a string and removes non-numerics from the string.
     * @param content Takes the String that should be edited.
     * @return Returns the edited String.
     */
    public static String editString(String content){
        content = content.replaceAll("ctl00_M_lblToAmount", ""); // The double zeros should be removed now.
        content = content.replaceAll("[^\\d.]", "");
        return content;
    }

    public static String getDBPage(String websiteURL) {
        try {
            URL url = new URL(websiteURL);
            Scanner scan = new Scanner(url.openStream());
            String content = "";
            content = readDBText(scan);
            // date = getDBDate(content);
            content = removeDBContent(content);
            List<ServerCurrency> currencyList = createCurrencyList(content);
            System.out.println(currencyList);
            return content;
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        return "-1";
    }
    /*
    public static String getDBDate(String content){

    }
*/
    public static String readDBText(Scanner scan){
        String reader = "";
        String content = "";
        while (scan.hasNextLine()) {
            reader = scan.nextLine() + "\n";
            reader = reader.trim() + "\n";
            if (containsDBDate(reader)){
                date = formateDate(reader).trim();
            }
            if (containsDBLineContent(reader))
                content = content.concat(reader);
        }
        return content;
    }

    public static String formateDate(String content){
        content = content.replaceAll("<input type=\"text\" value=\"", "");
        content = content.replaceAll("\" name=\"date\" id=\"historicalDate\">", "");
        return content;
    }

    public static String removeDBContent(String content){
        content = content.replaceAll("'>", " ");
        content = content.replaceAll("[?/<>']", "");
        content = content.replaceAll("atd", "");
        content = content.replaceAll("td class=rtRates a href=https:www.x-rates.comgraphfrom=USD&amp;to=", "");
        // content = content.replaceAll("\s", "");
        return content;
    }

    public static List<ServerCurrency> createCurrencyList(String content){
        Scanner scan = new Scanner(content);
        List<ServerCurrency> currencyList = new ArrayList<ServerCurrency>();
        String reader = "";
        while (scan.hasNextLine()) {
            reader = scan.nextLine();
            currencyList.add(new ServerCurrency(findCurrencyName(reader), findCurrencyRate(reader), date));
        }
        return currencyList;
    }

    public static String findCurrencyRate(String reader){
        reader = reader.replaceAll("[A-Za-z]", "");
        return reader;
    }

    public static String findCurrencyName(String reader){
        reader = reader.replaceAll("[0123456789,.]", "");
        return reader;
    }

    public static boolean containsDBLineContent(String reader){
        if (reader.contains("<td class='rtRates'>") && reader.contains("from=USD")) // specifies textual markers
            return true;
        else
            return false;
    }

    public static boolean containsDBDate(String reader){
        if (reader.contains("id=\"historicalDate\">")) // specifies textual markers
            return true;
        else
            return false;
    }

}
