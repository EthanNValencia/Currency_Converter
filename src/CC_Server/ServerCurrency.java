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
    protected String name;
    protected String rate;
    private String nationName;

    /***
     * Standard accessor method that is used to return the currency objects description.
     * @return Returns the full currency description.
     */
    public String getDescription(){
        return description;
    }

    /***
     * Standard accessor method that is used to return the currency objects date record.
     * @return It returns the date. It should be in the format of: YYYY-MM-DD.
     */
    public String getDate() {
        return date;
    }

    /***
     * Standard mutator method that should only be used for testing purposes.
     * @param date Requires the date. It should be in the format of: YYYY-MM-DD.
     */
    public void setDate(String date) {
        this.date = date;
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

    public String getNationName() {
        return getNationName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }

    public String getName() {
        return name;
    }

    public String getRate() {
        return rate;
    }

    public ServerCurrency(String name, String rate, String nationName) {
        this.name = name;
        this.rate = rate;
        this.nationName = nationName;
    }

    public ServerCurrency(){

    }

    public ServerCurrency(String name, String rate) {
        this.name = name;
        this.rate = rate;
    }

    /***
     * Overridden toString method that is mostly useful for testing.
     * @return It returns the object variables in a string, useful for testing purposes.
     */
    @Override
    public String toString() {
        return "ServerCurrency{"+ name + " " + date + " " + rate + " " + description + "}";
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
