package CC_Directory;

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

public class ChartController implements CONSTANTS {
    @FXML
    LineChart<String, Number> lineChart;

    @FXML
    Button chartButton;

    public void btnAction(){
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (int i = 0; i < 100; i++){
            series.getData().add(new XYChart.Data<>(DATE_TODAY.minusDays(i).toString(), i));
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
