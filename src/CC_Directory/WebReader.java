/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/***
 * WebReader class that pulls simple data from websites.
 */
public class WebReader implements CONSTANTS {

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

}
