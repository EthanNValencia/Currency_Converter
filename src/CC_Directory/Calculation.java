/*
Ethan J. Nephew
June 15, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import java.text.NumberFormat;

/***
 * This class is derived from embedding logics from the controller class into a single class definition that is testable.
 */
public class Calculation {

    private String formatRate;
    private String inputConversion;

    /***
     * Standard getter method that returns the converted currency of the inputted numerics.
     * @return Returns the converted input.
     */
    public String getInputConversion() {
        return inputConversion;
    }

    /***
     * Standard getter method that returns the formatted conversion rate.
     * @return Returns the formatted rate.
     */
    public String getFormatRate() {
        return formatRate;
    }

    /***
     * This is the constructor method that uses two currency objects as parameters and uses them to derive conversion rates.
     * @param currency1 This is the first currency object.
     * @param currency2 This is the second currency object.
     */
    public Calculation(Currency currency1, Currency currency2){
        double rate = (double) 1 / Double.parseDouble(currency1.getRate());
        rate = rate * Double.parseDouble(currency2.getRate());
        formatRate = String.format("%.5f", rate);
    }

    /***
     * This method uses the inputted numerical value and the formatted rate to calculate the currency conversion.
     * @param input Requires a numerical value as a parameter.
     */
    public void convertCurrency(double input){
        NumberFormat nf = NumberFormat.getInstance();
        this.inputConversion = nf.format(input * Double.parseDouble(formatRate));
    }

}