package CC_Server;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class ServerController extends CONSTANTS implements Initializable {

    private Thread serverThread;

    private ServerSocket serverSocket;

    @FXML
    private TextArea txtAreaServer;

    @FXML
    private Button btnAttemptConnection, btnRange;

    @FXML
    private Label loginLabel1, logicLabel2;

    @FXML
    private TextField txtBoxUsername, txtBoxPassword;

    @FXML
    private ComboBox<Integer> comboBoxRange;

    /***
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        loginControlsInvisible();
        rangeControlsVisible();
        txtAreaServer.wrapTextProperty().set(true);
        txtAreaServer.appendText("Select the range of days you would like the database to have.\n");
        for(int i = 0; i < CONSTANTS.DAYS_RANGE.length; i++){
            comboBoxRange.getItems().add(DAYS_RANGE[i]);
        }
        comboBoxRange.getSelectionModel().selectFirst();
    }

    /***
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void runServerLoop() throws IOException, ClassNotFoundException {
        while (true) {
            Socket socket = serverSocket.accept(); // it waits here for a client message
            // Create data input and output streams
            txtAreaServer.appendText("Messaged received!\n");
            ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());
            Object receivedObject = inputFromClient.readObject();

            if (receivedObject.getClass() == CurrencyDataObj.class) {
                CurrencyDataObj receivedDataObject = (CurrencyDataObj) receivedObject;
                txtAreaServer.appendText("Server received client data.\n");
                // For some reason the later check is not working
                receivedDataObject = getData(receivedDataObject);
                // End data object modification for chart
                outputToClient.writeObject(receivedDataObject);
                txtAreaServer.appendText("A client connection has been established from:\n" + socket + "\n");
                outputToClient.flush();
                outputToClient.close();
                receivedDataObject.setHistoricalList(false);
            } else if (receivedObject.getClass() == CurrencyChartObj.class) {
                CurrencyChartObj receivedChartObject = (CurrencyChartObj) receivedObject;
                System.out.println("received: " + receivedChartObject);
                if (receivedChartObject.isHistoricalList()) {
                    try {
                        receivedChartObject.setServerCurrencyList(Connect.generateHistoricalMonthlyDataList(receivedChartObject));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (receivedChartObject.isRateOfChangeList()) {
                    try {
                        receivedChartObject.setServerCurrencyList(Connect.generateHistoricalMonthlyRateOfChangeList(receivedChartObject));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("sent: " + receivedChartObject);
                outputToClient.writeObject(receivedChartObject);
                outputToClient.flush();
                outputToClient.close();
            }
            txtAreaServer.appendText("Connection has been closed. \n");
            txtAreaServer.appendText("Waiting for client connection... \n");
        }
    }

    /***
     *
     */
    public void runServer(){
        serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(8000);
                txtAreaServer.appendText("Server started at " + new Date() + '\n');
                try {
                    txtAreaServer.appendText("Server is attempting to connect to the Database.\n");
                    Connect.getConnection();
                } catch (Exception e) {
                    txtAreaServer.appendText("Server failed to connect to Database.\n");
                    getDatabaseCredentials();
                    return;
                }
                txtAreaServer.appendText("Server is gathering currency related data.\n");
                txtAreaServer.appendText("This may take some time...\n");
                ServerWebReader serverWebReader = new ServerWebReader();
                try {
                    serverWebReader.insertAnnualCurrencyData();
                    txtAreaServer.appendText("Currency related data has been successfully gathered.\n");
                } catch (Exception e) {
                    txtAreaServer.appendText("An exception occurred while trying to gather currency information.\n");
                }
                txtAreaServer.appendText("Waiting for client connection... \n");
                runServerLoop();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        serverThread.start();
    }

    /***
     * Sets the login controls to visible.
     */
    public void loginControlsVisible(){
        btnAttemptConnection.setVisible(true);
        loginLabel1.setVisible(true);
        logicLabel2.setVisible(true);
        txtBoxPassword.setVisible(true);
        txtBoxUsername.setVisible(true);
    }

    /***
     * Sets the login controls to invisible.
     */
    public void loginControlsInvisible(){
        btnAttemptConnection.setVisible(false);
        loginLabel1.setVisible(false);
        logicLabel2.setVisible(false);
        txtBoxPassword.setVisible(false);
        txtBoxUsername.setVisible(false);
    }

    /***
     * Sets the day range to scan controls to visible.
     */
    public void rangeControlsVisible(){
        comboBoxRange.setVisible(true);
        btnRange.setVisible(true);
    }

    /***
     * Sets the day range to scan controls to invisible.
     */
    public void rangeControlsInvisible(){
        comboBoxRange.setVisible(false);
        btnRange.setVisible(false);
    }

    /***
     * This is connected to the Button btnRange. It sets the number of days that the server will scan.
     */
    public void btnRange(){
        rangeControlsInvisible();
        DAYS_TO_SCAN = comboBoxRange.getValue();
        System.out.println(DAYS_TO_SCAN);
        runServer();
    }

    /***
     * In the case that the preset database login credentials fail, this will open up a GUI-based option to allow user input of the credentials.
     */
    public void getDatabaseCredentials(){
        Stage stage = (Stage) txtAreaServer.getScene().getWindow();
        stage.setWidth(600);
        loginControlsVisible();
        txtAreaServer.appendText("Please enter your login credentials.\n");
    }

    /***
     * This is connected to the Button btnAttemptConnection. When the user clicks the button it will take the text from the input fields and see if a connection can be made successfully.
     */
    public void btnConnection(){
        USERNAME = txtBoxUsername.getText();
        PASSWORD = txtBoxPassword.getText();
        try {
            Connect.getConnection();
            txtAreaServer.appendText("Connection was successful.\n");
            Stage stage = (Stage) txtAreaServer.getScene().getWindow();
            // stage.setWidth(310);
            // stage.setHeight(240);
        } catch (Exception e) {
            txtAreaServer.appendText("The login credentials were incorrect. \nPlease try again.\n");
        }
    }

    /***
     * This runs the data object through several checks to determine what data should be gathered for the object.
     * @param currencyDataObj It requires a data object.
     * @return It returns the amended data object.
     */
    public CurrencyDataObj getData(CurrencyDataObj currencyDataObj){
        if (!currencyDataObj.getHistoricalList() && !currencyDataObj.getRateOfChangeList()) {
            currencyDataObj = findRate(currencyDataObj);
            currencyDataObj = findDescription(currencyDataObj);
            currencyDataObj = calculateRate(currencyDataObj);
            currencyDataObj = calculateExchange(currencyDataObj);
        }
        return currencyDataObj;
    }

    /***
     * This class uses provided currency object information to build on the data structure. The currency rate is added in this method.
     * @param currencyDataObj Requires the data object.
     * @return It returns an appended data object.
     */
    public CurrencyDataObj findRate(CurrencyDataObj currencyDataObj){ // This is only needed for the second currency object.
        if (currencyDataObj.getCurrency1().getRawRate() == null){
            try {
                currencyDataObj.setCurrency1(Connect.getRate(currencyDataObj.getCurrency1()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (currencyDataObj.getCurrency2().getRawRate() == null){
            try {
                currencyDataObj.setCurrency2(Connect.getRate(currencyDataObj.getCurrency2()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return currencyDataObj;
    }

    /***
     * This class uses provided currency object information to build on the data structure. The currency description is added in this method.
     * @param currencyDataObj Requires the data object.
     * @return It returns an appended data object.
     */
    public CurrencyDataObj findDescription(CurrencyDataObj currencyDataObj){
        if(currencyDataObj.getCurrency1().getName() != null && currencyDataObj.getCurrency1().getDescription() == null){
            try {
                currencyDataObj.setCurrency1(Connect.getDescription(currencyDataObj.getCurrency1()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(currencyDataObj.getCurrency2().getName() != null && currencyDataObj.getCurrency2().getDescription() == null){
            try {
                currencyDataObj.setCurrency2(Connect.getDescription(currencyDataObj.getCurrency2()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return currencyDataObj;
    }

    /***
     * This class calculates the conversion rates between two currencies and assigns the adjusted conversion rates.
     * @param currencyDataObj Requires the data object.
     * @return It returns an appended data object.
     */
    public CurrencyDataObj calculateRate(CurrencyDataObj currencyDataObj) {
        String formatRate;
        double rate = (double) 1 / Double.parseDouble(currencyDataObj.getCurrency1().getRawRate());
        rate = rate * Double.parseDouble(currencyDataObj.getCurrency2().getRawRate());
        formatRate = checkLower(rate);
        currencyDataObj.getCurrency1().setAdjustedRate("1");
        currencyDataObj.getCurrency2().setAdjustedRate(formatRate);
        return currencyDataObj;
    }

    /***
     * This method looks complicated, but it simply converts two strings to doubles, performs an arithmetic operation, and then converts the double result back to a string and stores it in the data object.
     * @param currencyDataObj It requires the data object.
     * @return It returns the modified data object.
     */
    public CurrencyDataObj calculateExchange(CurrencyDataObj currencyDataObj){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        if (currencyDataObj.getCurrency1().getExchangeAmount() != null) {
            double number = Double.parseDouble(currencyDataObj.getCurrency1().getExchangeAmount()) *
                    Double.parseDouble(currencyDataObj.getCurrency2().getAdjustedRate().replaceAll(",", ""));
            String strNumber = checkLower(number); // This checks that the value returned is not zero.
            currencyDataObj.getCurrency2().setExchangeAmount(strNumber);
        }
        return currencyDataObj;
    }

    /***
     * This is used to check the lower bound of a number and to change the decimal format to include strings.
     * @param number The parameter is the number that is to be examined.
     * @return It returns the formatted parameter in the form of a string.
     */
    public String checkLower(double number){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(20);
        String strNumber = String.valueOf(number);
        strNumber = new DecimalFormat("#,##0.##############").format(Double.parseDouble(strNumber));
        return strNumber;
    }
}
