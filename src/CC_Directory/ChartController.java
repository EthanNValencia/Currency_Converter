package CC_Directory;

import CC_Server.CurrencyDataObject;
import CC_Server.ServerCurrency;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.ResourceBundle;

/***
 * This method controls the chart window. The chart window is used for displaying historical rates in a way that is visual.
 */
public class ChartController implements CONSTANTS, Initializable {

    @FXML
    LineChart<String, Number> lineChart;

    @FXML
    Button chartButton;

    @FXML
    FlowPane flowPane;

    public void btnAction(ActionEvent event){
        event.getSource();
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);
        String[] listCur = COMPLETE_CURRENCY_NAMES;
        /*
        lineChart.getData().add(new XYChart.Series(FXCollections.observableArrayList(new XYChart.Data("",0))));
        lineChart.getData().clear();
        */

        for (int i = 0; i < listCur.length; i++) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(listCur[i]);
            ServerCurrency sc1 = new ServerCurrency(listCur[i]);
            Client client = new Client(new CurrencyDataObject(sc1, DATE_TODAY, true));
            List<ServerCurrency> currencyList = client.getDataObject().getServerCurrencyList();
            for (int j = 0; j < currencyList.size(); j++) {
                ServerCurrency serverCurrency = currencyList.get(j);
                series.getData().add(new XYChart.Data<>(serverCurrency.getDate(), Double.parseDouble(serverCurrency.getRawRate())));
            }
            lineChart.getData().add(series);
        }
    }

    public void getCurrencyData(ActionEvent event){
        String sourceName = ((RadioButton)event.getSource()).getText();
        if(!lineChart.getData().toString().contains(sourceName)) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(sourceName);
            ServerCurrency sc1 = new ServerCurrency(sourceName);
            Client client = new Client(new CurrencyDataObject(sc1, DATE_TODAY, true));
            List<ServerCurrency> currencyList = client.getDataObject().getServerCurrencyList();
            for (int j = 0; j < currencyList.size(); j++) {
                ServerCurrency serverCurrency = currencyList.get(j);
                series.getData().add(new XYChart.Data<>(serverCurrency.getDate(), Double.parseDouble(serverCurrency.getRawRate())));
            }
            lineChart.getData().add(series);
        } else if (lineChart.getData().toString().contains(sourceName)){
            for(int i = 0; i < lineChart.getData().size(); i++) {
                if(lineChart.getData().get(i).toString().contains(sourceName)) {
                    lineChart.getData().remove(i);
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chartButton.setVisible(false);
        lineChart.setLegendVisible(true);
        lineChart.setLegendSide(Side.TOP);

        List<RadioButton> list = new ArrayList<>();
        for (int i = 0; i < 40; i++){
            RadioButton rb = new RadioButton();
            rb.setText(String.valueOf(CURRENCY_NAMES[i]));
            rb.setMinSize(55, 5);
            rb.setOnAction(this::getCurrencyData);
            list.add(rb);
        }
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        // flowPane.getChildren().add(new Button("Five"));
        for (int i = 0; i < list.size(); i++) {
            flowPane.getChildren().add(list.get(i));
        }
    }
}
