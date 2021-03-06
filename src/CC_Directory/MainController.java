/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import CC_Server.CurrencyDataObj;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/***
 * Controller class for the GUI. It contains some basic filtering logics for calculating currency conversions.
 */
public class MainController implements Initializable, CONSTANTS {

    private Tooltip toolTip1 = new Tooltip("This is the currency of Colombia");
    private Tooltip toolTip2 = new Tooltip("This is the currency of Colombia");
    private Client client;

    @FXML
    private ImageView cur1_Image, cur2_Image;

    @FXML
    private Label outputRate, conversionIndicator;

    @FXML
    private ComboBox<String> comboBox1, comboBox2;

    @FXML
    private TextField inputArea;

    @FXML
    private TextArea textArea;

    @FXML
    private Button chartButton;

    @FXML
    private Button clearTextArea;

    @FXML
    private Button submitButton;

    @FXML
    private AnchorPane backgroundPane;

    @FXML
    void chart() {
        try {
            FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("ChartUI.fxml"));
            Parent root = fxmlLoader1.load();
            Stage stage = new Stage();
            Image icon = new Image("CC_Icons/ChartIcon.png");
            stage.getIcons().add(icon);
            stage.setResizable(false);
            stage.setTitle("Historical Data");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex) {
            System.out.println("An exception occurred.");
            System.out.println(ex);
        }
}

    /***
     * This method sends a server request that generates a data object and receives a data object. The received data object is processed and displayed to relevant areas within the GUI.
     */
    public void serverRequest(){
        client = new Client(comboBox1.getValue(), comboBox2.getValue(), inputArea.getText());
        outputRate.setText(client.getOutputRate());
        conversionIndicator.setText(client.getConversionIndicator());
        CurrencyDataObj currencyDataObj = (CurrencyDataObj) client.getDataObj();
        if (currencyDataObj.getCurrency1().getExchangeAmount() != null) {
            textArea.appendText(client.getTextArea());
        }
        toolTip1.setText(currencyDataObj.getCurrency1().getDescription());
        toolTip2.setText(currencyDataObj.getCurrency2().getDescription());
    }

    /***
     * This method uses input generated by the combo boxes to send and receive data from the server and change the national images.
     */
    public void changeImages(){
        serverRequest();
        inputArea.setVisible(true);
        try {
            Image image1 = new Image("CC_Images/" + comboBox1.getValue() + ".png");
            Image image2 = new Image("CC_Images/" + comboBox2.getValue() + ".png");
            cur1_Image.setImage(image1);
            cur2_Image.setImage(image2);
        } catch (IllegalArgumentException iae){
            System.out.println("Image not found.");
        }
    }

    /***
     * Overridden initialize method for the GUI.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Stop[] stop = new Stop[]{new Stop(0, Color.GRAY), new Stop(1, Color.WHITESMOKE)};
        LinearGradient linGrad = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stop);
        BackgroundFill bckFill = new BackgroundFill(linGrad, CornerRadii.EMPTY, Insets.EMPTY);
        backgroundPane.setBackground(new Background(bckFill));
        comboBox1.setTooltip(toolTip1);
        comboBox2.setTooltip(toolTip2);
        outputRate.setText("");
        conversionIndicator.setText("");
        for (int i = 0; i < CONSTANTS.CURRENCY_NAMES.length; i++) {
            comboBox1.getItems().add(CONSTANTS.CURRENCY_NAMES[i]);
            comboBox2.getItems().add(CONSTANTS.CURRENCY_NAMES[i]);
        }
        inputArea.setVisible(false);
        submitButton.setVisible(true);
        chartButton.setVisible(true);
        textArea.setEditable(false);
        comboBox1.getSelectionModel().selectFirst();
        comboBox2.getSelectionModel().selectFirst();
        comboBox1.setOnAction(e -> changeImages());
        comboBox2.setOnAction(e -> changeImages());
        inputArea.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                serverRequest();
            }
        });
        inputArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    inputArea.setText(newValue.replaceAll("[^\\d]", "")); // I want to change this so that it doesn't delete periods.
                }
            }
        });
        clearTextArea.setOnAction(e -> {
            textArea.setText("");
        });
        changeImages();
    }

}
