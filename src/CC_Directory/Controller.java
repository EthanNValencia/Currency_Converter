/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.
*/

package CC_Directory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/***
 * Controller class for the GUI. It contains some basic filtering logics for calculating currency conversions.
 */
public class Controller implements Initializable, CONSTANTS {

    private ArrayList<Currency> arrayList = new ArrayList();
    private Currency comboBox1Currency = null;
    private Currency comboBox2Currency = null;
    private Calculation calculateObj;
    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;

    @FXML
    private ImageView cur1_Image, cur2_Image;

    @FXML
    private Label outputRate, conversionIndicator, currencyExchange, serverOutputLabel;

    @FXML
    private ComboBox<String> comboBox1, comboBox2;

    @FXML
    private TextField inputArea;

    @FXML
    private Button serverButton;

    /***
     * This is a simple method that generates the currency objects and stores them into an ArrayList.
     */
    public void generateArrayList(){

        arrayList.add(new Currency("USD", "1"));
        arrayList.add(new Currency("COP", WebReader.getPage(CONSTANTS.COP)));
        arrayList.add(new Currency("EUR", WebReader.getPage(CONSTANTS.EUR)));
        arrayList.add(new Currency("MXN", WebReader.getPage(CONSTANTS.MXN)));
        arrayList.add(new Currency("JPY", WebReader.getPage(CONSTANTS.JPY)));
        arrayList.add(new Currency("VES", WebReader.getPage(CONSTANTS.VES)));
        arrayList.add(new Currency("GBP", WebReader.getPage(CONSTANTS.GBP)));
        arrayList.add(new Currency("PHP", WebReader.getPage(CONSTANTS.PHP)));
        arrayList.add(new Currency("RUB", WebReader.getPage(CONSTANTS.RUB)));
        arrayList.add(new Currency("CNY", WebReader.getPage(CONSTANTS.CNY)));
        arrayList.add(new Currency("INR", WebReader.getPage(CONSTANTS.INR)));

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

    }

    /***
     * This method correlates the ComboBox names to stored currency objects.
     * @param comboBox1 This is the the string parameter from the first combobox.
     * @param comboBox2 This is the the string parameter from the second combobox.
     */
    public void getString(String comboBox1, String comboBox2){

        for(int i = 0; i < arrayList.size(); i++){
            if(arrayList.get(i).getName().equals(comboBox1)){
                comboBox1Currency = new Currency(arrayList.get(i).getName(), arrayList.get(i).getRate());
            }
            if(arrayList.get(i).getName().equals(comboBox2)){
                comboBox2Currency = new Currency(arrayList.get(i).getName(), arrayList.get(i).getRate());
            }
        }

        try {
            getRate(comboBox1Currency, comboBox2Currency);
        } catch (NullPointerException npe){
            System.out.println("A NullPointerException occurred. There was likely a problem pulling necessary data from the internet.");
        }

        try {
            Image image1 = new Image("CC_Images/" + comboBox1Currency.getName() + ".png");
            Image image2 = new Image("CC_Images/" + comboBox2Currency.getName() + ".png");
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
                currencyExchange.setText(amount + " " + comboBox1Currency.getName() + " = " + calculateObj.getInputConversion() + " " + comboBox2Currency.getName());
            }
        } catch (RuntimeException re) {
            conversionIndicator.setText("Please enter a numeric.");
            currencyExchange.setText("Your input must be a simple number. EX: 500");
        }

    }

    /***
     * Overridden initialize method for the GUI.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Image image = new Image("CC_Images/COP.png"); // default setting
            cur1_Image.setImage(image);
            cur2_Image.setImage(image);
        } catch (IllegalArgumentException iae){
            System.out.println("Image not found.");
        }

        outputRate.setText("");
        conversionIndicator.setText("");
        currencyExchange.setText("");
        generateArrayList();

        for (int i = 0; i < CONSTANTS.CURRENCYNAMES.length; i++){
            comboBox1.getItems().add(CONSTANTS.CURRENCYNAMES[i]);
            comboBox2.getItems().add(CONSTANTS.CURRENCYNAMES[i]);
        }

        comboBox1.getSelectionModel().selectFirst();
        comboBox2.getSelectionModel().selectFirst();

        comboBox1.setOnAction(e -> getString(comboBox1.getValue().toString(), comboBox2.getValue().toString()));
        comboBox2.setOnAction(e -> getString(comboBox1.getValue().toString(), comboBox2.getValue().toString()));

        serverButton.setOnAction(e -> {

            try {
                // Create a socket to connect to the server
                Socket socket = new Socket("localhost", 8000);
                // Socket socket = new Socket("192.168.0.95", 8000); // this is where this program connects to the server

                // Create an input stream to receive data from the server
                fromServer = new DataInputStream(socket.getInputStream());

                // Create an output stream to send data to the server
                toServer = new DataOutputStream(socket.getOutputStream());
                // System.out.println("Server connection was successful.");
                serverOutputLabel.setText("Connection established.");
            }
            catch (IOException ex) {
                System.out.println("An IO exception occurred on the client side.");
            }

            try {
                // Get the radius from the text field
                double sendNumber = Double.parseDouble(inputArea.getText().trim());
                inputArea.setText("");
                // Send the radius to the server
                toServer.writeDouble(sendNumber);
                toServer.flush();
                // Get area from the server
                double returnedNumber = fromServer.readDouble();

                // Display to the label area
                serverOutputLabel.setText("SN: " + sendNumber + " Received: " + returnedNumber);
            } catch (IOException ex) {
                System.out.println("The client failed to connect to the server.");
                System.out.println("The server may not be running.");
            }
        });
    }
}
