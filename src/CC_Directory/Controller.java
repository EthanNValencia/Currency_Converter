/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

/***
 * Controller class for the GUI. It contains some basic filtering logics for calculating currency conversions.
 */
public class Controller implements Initializable, CONSTANTS {

    private ArrayList<Currency> arrayList = new ArrayList();
    private Currency comboBox1Currency = null;
    private Currency comboBox2Currency = null;
    private String formatRate;
    private double rate;


    @FXML
    private Label outputRate, conversionIndicator, currencyExchange;

    @FXML
    private ComboBox<String> comboBox1, comboBox2;

    @FXML
    private TextField inputArea;

    @FXML
    private Button submit;

    /***
     * This is a simple method that generates the currency objects and stores them into an ArrayList.
     */
    public void generateArrayList(){
        arrayList.add(new Currency("USD", "1"));
        arrayList.add(new Currency("COP", WebReader.getPage(CONSTANTS.COP)));
        arrayList.add(new Currency("EUR", WebReader.getPage(CONSTANTS.EUR)));
        arrayList.add(new Currency("MXN", WebReader.getPage(CONSTANTS.MXN)));
    }

    /***
     * Method that determines what the currency rate is and displays the rate to the GUI.
     * @param currency1 The first currency object that is selected from the first combobox.
     * @param currency2 The second currency object that is selected from the second combobox
     */
    public void getRate(Currency currency1, Currency currency2){

        if (currency1.getName().equals("USD") || currency1.getName().equals("USD") && currency2.getName().equals("USD")){
            outputRate.setText(currency1.getRate() + " " + currency1.getName() + " = " + currency2.getRate() + " " + currency2.getName());
            conversionIndicator.setText("Converting " + currency1.getName() + " to " + currency2.getName());
            rate = Double.parseDouble(currency2.getRate());
        } else if(!(currency1.getName().equals("USD") || currency1.getName().equals("USD") && currency2.getName().equals("USD")) && !(currency1.getName().equals(currency2.getName()))){
            rate = (double) 1 / Double.parseDouble(currency1.getRate());
            rate = rate * Double.parseDouble(currency2.getRate());
            formatRate = String.format("%.5f", rate);
            outputRate.setText("1 " + currency1.getName() + " = " + formatRate + " " + currency2.getName());
            conversionIndicator.setText("Converting " + currency1.getName() + " to " + currency2.getName());
        } else if (currency1.getName().equals(currency2.getName())){
            outputRate.setText("1 " + currency1.getName() + " = " + "1 " + currency2.getName());
            conversionIndicator.setText("Converting " + currency1.getName() + " to " + currency2.getName());
            rate = 1;
        }

        //String Z = String.format("%.5f", Double.parseDouble(currency1.getRate())/Double.parseDouble(currency2.getRate()));
        //return Z;
    }

    /***
     * This method correlates the ComboBox names to stored currency objects.
     * @param comboBox1 This is the the string parameter from the first combobox.
     * @param comboBox2 This is the the string parameter from the second combobox.
     */
    public void getString(String comboBox1, String comboBox2){

        for(int i = 0; i < arrayList.size(); i++){
            if(arrayList.get(i).getName().equals(comboBox1)){
                comboBox1Currency = new Currency(arrayList.get(i).getName(), arrayList.get(i).getRate());
            }
            if(arrayList.get(i).getName().equals(comboBox2)){
                comboBox2Currency = new Currency(arrayList.get(i).getName(), arrayList.get(i).getRate());
            }
        }

        try {
            getRate(comboBox1Currency, comboBox2Currency);
        } catch (NullPointerException npe){
            System.out.println("A NullPointerException occurred. There was likely a problem pulling necessary data from the internet.");
        }
    }

    public void submit() {
        String input = inputArea.getText();
        String formatInput;
        double amount;
        try {
            amount = Double.parseDouble(input);
            NumberFormat nf = NumberFormat.getInstance();
            formatInput = nf.format(amount*rate);
            System.out.println(formatInput);
            if (comboBox1Currency == null && comboBox2Currency == null) {
                conversionIndicator.setText("Please select your currencies.");
            } else if (!(comboBox1Currency == null) && !(comboBox2Currency == null)) {
                currencyExchange.setText(amount + " " + comboBox1Currency.getName() + " = " + formatInput + " " + comboBox2Currency.getName());
            }
        } catch (RuntimeException re) {
            conversionIndicator.setText("Please enter a numeric.");
            currencyExchange.setText("Your input must be a simple number. EX: 500");
        }
    }

    /***
     * Overridden initialize method for the GUI.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        outputRate.setText("");
        conversionIndicator.setText("");
        currencyExchange.setText("");
        generateArrayList();
        for (int i = 0; i < CURRENCYNAMES.length; i++){
            comboBox1.getItems().add(CURRENCYNAMES[i]);
            comboBox2.getItems().add(CURRENCYNAMES[i]);
        }
        comboBox1.getSelectionModel().selectFirst();
        comboBox2.getSelectionModel().selectFirst();
        comboBox1.setOnAction(e -> getString(comboBox1.getValue().toString(), comboBox2.getValue().toString()));
        comboBox2.setOnAction(e -> getString(comboBox1.getValue().toString(), comboBox2.getValue().toString()));
    }

}
