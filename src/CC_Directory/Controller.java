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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/***
 * Controller class for the GUI.
 */
public class Controller implements Initializable, CONSTANTS {

    ArrayList<Currency> arrayList = new ArrayList();

    @FXML
    private Label USDtoCOP, COPtoUSD, USDtoEuro;

    @FXML
    private ComboBox comboBox1, comboBox2;


    public void generateArrayList(){
        arrayList.add(new Currency("USD", 1));
        arrayList.add(new Currency("COP", WebReader.getPage(CONSTANTS.colombianPeso)));
        arrayList.add(new Currency("EUR", WebReader.getPage(CONSTANTS.euro)));
    }

    /***
     * A method that takes the string values of two currencies and returns the ratio.
     * @param X This is the numerator.
     * @param Y This is the denominator.
     * @return It returns the ratio to the fifth decimal.
     */
    public String getRateIfNotUSD(String X, String Y){
        double rate = 1/Double.parseDouble(X);
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
        
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBox1.getItems().addAll("COP", "EUR", "USD");
        comboBox2.getItems().addAll("COP", "EUR", "USD");
        comboBox1.getSelectionModel().selectFirst();
        comboBox2.getSelectionModel().selectFirst();
        comboBox1.setOnAction(e -> getString(comboBox1.getValue().toString(), comboBox2.getValue().toString()));
        comboBox2.setOnAction(e -> getString(comboBox1.getValue().toString(), comboBox2.getValue().toString()));
    }
}
