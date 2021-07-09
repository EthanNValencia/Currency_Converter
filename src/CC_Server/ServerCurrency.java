/*
Ethan J. Nephew
July 1, 2021
Server currency class definition.
*/

package CC_Server;

import java.io.Serializable;
import java.util.Objects;

/***
 * This is a child class of the Currency class. It has similar behaviors and serves the same purposes of holding currency related data.
 */
public class ServerCurrency implements Serializable {

    private String name = null;
    private String date = null;
    private String description = null;
    private String rawRate = null;
    private String exchangeAmount = null;
    private String adjustedRate = null;

    /***
     * The accessor method for the variable that stores the calculated rate.
     * @return Returns the calculated rate of exchange.
     */
    public String getAdjustedRate() {
        return adjustedRate;
    }

    /***
     * This is the mutator method for the calculated rate variable.
     * @param adjustedRate Returns the contained calculated rate.
     */
    public void setAdjustedRate(String adjustedRate) {
        this.adjustedRate = adjustedRate;
    }

    /***
     * The mutator method for the description variable.
     * @param description It requires a string containing the currency description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /***
     * The accessor method for the variable that stores the amount of currency to be exchanged.
     * @return Returns the amount of currency that is to be traded.
     */
    public String getExchangeAmount() {
        return exchangeAmount;
    }

    /***
     * This is the mutator method for the variable that stores the amount of currency to be exchanged.
     * @param exchangeAmount It requires the specified amount that is to be traded.
     */
    public void setExchangeAmount(String exchangeAmount) {
        this.exchangeAmount = exchangeAmount;
    }

    /***
     * Standard accessor method that is used to return the object description.
     * @return Returns the full currency description.
     */
    public String getDescription(){
        return description;
    }

    /***
     * Standard accessor method that is used to return the object date record.
     * @return Returns the date. It should be in the format of: YYYY-MM-DD.
     */
    public String getDate() {
        return date;
    }

    /***
     * Standard mutator method. Should only be used for testing purposes.
     * @param date Requires the date. It should be in the format of: YYYY-MM-DD.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /***
     * Standard mutator method that sets the object name.
     * @param name Requires the specified name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /***
     * Standard mutator method that sets the object rate.
     * @param rawRate Requires the specified rate.
     */
    public void setRawRate(String rawRate) {
        this.rawRate = rawRate;
    }

    /***
     * The accessor method for the field that stores the name.
     * @return Returns the data that is contained in the specified variable.
     */
    public String getName() {
        return name;
    }

    /***
     * The accessor method for the field that stores the rate.
     * @return Returns the data that is contained in the specified variable.
     */
    public String getRawRate() {
        return rawRate;
    }

    /***
     * Zero parameter constructor. It is great for testing.
     */
    public ServerCurrency(){
        // No parameters required.
    }

    /***
     * Single parameter ServerCurrency constructor.
     */
    public ServerCurrency(String name){
        this.name = name;
    }

    /***
     * This is a two parameter constructor for the server currency object.
     * @param name It takes a name (this is meant to be the name of the currency).
     * @param rawRate It takes a rate (this is meant to be the exchange rate).
     */
    public ServerCurrency(String name, String rawRate) {
        this.name = name;
        this.rawRate = rawRate;
    }

    /***
     * This is a 3 parameter constructor. It requires the name, rate, and description.
     * @param name It takes a name (this is meant to be the name of the currency).
     * @param rawRate It takes a rate (this is meant to be the exchange rate).
     * @param description It takes the description of the object.
     */
    public ServerCurrency(String name, String rawRate, String description) {
        this.name = name;
        this.rawRate = rawRate;
        this.description = description;
    }

    /***
     * The ServerCurrency is a child class of the CC_Directory.Currency class. It only differs in that it has an addition date variable.
     * @param name It requires the name of the currency.
     * @param rawRate It requires the exchange rate from USD to itself.
     * @param date It requires the date on which this exchange rate was recorded.
     */
    public ServerCurrency(String name, String rawRate, String date, String description) {
        this.name = name;
        this.rawRate = rawRate;
        this.date = date;
        this.description = description;
    }

    /***
     * This is a full, 6 parameter constructor. The reason for this is to help improve readability for testing.
     * @param name It requires the name of the currency.
     * @param date It requires the date on which this exchange rate was recorded.
     * @param description It requires the description of the currency.
     * @param rawRate It requires the exchange rate from USD to itself.
     * @param exchangeAmount It requires the exchange amount.
     * @param adjustedRate It requires the adjusted rate.
     */
    public ServerCurrency(String name, String date, String description, String rawRate, String exchangeAmount, String adjustedRate) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.rawRate = rawRate;
        this.exchangeAmount = exchangeAmount;
        this.adjustedRate = adjustedRate;
    }

    /***
     * Overridden toString method that is mostly useful for testing.
     * @return It returns the object variables in a string, useful for testing purposes.
     */
    @Override
    public String toString() {
        return "ServerCurrency{name: "+ name + ", date: " + date + ", rate: " + rawRate + ", description: " + description + ", exchangeAmount: " + exchangeAmount + ", adjustedRate: " + adjustedRate + "}";
    }

    /***
     * This is the overridden equals method that is useful for comparing ServerCurrency objects with the goal of eliminating duplicates.
     * @param o It takes a generic object to be compared.
     * @return It returns whether or not the hashcode of each object is equal or not.
     */
    @Override
    public boolean equals(Object o) {
        ServerCurrency that = (ServerCurrency) o;
        return Objects.equals(hashCode(), that.hashCode());
    }

    /***
     * This is the overridden hash code method that is useful for comparing ServerCurrency objects with the goal of eliminating duplicates.
     * @return It uses the object name and date as a basis for comparison.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, date);
    }
}
