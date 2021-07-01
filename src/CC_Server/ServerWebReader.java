package CC_Server;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServerWebReader {

    private String date;

    public String readDBText(Scanner scan){
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

    public String formateDate(String content){
        content = content.replaceAll("<input type=\"text\" value=\"", "");
        content = content.replaceAll("\" name=\"date\" id=\"historicalDate\">", "");
        return content;
    }

    public String removeDBContent(String content){
        content = content.replaceAll("'>", " ");
        content = content.replaceAll("[?/<>']", "");
        content = content.replaceAll("atd", "");
        content = content.replaceAll("td class=rtRates a href=https:www.x-rates.comgraphfrom=USD&amp;to=", "");
        // content = content.replaceAll("\s", "");
        return content;
    }

    public List<ServerCurrency> createCurrencyList(String content){
        Scanner scan = new Scanner(content);
        List<ServerCurrency> currencyList = new ArrayList<ServerCurrency>();
        String reader = "";
        while (scan.hasNextLine()) {
            reader = scan.nextLine();
            currencyList.add(new ServerCurrency(findCurrencyName(reader), findCurrencyRate(reader), date));
        }
        return currencyList;
    }

    public String findCurrencyRate(String reader){
        reader = reader.replaceAll("[A-Za-z]", "");
        return reader;
    }

    public String findCurrencyName(String reader){
        reader = reader.replaceAll("[0123456789,.]", "");
        return reader;
    }

    public boolean containsDBLineContent(String reader){
        if (reader.contains("<td class='rtRates'>") && reader.contains("from=USD")) // specifies textual markers
            return true;
        else
            return false;
    }

    public boolean containsDBDate(String reader){
        if (reader.contains("id=\"historicalDate\">")) // specifies textual markers
            return true;
        else
            return false;
    }
    public String getDBPage(String websiteURL) {
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
}
