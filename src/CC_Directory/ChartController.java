package CC_Directory;

import CC_Server.CurrencyDataObject;
import CC_Server.ServerCurrency;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/***
 * This method controls the chart window. The chart window is used for displaying historical rates in a way that is visual.
 */
public class ChartController implements CONSTANTS, Initializable {

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private CategoryAxis xAxis ;

    @FXML
    private NumberAxis yAxis ;

    @FXML
    private Button chartButton;

    @FXML
    private FlowPane flowPane;

    @FXML
    private AnchorPane backgroundPane;

    @FXML
    private Button buttonExchangeRate, buttonRateOfChange;

    private boolean exchangeRate = true;
    private boolean rateOfChange = false;

    /***
     * This is a test method that is used to display all the historical exchange rates. The method is triggered by a button that is set to invisible by default.
     */
    public void btnAction(){
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);
        for (int i = 0; i < CURRENCY_NAMES.length; i++) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(CURRENCY_NAMES[i]);
            ServerCurrency sc1 = new ServerCurrency(CURRENCY_NAMES[i]);
            Client client = new Client(new CurrencyDataObject(sc1, DATE_TODAY, true, false));
            List<ServerCurrency> currencyList = client.getDataObject().getServerCurrencyList();
            for (int j = 0; j < currencyList.size(); j++) {
                ServerCurrency serverCurrency = currencyList.get(j);
                series.getData().add(new XYChart.Data<>(serverCurrency.getDate(), Double.parseDouble(serverCurrency.getRawRate())));
            }
            lineChart.getData().add(series);
        }
    }

    /***
     * This is a universal data request method that returns currency related data based on boolean values.
     * @param event The action event will be triggered by the radio buttons on the GUI.
     */
    public void getCurrencyData(ActionEvent event){
        String sourceName = ((RadioButton)event.getSource()).getText();
        if (!lineChart.getData().toString().contains(sourceName)) {
            XYChart.Series series = new XYChart.Series();
            series.setName(sourceName);
            ServerCurrency sc1 = new ServerCurrency(sourceName);
            Client client = new Client(new CurrencyDataObject(sc1, DATE_TODAY, exchangeRate, rateOfChange));
            List<ServerCurrency> currencyList = client.getDataObject().getServerCurrencyList();
            for (int j = 0; j < currencyList.size(); j++) {
                ServerCurrency serverCurrency = currencyList.get(j);
                series.getData().add(new XYChart.Data<>(serverCurrency.getDate(), Double.parseDouble(serverCurrency.getRawRate())));
            }
            lineChart.getData().add(series);
        } else if (lineChart.getData().toString().contains(sourceName)){
            for (int i = 0; i < lineChart.getData().size(); i++) {
                if (lineChart.getData().get(i).toString().contains(sourceName)) {
                    lineChart.getData().remove(i);
                }
            }
        }
    }

    /***
     * This is the GUI control method that changes the chart type.
     * @param event It determines which chart type to transition to based on the ActionEvent.
     */
    public void changeChartType(ActionEvent event){
        if (event.getSource().equals(buttonExchangeRate)){
            exchangeRate = true;
            rateOfChange = false;
            buttonExchangeRate.setDisable(true);
            buttonRateOfChange.setDisable(false);
            yAxis.setLabel("USD to X");
            lineChart.getData().clear();
            addRadioButtonsInFlowPane();
        }
        if (event.getSource().equals(buttonRateOfChange)) {
            rateOfChange = true;
            exchangeRate = false;
            buttonRateOfChange.setDisable(true);
            buttonExchangeRate.setDisable(false);
            yAxis.setLabel("Average Rate of Change (Large variations represents instability)");
            lineChart.getData().clear();
            addRadioButtonsInFlowPane();
        }
    }

    /***
     * Adds the radio buttons and image views to the flow pane.
     */
    public void addRadioButtonsInFlowPane(){
        flowPane.getChildren().clear();
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        flowPane.setAlignment(Pos.TOP_LEFT);
        List<RadioButton> list = new ArrayList<>();
        List<ImageView> ivList = new ArrayList<>();
        for (int i = 0; i < CURRENCY_NAMES.length; i++){
            Image image = new Image("CC_Images/" + CURRENCY_NAMES[i] + ".png");
            ImageView imageView = new ImageView();
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            imageView.setImage(image);
            ivList.add(imageView);
            RadioButton rb = new RadioButton();
            rb.setText(String.valueOf(CURRENCY_NAMES[i]));
            rb.setMinSize(50, 2);
            rb.setOnAction(this::getCurrencyData);
            list.add(rb);
        }
        for (int i = 0; i < list.size(); i++) {
            flowPane.getChildren().add(ivList.get(i));
            flowPane.getChildren().add(list.get(i));
        }
    }

    /***
     * The overridden method that is used to set the chart GUI defaults.
     * @param url The URL.
     * @param resourceBundle The resource bundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Stop[] stop = new Stop[]{new Stop(0, Color.GRAY), new Stop(1, Color.WHITE)};
        LinearGradient linGrad = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, stop);
        BackgroundFill bckFill = new BackgroundFill(linGrad, CornerRadii.EMPTY, Insets.EMPTY);
        backgroundPane.setBackground(new Background(bckFill));
        buttonExchangeRate.setDisable(true);
        chartButton.setVisible(false);
        xAxis.setLabel("Months");
        yAxis.setLabel("USD to X");
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);
        yAxis.setForceZeroInRange(false);
        lineChart.setLegendVisible(true);
        lineChart.setLegendSide(Side.TOP);
        lineChart.setCreateSymbols(false);
        addRadioButtonsInFlowPane();
    }
}
