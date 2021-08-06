/*
Ethan J. Nephew
July 1, 2021
Server web reader and text processing class.
*/

package CC_Server;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/***
 * The ServerWebReader class is similar to the client-side WebReader class, however, it is not similar enough to make inheritance useful.
 */
public class ServerWebReader extends CC_Server.CONSTANTS {

    private String date;

    /***
     * This is an accessor method for the date variable.
     * @return It returns the date content as a string.
     */
    public String getDate() {
        return date;
    }

    /***
     * A standard mutator method that sets the date. This is for testing purposes, because dates should always be derived from processed webpages.
     * @param date Requires a date as a parameter. Format should be: YYYY-MM-DD
     */
    public void setDate(String date) {
        this.date = date;
    }

    /***
     * This method ties several other methods together. It takes a scanner that has been used to pull textual data from the web. It then filters through the textual data and engages in processing.
     * @param scan It takes a scanner object.
     * @return It returns the processed textual data as a string.
     */
    public String readText(Scanner scan){
        String reader = "";
        String content = "";
        while (scan.hasNextLine()) {
            reader = scan.nextLine() + "\n";
            reader = reader.trim() + "\n";
            if (containsDate(reader)){
                date = formatDate(reader).trim();
            }
            if (containsLineContent(reader))
                content = content.concat(reader);
            if (containsDescription(reader)){
                content = content.concat(reader);
            }
        }
        return content;
    }

    /***
     * This method takes a pre-processed line of interest that contains the date. It filters out non-vital text and returns the post-processed date.
     * @param content This is the string of pre-processed content.
     * @return It returns a post-processed date as a string.
     */
    public String formatDate(String content){
        content = content.replaceAll("<input type=\"text\" value=\"", "");
        content = content.replaceAll("\" name=\"date\" id=\"historicalDate\">", "");
        return content;
    }

    /***
     * This method is used to conduct textual processing.
     * @param content It takes a pre-processed string as a parameter.
     * @return It returns a post-processed string that has had various text filtered and removed.
     */
    public String removeStringContent(String content){
        content = content.replaceAll("<td>", "");
        content = content.replaceAll("</td>", "");
        content = content.replaceAll("'>", " ");
        content = content.replaceAll("</a>", "");
        content = content.replaceAll("[?/<>']", "");
        content = content.replaceAll("atd", "");
        content = content.replaceAll("td class=rtRates a href=https:www.x-rates.comgraphfrom=USD&amp;to=", "");
        // content = content.replaceAll("\s", "");
        return content;
    }

    /***
     * This method receives the post-processed string that contains the currency name and rate. It converts the textual data into ServerCurrency objects that are then inserted into a HashSet.
     * @param content It requires a string of post-processed text.
     * @return It returns the HashSet of ServerCurrency objects.
     */
    public HashSet<ServerCurrency> createCurrencyList(String content){
        Scanner scan = new Scanner(content);
        String currencyDescription = "";
        HashSet<ServerCurrency> currencyHashSet = new HashSet<ServerCurrency>();
        String reader = "";
        while (scan.hasNextLine()) {
            reader = scan.nextLine();
            if (!reader.matches(".*\\d.*")) {
                currencyDescription = reader;
                reader = scan.nextLine();
            }
            currencyHashSet.add(new ServerCurrency(findCurrencyName(reader), findCurrencyRate(reader), date, currencyDescription));
        }
        return currencyHashSet;
    }

    /***
     * This method removes alphabetic characters from a string. Ex: Input: "USD 1.0" Output: "1.0"
     * @param reader It takes a string that contains post-processed text.
     * @return It returns a string that has had alphabetic characters removed.
     */
    public String findCurrencyRate(String reader){
        reader = reader.replaceAll("[A-Za-z]", "");
        return reader.trim();
    }

    /***
     * This method removes numbers, comas, and periods from a string. Ex: Input: "USD 1.0" Output: "USD"
     * @param reader It takes a string that contains post-processed text.
     * @return It returns a String that has had numerical, periods, and commas removed.
     */
    public String findCurrencyName(String reader){
        reader = reader.replaceAll("[0123456789,.]", "");
        return reader.trim();
    }

    /***
     * This is a simple method that checks whether or not pre-processed lines contain content of interest. This is for searching for currency names and conversion rates.
     * @param reader Requires a string of content.
     * @return If the string contains content of specified interest then it will return true.
     */
    public boolean containsLineContent(String reader){
        if (reader.contains("<td class='rtRates'>") && reader.contains("from=USD")) // specifies textual markers
            return true;
        else
            return false;
    }

    /***
     * This is a simple method that checks whether or not pre-processed lines contain content of interest. This is for searching for date as specified on the source page.
     * @param reader Requires a string of content.
     * @return If the string contains content of specified interest then it will return true.
     */
    public boolean containsDate(String reader){
        if (reader.contains("id=\"historicalDate\">")) // specifies textual markers
            return true;
        else
            return false;
    }

    public boolean containsDescription(String reader){
        if (reader.contains("<td>") && reader.contains("</td>")) // specifies textual markers
            return true;
        else
            return false;
    }

    /***
     * This method ties many of the ServerWebReader methods together and returns the ServerCurrency objects in a HashSet.
     * @param websiteURL It requires the URL of the website as a string.
     * @return It returns the HashSet of generated ServerCurrency objects.
     */
    public HashSet<ServerCurrency> getPage(String websiteURL) {
        HashSet<ServerCurrency> currencyList = null;
        try {
            URL url = new URL(websiteURL);
            Scanner scan = new Scanner(url.openStream());
            String content = "";
            content = readText(scan);
            content = removeStringContent(content);
            currencyList = createCurrencyList(content);
            return currencyList;
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        return currencyList;
    }

    /***
     * This method pulls the historical currency conversion rate of many different currencies off a website and puts them all into the database.
     * @throws Exception A lot could go wrong with this method: database problems and website access problems.
     */
    public void insertAnnualCurrencyData() throws Exception {
        String insertDate;
        HashSet<ServerCurrency> currencyList = null;
        // currencyList = getPage(WEBSITE_URL + DATE_TODAY);
        for (int i = 0; i <= DAYS_TO_SCAN; i++) {
            insertDate = "" + DATE_TODAY.minusDays(i);
            if (!Connect.checkEntries("" + insertDate)) { // If entries with this date already exist, then cancel the insertion.
                currencyList = getPage(WEBSITE_URL + insertDate);
                if (currencyList != null) {
                    Connect.insertCurrencyDate(insertDate);
                    Connect.insertCurrencyNames(currencyList);
                    Connect.insertList(currencyList);
                }
            }
        }
        Connect.insertUSD(); // After the db fields are update the USD field will be updated too.
    }

}
