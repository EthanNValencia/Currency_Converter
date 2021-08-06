package CC_Server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/***
 * This is meant to be a sort of test driven development location for features that I have
 * not entirely determined how or where to implement.
 */
public class ProductionTests {
    /***
     * This demonstrates the functionality of the DatabaseFileCreateLoad multithreading. This test
     * will likely take around 10 minutes to run if generation of 2+ years of data is required.
     * @throws Exception A lot can go wrong here.
     */
    @Test
    public void testDatabaseFileCreateLoad_CONSTRUCTOR() throws Exception {
        String[] fullList = Connect.getCurrencyNameArray();
        try {
            DatabaseFileCreateLoad dfcl1 = new DatabaseFileCreateLoad(fullList, 0, 13);
            DatabaseFileCreateLoad dfcl2 = new DatabaseFileCreateLoad(fullList, 13, 26);
            DatabaseFileCreateLoad dfcl3 = new DatabaseFileCreateLoad(fullList, 26, 39);
            DatabaseFileCreateLoad dfcl4 = new DatabaseFileCreateLoad(fullList, 39, 54);
            dfcl1.start(); dfcl2.start(); dfcl3.start(); dfcl4.start();
            dfcl1.join(); dfcl2.join(); dfcl3.join(); dfcl4.join();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /***
     * This is used to determine if data is missing from the database.
     * @throws InterruptedException
     */
    @Test
    public void testDatabaseChecker_Run() throws InterruptedException {
        DatabaseChecker dbc1 = new DatabaseChecker(0, 0.25);
        DatabaseChecker dbc2 = new DatabaseChecker(0.25, 0.50);
        DatabaseChecker dbc3 = new DatabaseChecker(0.50, 0.75);
        DatabaseChecker dbc4 = new DatabaseChecker(0.75, 1);
        // System.out.println(dbc1);
        // System.out.println(dbc2);
        // System.out.println(dbc3);
        // System.out.println(dbc4);
        dbc1.setDaemon(true);
        dbc2.setDaemon(true);
        dbc3.setDaemon(true);
        dbc4.setDaemon(true);
        dbc1.start(); dbc2.start(); dbc3.start(); dbc4.start();
        dbc1.join(); dbc2.join(); dbc3.join(); dbc4.join();
    }
}
