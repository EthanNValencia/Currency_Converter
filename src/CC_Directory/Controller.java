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
    private Label output;

    @FXML
    private ComboBox<String> comboBox1, comboBox2;


    public void generateArrayList(){
        arrayList.add(new Currency("USD", "1"));
        arrayList.add(new Currency("COP", WebReader.getPage(CONSTANTS.COP)));
        arrayList.add(new Currency("EUR", WebReader.getPage(CONSTANTS.EUR)));
        arrayList.add(new Currency("MXN", WebReader.getPage(CONSTANTS.MXN)));
    }

    public void getRate(Currency currency1, Currency currency2){

        if (currency1.getName().equals("USD") || currency1.getName().equals("USD") && currency2.getName().equals("USD")){
            output.setText(currency1.getRate() + " " + currency1.getName() + " = " + currency2.getRate() + " " + currency2.getName());
        } else if(!(currency1.getName().equals("USD") || currency1.getName().equals("USD") && currency2.getName().equals("USD")) && !(currency1.getName().equals(currency2.getName()))){
            double rate = (double) 1 / Double.parseDouble(currency1.getRate());
            rate = rate * Double.parseDouble(currency2.getRate());
            String formatRate = String.format("%.5f", rate);
            output.setText("1 " + currency1.getName() + " = " + formatRate + " " + currency2.getName());
        } else if (currency1.getName().equals(currency2.getName())){
            output.setText("1 " + currency1.getName() + " = " + "1 " + currency2.getName());
        }

        //String Z = String.format("%.5f", Double.parseDouble(currency1.getRate())/Double.parseDouble(currency2.getRate()));
        //return Z;
    }

    /***
     *
     * @param comboBox1
     * @param comboBox2
     */
    public void getString(String comboBox1, String comboBox2){
        Currency comboBox1Currency = null;
        Currency comboBox2Currency = null;

        for(int i = 0; i < arrayList.size(); i++){
            if(arrayList.get(i).getName().equals(comboBox1)){
                comboBox1Currency = new Currency(arrayList.get(i).getName(), arrayList.get(i).getRate());
            }
            if(arrayList.get(i).getName().equals(comboBox2)){
                comboBox2Currency = new Currency(arrayList.get(i).getName(), arrayList.get(i).getRate());
            }
        }
        getRate(comboBox1Currency, comboBox2Currency);
        //System.out.println(comboBox1Currency + " " + comboBox2Currency);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
