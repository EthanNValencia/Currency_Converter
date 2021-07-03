/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import CC_Server.CurrencyDataObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
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

    @FXML
    private ImageView cur1_Image, cur2_Image;

    @FXML
    private Label outputRate, conversionIndicator, currencyExchange, serverOutputLabel;

    @FXML
    private ComboBox<String> comboBox1, comboBox2;

    @FXML
    private TextField inputArea;

    @FXML
    private TextArea textArea;

    @FXML
    private Button serverButton;

    @FXML
    private Button submitBtn;

    @FXML
    private AnchorPane backgroundPane;

    @FXML
    void chart(ActionEvent event) {

        // I PASTED THIS CODE HERE FOR FUTURE USE
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);
            // Socket socket = new Socket("192.168.0.95", 8000); // this is where this program connects to the server

            // Create an input stream to receive data from the server
            fromServer = new ObjectInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new ObjectOutputStream(socket.getOutputStream());
            // System.out.println("Server connection was successful.");
            // serverOutputLabel.setText("Connection established.");
        } catch (
                IOException ex) {
            System.out.println("An IO exception occurred on the client side.");
        }

        try {
            // Get the number from the server
            CurrencyDataObject dataObject = new CurrencyDataObject(comboBox1.getValue(), comboBox2.getValue(), DATE_TODAY);
            // inputArea.setText("");
            // Send the number to the server
            toServer.writeObject(dataObject);
            toServer.flush();
            // Get area from the server
            System.out.println("Sent to server " + dataObject);
            CurrencyDataObject returnedInfo = (CurrencyDataObject) fromServer.readObject();
            System.out.println("Received from server: " + returnedInfo);
            // Display to the label area
            // serverOutputLabel.setText("SN: " + sendNumber + " Received: " + returnedNumber);
        } catch (IOException ex) {
            System.out.println("The client threw an io exception.");
            System.out.println("The server may not be running.");
        } catch (NullPointerException npe) {
            System.out.println("A null pointer exception was thrown on the client side.");
            System.out.println("The server may not be running.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("ChartUI.fxml"));
            Parent root = fxmlLoader1.load();
            Stage stage = new Stage();
            stage.setTitle("Chart");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex) {
            System.out.println("An exception occurred.");
        }
}

    /***
     * Method that determines what the currency rate is and displays the rate to the GUI.
     * @param currency1 The first currency object that is selected from the first combobox.
     * @param currency2 The second currency object that is selected from the second combobox
     */
    public void getRate(Currency currency1, Currency currency2){
        currencyExchange.setText("");
        calculateObj = new Calculation(currency1, currency2);
        outputRate.setText(calculateObj.getDisplayRate1() + " " + currency1.getName() + " = " + calculateObj.getDisplayRate2() + " " + currency2.getName());
        conversionIndicator.setText("Converting " + currency1.getName() + " to " + currency2.getName());
        submitBtn.setVisible(true); inputArea.setVisible(true);
    }

    /***
     * This method correlates the ComboBox names to stored currency objects.
     * @param comboBox1 This is the the string parameter from the first combobox.
     * @param comboBox2 This is the the string parameter from the second combobox.
     */
    public void getString(String comboBox1, String comboBox2){
        for (HashMap.Entry<String, Currency> entry : CONSTANTS.nationHashMap.entrySet()){
            if(entry.getKey() == comboBox1)
                comboBox1Currency = entry.getValue();

            if(entry.getKey() == comboBox2)
                comboBox2Currency = entry.getValue();
        }

        try {
            getRate(comboBox1Currency, comboBox2Currency);
        } catch (NullPointerException npe){
            System.out.println("A NullPointerException occurred. There was likely a problem pulling necessary data from the internet.");
        }

        changeImages();
    }

    public void changeImages(){
        try {
            Image image1 = new Image("CC_Images/" + comboBox1Currency.getName() + ".png");
            Image image2 = new Image("CC_Images/" + comboBox2Currency.getName() + ".png");
            toolTip1.setText("This is the currency of " + comboBox1Currency.getNationName());
            toolTip2.setText("This is the currency of " + comboBox2Currency.getNationName());
            cur1_Image.setImage(image1);
            cur2_Image.setImage(image2);
        } catch (IllegalArgumentException iae){
            System.out.println("Image not found.");
        }
    }


    /***
     * This method is used with the submit button. It pulls text from the text area and calculates the currency conversion.
     */
    public void submit() {
        String input = inputArea.getText();
        double amount;

        try {
            conversionIndicator.setText("Converting " + comboBox1Currency.getName() + " to " + comboBox2Currency.getName());
            amount = Double.parseDouble(input);
            calculateObj.convertCurrency(amount);
            if (comboBox1Currency == null && comboBox2Currency == null) {
                conversionIndicator.setText("Please select your currencies.");
            } else if (!(comboBox1Currency == null) && !(comboBox2Currency == null)) {
                textArea.appendText(amount + " " + comboBox1Currency.getName() + " = " + calculateObj.getInputConversion() + " " + comboBox2Currency.getName() + "\n");
            }
        } catch (RuntimeException re) {
            textArea.appendText("Please enter a numeric. ");
            textArea.appendText("Your input must be a simple number. Ex: 500\n");
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
        currencyExchange.setText("");

        for (int i = 0; i < CONSTANTS.CURRENCYNAMES.length; i++) {
            comboBox1.getItems().add(CONSTANTS.CURRENCYNAMES[i]);
            comboBox2.getItems().add(CONSTANTS.CURRENCYNAMES[i]);
        }
        inputArea.setVisible(false);
        submitBtn.setVisible(false);
        serverButton.setVisible(true);
        serverOutputLabel.setVisible(false);

        comboBox1.getSelectionModel().selectFirst();
        comboBox2.getSelectionModel().selectFirst();

        comboBox1.setOnAction(e -> getString(comboBox1.getValue(), comboBox2.getValue()));
        comboBox2.setOnAction(e -> getString(comboBox1.getValue(), comboBox2.getValue()));
    }
}
