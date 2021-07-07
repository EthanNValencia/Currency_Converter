package CC_Directory;

import CC_Server.CurrencyDataObject;
import CC_Server.ServerCurrency;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.util.List;

/***
 * This method controls the chart window. The chart window is used for displaying historical rates in a way that is visual.
 */
public class ChartController implements CONSTANTS {

    @FXML
    LineChart<String, Number> lineChart;

    @FXML
    Button chartButton;

    @FXML
    VBox vBox;

    public void btnAction(){
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);
        // String[] listCur = {"EUR", "MXN", "JPY", "GBP", "PHP", "RUB", "CNY"};
        // String[] listCur = {"MXN"};
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

}
