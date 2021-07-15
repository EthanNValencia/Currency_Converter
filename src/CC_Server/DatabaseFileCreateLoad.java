/*
Ethan J. Nephew
July 12, 2021
Thread object that will be used generate text files for the calc_cur table and input them into the database.
*/

package CC_Server;

import java.io.IOException;
import java.util.Arrays;

public class DatabaseFileCreateLoad extends Thread{

    private String[] loadFileNameArray;
    private String[] fullNameArray;


    @Override
    public String toString() {
        return "DatabaseFileCreateLoad{" +
                "loadFileNameArray=" + Arrays.toString(loadFileNameArray) +
                ", fullNameArray=" + Arrays.toString(fullNameArray) +
                '}';
    }


    public DatabaseFileCreateLoad(String[] fullNameArray, int loadNameArrayLow, int loadNameArrayHigh) {
        this.fullNameArray = fullNameArray;
        this.loadFileNameArray = new String[loadNameArrayHigh - loadNameArrayLow];
        for (int i = 0; i < loadFileNameArray.length; i++){
            loadFileNameArray[i] = fullNameArray[loadNameArrayLow + i];
        }
    }


    public void run(){
        try {
            Connect.createFiles(loadFileNameArray);
            // String[] currencyDescr = Connect.getCurrencyDescriptionArray();
            String[] dateArray = Connect.getDates();
            String[] rateArray; // COP and USD
            for (int i = 0; i < loadFileNameArray.length; i++) {
                for (int j = 0; j < fullNameArray.length; j++) {
                    rateArray = Connect.getRates(loadFileNameArray[i], fullNameArray[j]); // gets annual rate of every combination
                    Connect.writeToFile(loadFileNameArray[i], fullNameArray[j], rateArray, dateArray);
                }
            }
            Connect.readOnlyFiles(loadFileNameArray);
        } catch (IOException ioe){
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
