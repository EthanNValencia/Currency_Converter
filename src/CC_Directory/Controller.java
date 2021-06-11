/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/***
 * Controller class for the GUI.
 */
public class Controller implements Initializable, CONSTANTS {

    String USD = "1";
    String COP = WebReader.getPage(CONSTANTS.colombianPeso);
    String EUR = WebReader.getPage(CONSTANTS.euro);
    String convertCOPtoUSD = String.format("%.5f", 1/Double.parseDouble(COP));
    String convertEURtoUSD = "";
    HashMap<String, String> currencyHashMap = new HashMap<>();

    @FXML
    private Label USDtoCOP, COPtoUSD, USDtoEuro;

    @FXML
    private ComboBox comboBox1, comboBox2;


    public void generateHashMap(){
        currencyHashMap.put("USD", "1");
        currencyHashMap.put("COP", WebReader.getPage(CONSTANTS.colombianPeso));
        currencyHashMap.put("EUR", WebReader.getPage(CONSTANTS.euro));
    }

    /***
     * A method that takes the string values of two currencies and returns the ratio.
     * @param X This is the numerator.
     * @param Y This is the denominator.
     * @return It returns the ratio to the fifth decimal.
     */
    public String determineRateXtoY(String X, String Y){
        String Z = String.format("%.5f", Double.parseDouble(X)/Double.parseDouble(Y));
        return Z;
    }

    /***
     *
     * @param comboBox1
     * @param comboBox2
     */
    public void getString(String comboBox1, String comboBox2){
        String output = "";
        if(currencyHashMap.containsKey(comboBox1) && currencyHashMap.containsKey(comboBox2)){ // this should always be the case.
            String comboVal1 = currencyHashMap.get(comboBox1);
            String comboVal2 = currencyHashMap.get(comboBox2);
            System.out.println(comboBox1 + " " + comboVal1 + " " + comboBox2 + " " + comboVal2);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateHashMap();
        USDtoCOP.setText("1 USD = " + COP + " COP");
        COPtoUSD.setText("1 COP = " + convertCOPtoUSD + " USD");
        USDtoEuro.setText("1 USD = " + EUR + " EUR");
        comboBox1.getItems().addAll("COP", "EUR", "USD");
        comboBox2.getItems().addAll("COP", "EUR", "USD");
        comboBox1.getSelectionModel().selectFirst();
        comboBox2.getSelectionModel().selectFirst();
        comboBox1.setOnAction(e -> getString(comboBox1.getValue().toString(), comboBox2.getValue().toString()));
        comboBox2.setOnAction(e -> getString(comboBox1.getValue().toString(), comboBox2.getValue().toString()));
    }



}
