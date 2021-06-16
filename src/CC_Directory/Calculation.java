package CC_Directory;

import java.text.NumberFormat;

/***
 *
 */
public class Calculation {

    private String formatRate;
    private String inputConversion;

    /***
     *
     * @return
     */
    public String getInputConversion() {
        return inputConversion;
    }

    /***
     *
     * @return
     */
    public String getFormatRate() {
        return formatRate;
    }

    /***
     *
     * @param currency1
     * @param currency2
     */
    public Calculation(Currency currency1, Currency currency2){
        double rate = (double) 1 / Double.parseDouble(currency1.getRate());
        rate = rate * Double.parseDouble(currency2.getRate());
        formatRate = String.format("%.5f", rate);
    }

    /***
     *
     * @param input
     */
    public void convertCurrency(double input){
        NumberFormat nf = NumberFormat.getInstance();
        this.inputConversion = nf.format(input * Double.parseDouble(formatRate));
    }

}
