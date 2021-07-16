/*
Ethan J. Nephew
July 1, 2021
Server GUI class.
*/

package CC_Server;

import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;

// --module-path "C:\Program Files\JavaFX\javafx-sdk-16\lib" --add-modules javafx.controls,javafx.fxml

/*
To run jar in cmd use: (outdated)
java --module-path "C:\Program Files (x86)\JavaFx\javafx-sdk-15.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar C:\Users\16165\Desktop\SeverTest\out\artifacts\SeverTest_jar\SeverTest.jar
*/

/***
 * This is the server class definition. This class brings together all the CC_Server classes. It also has some local algorithms and logics.
 */
public class Server extends Application {

    /***
     * This method contains the server directions. Anything that is passed to the server and modified can be found here.
     * @param primaryStage Requires the primary stage.
     */
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Text area for displaying contents
        TextArea ta = new TextArea();
        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(ta), 350, 190);
        Image icon = new Image("CC_Icons/ServerIcon.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.setResizable(false);
        primaryStage.show(); // Display the stage
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit(); // This shuts down all server threads when the window closes.
            System.exit(0);
        });
        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        new Thread( () -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> ta.appendText("Server started at " + new Date() + '\n'));
                Platform.runLater(() -> ta.appendText("Server is gathering currency related data.\n"));
                Platform.runLater(() -> ta.appendText("This may take some time...\n"));
                ServerWebReader serverWebReader = new ServerWebReader();
                try {
                    serverWebReader.insertAnnualCurrencyData();
                    Platform.runLater(() -> ta.appendText("Currency related data has been successfully gathered.\n"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> ta.appendText("Waiting for client connection... \n"));
                while (true) {
                    Socket socket = serverSocket.accept(); // it waits here for a client message
                    // Create data input and output streams
                    Platform.runLater(() -> ta.appendText("Messaged received!\n"));
                    ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());
                    Object receivedObject = inputFromClient.readObject();

                    if(receivedObject.getClass() == CurrencyDataObj.class) {
                        CurrencyDataObj receivedDataObject = (CurrencyDataObj) receivedObject;
                        Platform.runLater(() -> ta.appendText("Server received client data.\n"));
                        // For some reason the later check is not working
                        receivedDataObject = getData(receivedDataObject);
                        // End data object modification for chart
                        outputToClient.writeObject(receivedDataObject);
                        Platform.runLater(() -> ta.appendText("A client connection has been established from:\n" + socket + "\n"));
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
                    Platform.runLater(() -> ta.appendText("Connection has been closed. \n"));
                    Platform.runLater(() -> ta.appendText("Waiting for client connection... \n"));
                }
            } catch(IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }).start();
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
        /*
        else if (currencyDataObj.getHistoricalList()) {
            try {
                currencyDataObj.setServerCurrencyList(Connect.generateHistoricalMonthlyDataList(currencyDataObj));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (currencyDataObj.getRateOfChangeList()) {
            try {
                currencyDataObj.setServerCurrencyList(Connect.generateHistoricalMonthlyRateOfChangeList(currencyDataObj));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */
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

    /**
     * The main method is only needed for the IDE with limited JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }

}