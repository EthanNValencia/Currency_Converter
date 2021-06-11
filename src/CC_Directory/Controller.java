/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/***
 * Controller class for the GUI.
 */
public class Controller implements Initializable, CONSTANTS {

    String colombianPesos = WebReader.getPage(colombianPeso);
    String convertCOPtoUSD = String.format("%.5f", 1/Double.parseDouble(colombianPesos));

    @FXML
    private Label USDtoCOP, COPtoUSD;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        USDtoCOP.setText("1 USD = " + colombianPesos + " COP");
        COPtoUSD.setText("1 COP = " + convertCOPtoUSD + " USD");
    }

}
