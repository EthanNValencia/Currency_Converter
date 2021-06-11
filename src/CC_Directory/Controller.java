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

    @FXML
    private Label USDtoCOP, COPtoUSD, USDtoEuro;

    @FXML
    private ComboBox comboBox1, comboBox2;

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

    public void getString(String comboBox1, String comboBox2){
        
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        USDtoCOP.setText("1 USD = " + COP + " COP");
        COPtoUSD.setText("1 COP = " + convertCOPtoUSD + " USD");
        USDtoEuro.setText("1 USD = " + EUR + " EUR");
        comboBox1.getItems().addAll("COP", "EUR", "USD");
        comboBox2.getItems().addAll("COP", "EUR", "USD");
        comboBox1.getSelectionModel().selectFirst();
        comboBox2.getSelectionModel().selectFirst();

        comboBox1.setOnAction(e -> System.out.println(comboBox1.getValue() + " " + comboBox2.getValue()));
        comboBox2.setOnAction(e -> System.out.println(comboBox1.getValue() + " " + comboBox2.getValue()));
    }



}
