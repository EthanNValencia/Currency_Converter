/*
Ethan J. Nephew
July 9, 2021
Currency data object that is utilized during transfer requests.
*/
package CC_Server;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/***
 * The idea behind this class is for it to be a data packet that is sent between the client and server.
 */
public class CurrencyDataObject implements Serializable {

    private ServerCurrency currency1 = new ServerCurrency(); // combobox1
    private ServerCurrency currency2 = new ServerCurrency(); // combobox2
    private List<ServerCurrency> serverCurrencyList;
    private boolean getHistoricalList = false, getRateOfChangeList = false;

    public boolean getRateOfChangeList(){
        return getRateOfChangeList;
    }

    public boolean getHistoricalList(){
        return getHistoricalList;
    }

    public void setHistoricalList(Boolean getList){
        this.getHistoricalList = getList;
    }

    public List<ServerCurrency> getServerCurrencyList(){
        return serverCurrencyList;
    }

    public void setServerCurrencyList(List<ServerCurrency> list){
        this.serverCurrencyList = list;
    }

    /***
     * This is the accessor method for the first server currency object.
     * @return It returns the first server currency object that the instance contains.
     */
    public ServerCurrency getCurrency1() {
        return currency1;
    }

    /***
     * This is the mutator method for the first currency object.
     * @param currency1 It requires a ServerCurrency object that will be assigned to the instance.
     */
    public void setCurrency1(ServerCurrency currency1) {
        this.currency1 = currency1;
    }

    /***
     * This is the accessor method for the second server currency object.
     * @return It returns the second server currency object that the instance contains.
     */
    public ServerCurrency getCurrency2() {
        return currency2;
    }

    /***
     * This is a mutator method for the second server currency object.
     * @param currency2 It requires a ServerCurrency object that will be assigned to the instance.
     */
    public void setCurrency2(ServerCurrency currency2) {
        this.currency2 = currency2;
    }

    /***
     * A constructor for the CurrencyDataObject.
     * @param cur1 It requires the currency data taken from combo box 1.
     * @param cur2 It requires the currency data taken from combo box 2.
     * @param localDate It requires the date in the LocalDate format (not a string).
     */
    public CurrencyDataObject(ServerCurrency cur1, ServerCurrency cur2, LocalDate localDate) {
        this.currency1 = cur1;
        this.currency2 = cur2;
        this.currency1.setDate(localDate.toString());
        this.currency2.setDate(localDate.toString());
    }

    /***
     * A constructor for the CurrencyDataObject.
     * @param cur1 It requires the currency data taken from combo box 1.
     * @param localDate It requires the date in the LocalDate format (not a string).
     */
    public CurrencyDataObject(ServerCurrency cur1, ServerCurrency cur2, LocalDate localDate, boolean getHistoricalList, boolean getRateOfChangeList) {
        this.currency1 = cur1;
        this.currency2 = cur2;
        this.currency1.setDate(localDate.toString());
        this.getHistoricalList = getHistoricalList;
        this.getRateOfChangeList = getRateOfChangeList;
    }

    public CurrencyDataObject() {

    }

    /***
     * Overridden toString method for the CurrencyDataObject.
     * @return It returns the text that contains basic object information.
     */
    @Override
    public String toString() {
        return "CurrencyDataObject{" +
                "currency1=" + currency1 +
                ", currency2=" + currency2 +
                ", serverCurrencyList=" + serverCurrencyList +
                ", getHistoricalList=" + getHistoricalList +
                ", getRateOfChangeList=" + getRateOfChangeList +
                '}';
    }


}
