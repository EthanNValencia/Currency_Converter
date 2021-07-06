package CC_Directory;

import CC_Server.CurrencyDataObject;
import CC_Server.ServerCurrency;
import javafx.fxml.FXML;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;

public class ChartController implements CONSTANTS {
    @FXML
    LineChart<String, Number> lineChart;

    @FXML
    Button chartButton;

    public void btnAction(){
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        ServerCurrency sc1 = new ServerCurrency("COP");
        Client client = new Client( new CurrencyDataObject(sc1, DATE_TODAY, true));
        List<ServerCurrency> currencyList = client.getDataObject().getServerCurrencyList();
        for (int i = 0; i < currencyList.size(); i++){
            ServerCurrency serverCurrency = currencyList.get(i);
            series.getData().add(new XYChart.Data<>(serverCurrency.getDate(), Double.parseDouble(serverCurrency.getRawRate())));
        }


        /*
        series.getData().add(new XYChart.Data<>("Jan", 400));
        series.getData().add(new XYChart.Data<>("Feb", 200));
        series.getData().add(new XYChart.Data<>("Mar", 300));
        series.getData().add(new XYChart.Data<>("Apr", 600));
        */
        lineChart.getData().add(series);
    }

}
