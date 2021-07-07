package CC_Directory;

import CC_Server.CurrencyDataObject;
import CC_Server.ServerCurrency;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

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

    @FXML
    private AnchorPane backgroundPane;

    public void btnAction(ActionEvent event){
        event.getSource();
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);

        /*
        lineChart.getData().add(new XYChart.Series(FXCollections.observableArrayList(new XYChart.Data("",0))));
        lineChart.getData().clear();
        */

        for (int i = 0; i < CURRENCY_NAMES.length; i++) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(CURRENCY_NAMES[i]);
            ServerCurrency sc1 = new ServerCurrency(CURRENCY_NAMES[i]);
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

        Stop[] stop = new Stop[]{new Stop(0, Color.GRAY), new Stop(1, Color.WHITE)};
        LinearGradient linGrad = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, stop);
        BackgroundFill bckFill = new BackgroundFill(linGrad, CornerRadii.EMPTY, Insets.EMPTY);
        backgroundPane.setBackground(new Background(bckFill));

        chartButton.setVisible(false);
        lineChart.setLegendVisible(true);
        lineChart.setLegendSide(Side.TOP);
        lineChart.setCreateSymbols(false);
        flowPane.setHgap(10);
        flowPane.setVgap(10);

        List<RadioButton> list = new ArrayList<>();
        for (int i = 0; i < CURRENCY_NAMES.length; i++){
            RadioButton rb = new RadioButton();
            rb.setText(String.valueOf(CURRENCY_NAMES[i]));
            rb.setMinSize(50, 5);
            rb.setOnAction(this::getCurrencyData);
            list.add(rb);
        }
        flowPane.setAlignment(Pos.TOP_CENTER);

        // flowPane.getChildren().add(new Button("Five"));
        for (int i = 0; i < list.size(); i++) {
            flowPane.getChildren().add(list.get(i));
        }
    }
}
