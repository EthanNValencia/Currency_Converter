/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/***
 * Controller class for the GUI. It contains some basic filtering logics for calculating currency conversions.
 */
public class Controller implements Initializable, CONSTANTS {

    private ArrayList<Currency> arrayList = new ArrayList();
    private Currency comboBox1Currency = null;
    private Currency comboBox2Currency = null;
    private Calculation calculateObj;

    @FXML
    private Label outputRate, conversionIndicator, currencyExchange;

    @FXML
    private ComboBox<String> comboBox1, comboBox2;

    @FXML
    private TextField inputArea;

    /***
     * This is a simple method that generates the currency objects and stores them into an ArrayList.
     */
    public void generateArrayList(){

        arrayList.add(new Currency("USD", "1"));
        arrayList.add(new Currency("COP", WebReader.getPage(CONSTANTS.COP)));
        arrayList.add(new Currency("EUR", WebReader.getPage(CONSTANTS.EUR)));
        arrayList.add(new Currency("MXN", WebReader.getPage(CONSTANTS.MXN)));
        arrayList.add(new Currency("JPY", WebReader.getPage(CONSTANTS.JPY)));
        arrayList.add(new Currency("VES", WebReader.getPage(CONSTANTS.VES)));
        arrayList.add(new Currency("GBP", WebReader.getPage(CONSTANTS.GBP)));
        arrayList.add(new Currency("PHP", WebReader.getPage(CONSTANTS.PHP)));
        arrayList.add(new Currency("RUB", WebReader.getPage(CONSTANTS.RUB)));

    }

    /***
     * Method that determines what the currency rate is and displays the rate to the GUI.
     * @param currency1 The first currency object that is selected from the first combobox.
     * @param currency2 The second currency object that is selected from the second combobox
     */
    public void getRate(Currency currency1, Currency currency2){

        currencyExchange.setText("");
        calculateObj = new Calculation(currency1, currency2);
        outputRate.setText(calculateObj.getDisplayRate1() + " " + currency1.getName() + " = " + calculateObj.getDisplayRate2() + " " + currency2.getName());
        conversionIndicator.setText("Converting " + currency1.getName() + " to " + currency2.getName());

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

    /***
     * This method is used with the submit button. It pulls text from the text area and calculates the currency conversion.
     */
    public void submit() {

        String input = inputArea.getText();
        double amount;

        try {
            amount = Double.parseDouble(input);
            calculateObj.convertCurrency(amount);
            if (comboBox1Currency == null && comboBox2Currency == null) {
                conversionIndicator.setText("Please select your currencies.");
            } else if (!(comboBox1Currency == null) && !(comboBox2Currency == null)) {
                currencyExchange.setText(amount + " " + comboBox1Currency.getName() + " = " + calculateObj.getInputConversion() + " " + comboBox2Currency.getName());
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

        /*
        // I found this online? Not sure if it works. Will need to do some testing and studying. 
        comboBox1.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> p) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon;
                            try {
                                int iconNumber = this.getIndex() + 1;
                                String iconPath = "Currency_Converter/src/CC_Images/Chinese_Flag.png";
                                icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                            } catch(NullPointerException ex) {
                                // in case the above image doesn't exist, use a default one
                                String iconPath = "MyProject/resources/images/icon_na.png";
                                icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(30);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        });
        */

        outputRate.setText("");
        conversionIndicator.setText("");
        currencyExchange.setText("");
        generateArrayList();

        for (int i = 0; i < CONSTANTS.CURRENCYNAMES.length; i++){
            //comboBox1.getItems().add(CONSTANTS.CURRENCYNAMES[i]);
            comboBox2.getItems().add(CONSTANTS.CURRENCYNAMES[i]);
        }
        comboBox1.getSelectionModel().selectFirst();
        comboBox2.getSelectionModel().selectFirst();
        comboBox1.setOnAction(e -> getString(comboBox1.getValue().toString(), comboBox2.getValue().toString()));
        comboBox2.setOnAction(e -> getString(comboBox1.getValue().toString(), comboBox2.getValue().toString()));

    }

}
