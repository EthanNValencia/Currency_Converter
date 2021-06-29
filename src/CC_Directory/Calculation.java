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

    NumberFormat nf = NumberFormat.getInstance();
    private String formatRate;
    private String inputConversion;
    private String displayRate1, displayRate2;

    /***
     * Standard getter method for the rate 1 variable.
     * @return Returns what is stored in rate 1.
     */
    public String getDisplayRate1() {
        return displayRate1;
    }

    /***
     * Standard getter method for the rate 2 variable.
     * @return Returns what is stored in rate 2.
     */
    public String getDisplayRate2() {
        return displayRate2;
    }

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
        formatRate = String.format("%.2f", rate);
        if(formatRate.equals("0.00"))
            formatRate = String.format("%.7f", rate);
        displayRate1 = "1";
        displayRate2 = formatRate;

    }

    /***
     * This method uses the inputted numerical value and the formatted rate to calculate the currency conversion.
     * @param input Requires a numerical value as a parameter.
     */
    public void convertCurrency(double input){

        nf.setMinimumFractionDigits(2);
        System.out.println(Double.parseDouble(formatRate) * input);
        inputConversion = nf.format(input * Double.parseDouble(formatRate));
        checkLower(input);

    }

    /***
     * Method that verifies that a 0 isn't be returned as the exchange number. If certain countries experience higher rates of inflation this may need to be adjusted.
     * @param input Takes the input that is passed into the method stack from the input area in the GUI.
     */
    public void checkLower(double input){

        if(inputConversion.equals("0.00")){
            nf.setMinimumFractionDigits(7);
            inputConversion = nf.format(input * Double.parseDouble(formatRate));
        }

    }
}