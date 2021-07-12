/*
Ethan J. Nephew
July 12, 2021
Thread object that will be used to both verify database integrity and to generate and input missing data.
*/
package CC_Server;

import java.time.LocalDate;
import java.util.HashSet;

public class DatabaseChecker extends Thread implements CONSTANTS {
    int lowerRange, upperRange, totalRange;
    LocalDate beginDate, endDate;

    public DatabaseChecker(int lower, int upper) {
        this.lowerRange = lower;
        this.upperRange = upper;
        this.totalRange = upperRange - lowerRange;
        this.beginDate = DATE_TODAY.minusDays(lowerRange);
        this.endDate = DATE_TODAY.minusDays(upperRange);
    }

    public void run() {
        String insertDate;
        ServerWebReader serverWebReader = new ServerWebReader();
        HashSet<ServerCurrency> currencyList = null;
        for (int i = 0; i <= totalRange; i++) {
            insertDate = "" + beginDate.minusDays(i);
            try {
                if (!Connect.checkEntries(insertDate)) { // If entries with this date already exist, then cancel the insertion.
                    currencyList = serverWebReader.getPage(WEBSITE_URL + insertDate);
                    if (currencyList != null) {
                        try {
                            Connect.insertCurrencyDate(insertDate);
                            Connect.insertCurrencyNames(currencyList);
                            Connect.insertList(currencyList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "DatabaseChecker{" +
                "lowerRange=" + lowerRange +
                ", upperRange=" + upperRange +
                ", totalRange=" + totalRange +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                '}';
    }
}