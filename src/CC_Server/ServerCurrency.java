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

    private String date;
    private String description;
    private String name;
    private String rate;
    private String exchangeAmount;

    /***
     * The mutator method for the description variable.
     * @param description It requires a string containing the currency description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /***
     * The accessor method for the variable that stores the amount of currency to be exchanged.
     * @return It returns the amount of currency that is to be traded.
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
     * @return It returns the date. It should be in the format of: YYYY-MM-DD.
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
     * @param rate Requires the specified rate.
     */
    public void setRate(String rate) {
        this.rate = rate;
    }

    /***
     * The accessor method for the field that stores the name.
     * @return It returns the data that is contained in the specified variable.
     */
    public String getName() {
        return name;
    }

    /***
     * The accessor method for the field that stores the rate.
     * @return It returns the data that is contained in the specified variable.
     */
    public String getRate() {
        return rate;
    }

    /***
     * Zero parameter constructor. It is great for testing.
     */
    public ServerCurrency(){
        // No parameters required.
    }

    /***
     * This is a two parameter constructor for the server currency object.
     * @param name It takes a name (this is meant to be the name of the currency).
     * @param rate It takes a rate (this is meant to be the exchange rate).
     */
    public ServerCurrency(String name, String rate) {
        this.name = name;
        this.rate = rate;
    }

    /***
     * This is a 3 parameter constructor. It requires the name, rate, and description.
     * @param name It takes a name (this is meant to be the name of the currency).
     * @param rate It takes a rate (this is meant to be the exchange rate).
     * @param description It takes the description of the object.
     */
    public ServerCurrency(String name, String rate, String description) {
        this.name = name;
        this.rate = rate;
        this.description = description;
    }

    /***
     * The ServerCurrency is a child class of the CC_Directory.Currency class. It only differs in that it has an addition date variable.
     * @param name It requires the name of the currency.
     * @param rate It requires the exchange rate from USD to itself.
     * @param date It requires the date on which this exchange rate was recorded.
     */
    public ServerCurrency(String name, String rate, String date, String description) {
        this.name = name;
        this.rate = rate;
        this.date = date;
        this.description = description;
    }

    /***
     * Overridden toString method that is mostly useful for testing.
     * @return It returns the object variables in a string, useful for testing purposes.
     */
    @Override
    public String toString() {
        return "ServerCurrency{"+ name + " " + date + " " + rate + " " + description + " " + exchangeAmount + "}";
    }

    /***
     * This is the overridden equals method that is useful for comparing ServerCurrency objects with the goal of eliminating duplicates.
     * @param o It takes a rather generic object to be compared.
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
