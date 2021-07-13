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
    boolean missing = false;
    LocalDate[] localDatesList;

    public DatabaseChecker(double lower, double upper) {
        this.lowerRange = (int) (CONSTANTS.RANGE_OF_DAYS_TO_SCAN * lower);
        this.upperRange = (int) (CONSTANTS.RANGE_OF_DAYS_TO_SCAN * upper);
        this.totalRange = upperRange - lowerRange;
        this.beginDate = DATE_TODAY.minusDays(upperRange);
        this.endDate = DATE_TODAY.minusDays(lowerRange);
        this.localDatesList = new LocalDate[totalRange];
    }

    public void run() {
        int counter = 0;
        generateLocalDatesList();
        for (int i = 0; i < totalRange; i++) {

        }
        System.out.println(localDatesList[0] + " - " + localDatesList[374]);
    }

    public void generateLocalDatesList(){
        for(int i = 0; i < totalRange; i++){
            localDatesList[i] = beginDate.plusDays(i);
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