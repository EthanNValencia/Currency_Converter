/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import CC_Server.CurrencyDataObject;
import CC_Server.ServerCurrency;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/***
 * Controller class for the GUI. It contains some basic filtering logics for calculating currency conversions.
 */
public class MainController implements Initializable, CONSTANTS {

    private Currency comboBox1Currency = null;
    private Currency comboBox2Currency = null;
    private Calculation calculateObj;
    private Tooltip toolTip1 = new Tooltip("This is the currency of Colombia");
    private Tooltip toolTip2 = new Tooltip("This is the currency of Colombia");
    private Tooltip submit = new Tooltip();
    private ObjectOutputStream toServer = null;
    private ObjectInputStream fromServer = null;
    private CurrencyDataObject sentInfo;

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
    void chart(ActionEvent event) {
        serverRequest();
        /*
        try { // THIS LOADS THE CHART WINDOW DO NOT DELETE
            FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("ChartUI.fxml"));
            Parent root = fxmlLoader1.load();
            Stage stage = new Stage();
            stage.setTitle("Chart");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex) {
            System.out.println("An exception occurred.");
        }
        */
}

    /***
     * This method sends a server request that generates a data object and receives a data object. The received data object is processed and displayed to relevant areas within the GUI.
     */
    public void serverRequest(){
        try {
            Socket socket = new Socket("localhost", 8000);
            // Socket socket = new Socket("192.168.0.95", 8000); // this is where this program connects to the server
            fromServer = new ObjectInputStream(socket.getInputStream());
            toServer = new ObjectOutputStream(socket.getOutputStream());
        } catch (
                IOException ex) {
            System.out.println("An IO exception occurred on the client side.");
        }
        try {
            ServerCurrency cur1 = new ServerCurrency();
            ServerCurrency cur2 = new ServerCurrency();
            cur1.setName(comboBox1.getValue());
            cur2.setName(comboBox2.getValue());
            if (!inputArea.getText().equals("")) {
                cur1.setExchangeAmount(inputArea.getText());
            }
            sentInfo = new CurrencyDataObject(cur1, cur2, DATE_TODAY);
            toServer.writeObject(sentInfo); // send object to server
            toServer.flush(); // flush request
            CurrencyDataObject returnedInfo = (CurrencyDataObject) fromServer.readObject();
            outputRate.setText(returnedInfo.getCurrency1().getAdjustedRate() + " " + returnedInfo.getCurrency1().getName() + " = " + returnedInfo.getCurrency2().getAdjustedRate() + " " + returnedInfo.getCurrency2().getName());
            conversionIndicator.setText("Converting " + returnedInfo.getCurrency1().getName() + " to " + returnedInfo.getCurrency2().getName());
            if (returnedInfo.getCurrency1().getExchangeAmount() != null) {
                textArea.appendText(returnedInfo.getCurrency1().getExchangeAmount() + " " + returnedInfo.getCurrency1().getName() + " = " + returnedInfo.getCurrency2().getExchangeAmount() + " " + returnedInfo.getCurrency2().getName() + "\n");
            }
            toolTip1.setText(returnedInfo.getCurrency1().getDescription());
            toolTip2.setText(returnedInfo.getCurrency2().getDescription());

        } catch (IOException ex) {
            System.out.println("The client threw an io exception.");
            System.out.println("The server may not be running.");
        } catch (NullPointerException npe) {
            System.out.println("A null pointer exception was thrown on the client side.");
            System.out.println("The server may not be running.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * This method uses input generated by the combo boxes to send and receive data from the server and change the national images.
     */
    public void changeImages(){
        inputArea.setText("");
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

        try {
            Image image = new Image("CC_Images/COP.png"); // default setting
            cur1_Image.setImage(image);
            cur2_Image.setImage(image);
        } catch (IllegalArgumentException iae) {
            System.out.println("Image not found.");
        }

        outputRate.setText("");
        conversionIndicator.setText("");

        for (int i = 0; i < CONSTANTS.CURRENCYNAMES.length; i++) {
            comboBox1.getItems().add(CONSTANTS.CURRENCYNAMES[i]);
            comboBox2.getItems().add(CONSTANTS.CURRENCYNAMES[i]);
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
        clearTextArea.setOnAction(e -> {
            textArea.setText("");
        });
    }

}
