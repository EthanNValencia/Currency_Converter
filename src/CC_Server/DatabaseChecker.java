/*
Ethan J. Nephew
July 12, 2021
Thread object that will be used to both verify database integrity and generate and input missing data.
*/
package CC_Server;

import java.time.LocalDate;

/***
 * This is a Thread that is meant to be used to verify that the database contains the data that it should contain.
 */
public class DatabaseChecker extends Thread {
    int lowerRange, upperRange, totalRange;
    LocalDate beginDate, endDate;
    boolean missing = false, test = false;
    LocalDate[] localDatesList;

    /***
     * Accessor method for the array of LocalDates.
     * @return It returns the array of LocalDates.
     */
    public LocalDate[] getLocalDatesList() {
        return localDatesList;
    }

    /***
     * This is the constructor for the DatabaseChecker.
     * @param lower The lower bound of the date range.
     * @param upper The upper bound of the date range.
     */
    public DatabaseChecker(double lower, double upper) {
        this.lowerRange = (int) (CONSTANTS.DAYS_TO_SCAN * lower);
        this.upperRange = (int) (CONSTANTS.DAYS_TO_SCAN * upper);
        this.totalRange = upperRange - lowerRange;
        this.beginDate = LocalDate.now().minusDays(upperRange);
        this.endDate = LocalDate.now().minusDays(lowerRange);
        this.localDatesList = new LocalDate[totalRange];
        generateLocalDatesList();
    }

    /***
     * This constructor is for testing purposes.
     * @param lower The lower bound of the date range.
     * @param upper The upper bound of the date range.
     * @param range The specified range. This is useful for testing purposes.
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

    /***
     * This method is used as the center point for the operation of the class.
     */
    public void run() {
        int checkEntries = checkEntries();
        int predictedRange = 2916*251;
        if(checkEntries != predictedRange){
            System.out.println("{checkEntries:" + checkEntries + "} {totalRange:" + totalRange + "} {dates:" + beginDate + " - " + endDate + "}");
            // find those entries and put them in the database.
        }

       // System.out.println(localDatesList[0] + " - " + localDatesList[localDatesList.length - 1]);
    }

    /***
     * This generates an array of LocalDates. It uses the begin date as a start point and the total range as the end point.
     */
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

    /***
     * Method that converts the contents of the DatabaseChecker object into a string for output purposes.
     * @return The string that contains the variable data contents.
     */
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