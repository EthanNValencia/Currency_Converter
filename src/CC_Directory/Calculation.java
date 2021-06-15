package CC_Directory;

import java.text.NumberFormat;

/***
 *
 */
public class Calculation {

    public String getFormatRate() {
        return formatRate;
    }

    private String formatRate;

    public String getInputConversion() {
        return inputConversion;
    }

    private String inputConversion;

    public Calculation(Currency currency1, Currency currency2){
        double rate = (double) 1 / Double.parseDouble(currency1.getRate());
        rate = rate * Double.parseDouble(currency2.getRate());
        formatRate = String.format("%.5f", rate);
    }

    public void convertCurrency(double input){
        NumberFormat nf = NumberFormat.getInstance();
        this.inputConversion = nf.format(input * Double.parseDouble(formatRate));
    }

}
