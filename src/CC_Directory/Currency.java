/*
Ethan J. Nephew
June 12, 2021
Currency converter and presentation application.
*/

package CC_Directory;

/***
 * Simple class for storing currency names and their rate (as compared to USD).
 */
public class Currency {

    private String name;
    private String rate;

    /***
     * Standard getter method for the currency object name variable.
     * @return Returns what is stored in the object name variable.
     */
    public String getName() {
        return name;
    }

    /***
     * Standard getter method for the currency object rate variable.
     * @return Returns what is stored in the object rate variable.
     */
    public String getRate() {
        return rate;
    }

    /***
     * Constructor method for the currency object. It requires that the name of the currency and rate.
     * @param name Requires the name of the currency (EX: USD, COP)
     * @param rate Requires the currency exchange rate as compared to USD.
     */
    public Currency(String name, String rate) {
        this.name = name;
        this.rate = rate;
    }

    /***
     * Overridden toString method. Doesn't have direct application use, but it is useful for testing purposes.
     * @return It returns the toString.
     */
    @Override
    public String toString() {
        return "Currency{" +
                "name='" + name + '\'' +
                ", rate=" + rate +
                '}';
    }
}
