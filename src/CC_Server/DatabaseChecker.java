/*
Ethan J. Nephew
July 12, 2021
Thread object that will be used to both verify database integrity and to generate and input missing data.
*/
package CC_Server;

import java.time.LocalDate;

public class DatabaseChecker extends Thread implements CONSTANTS {
    int lowerRange, upperRange, totalRange;
    LocalDate beginDate, endDate;
    boolean missing = false, test = false;
    LocalDate[] localDatesList;

    public LocalDate[] getLocalDatesList() {
        return localDatesList;
    }

    public DatabaseChecker(double lower, double upper) {
        this.lowerRange = (int) (CONSTANTS.RANGE_OF_DAYS_TO_SCAN * lower);
        this.upperRange = (int) (CONSTANTS.RANGE_OF_DAYS_TO_SCAN * upper);
        this.totalRange = upperRange - lowerRange;
        this.beginDate = DATE_TODAY.minusDays(upperRange);
        this.endDate = DATE_TODAY.minusDays(lowerRange);
        this.localDatesList = new LocalDate[totalRange];
        generateLocalDatesList();
    }

    /***
     * This constructor is for testing purposes.
     * @param lower
     * @param upper
     * @param range This is specifying the range. This is useful for testing purposes.
     */
    public DatabaseChecker(double lower, double upper, double range) {
        this.lowerRange = (int) (range * lower);
        this.upperRange = (int) (range * upper);
        LocalDate testDate = LocalDate.of(2021, 7, 14);
        this.totalRange = upperRange - lowerRange;
        this.beginDate = testDate.minusDays(upperRange);
        this.endDate = testDate.minusDays(lowerRange);
        this.localDatesList = new LocalDate[totalRange];
        generateLocalDatesList();
    }

    public void run() {

        int checkEntries = checkEntries();

        if(checkEntries < totalRange){
            // find those entries and put them in the database.
        }

        System.out.println(localDatesList[0] + " - " + localDatesList[localDatesList.length - 1]);
    }

    public void generateLocalDatesList(){
        for(int i = 0; i < totalRange; i++){
            localDatesList[i] = beginDate.plusDays(i);
        }
    }

    /***
     * This method checks the amount of entries are contained in the database within the specified date range.
     * @return It returns the number of entries in the database.
     */
    public int checkEntries(){
        try {
            return Connect.countCalculationTableEntries(beginDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
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