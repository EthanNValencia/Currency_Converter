/*
Ethan J. Nephew
July 1, 2021
Server GUI class.
*/

package CC_Server;

import java.io.*;
import java.net.*;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
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

    private ServerWebReader serverWebReader = new ServerWebReader();

    /***
     * This is the overridden start method that is used to begin the the GUI.
     * @param primaryStage Requires the primary stage.
     */
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Text area for displaying contents
        TextArea ta = new TextArea();

        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(ta), 485, 190);
        primaryStage.setTitle("Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit(); // This shuts down all server threads when the window opens.
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
                    Platform.runLater(() -> ta.appendText("Messaged received!"));
                    ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());

                    CurrencyDataObject receivedDataObject = (CurrencyDataObject) inputFromClient.readObject();
                    Platform.runLater(() -> ta.appendText("Server received client data."));
                    receivedDataObject = findRate(receivedDataObject);
                    receivedDataObject = findDescription(receivedDataObject);
                    receivedDataObject = calculateRate(receivedDataObject);
                    outputToClient.writeObject(receivedDataObject);

                    Platform.runLater(() -> ta.appendText("A client connection has been established from:\n" + socket + "\n"));
                    outputToClient.flush();
                    outputToClient.close();
                    Platform.runLater(() -> ta.appendText("Connection has been closed. \n"));
                    Platform.runLater(() -> ta.appendText("Waiting for client connection... \n"));
                }
            } catch(IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /***
     * This class uses provided currency object information to build on the data structure. The currency rate is added in this method.
     * @param currencyDataObject Requires the data object.
     * @return It returns an appended data object.
     */
    public CurrencyDataObject findRate(CurrencyDataObject currencyDataObject){ // This is only needed for the second currency object.
        if (currencyDataObject.getCurrency1().getRawRate() == null){
            try {
                currencyDataObject.setCurrency1(Connect.getRate(currencyDataObject.getCurrency1()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (currencyDataObject.getCurrency2().getRawRate() == null){
            try {
                currencyDataObject.setCurrency2(Connect.getRate(currencyDataObject.getCurrency2()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return currencyDataObject;
    }

    /***
     * This class uses provided currency object information to build on the data structure. The currency description is added in this method.
     * @param currencyDataObject Requires the data object.
     * @return It returns an appended data object.
     */
    public CurrencyDataObject findDescription(CurrencyDataObject currencyDataObject){
        if(currencyDataObject.getCurrency1().getDescription() == null){
            try {
                currencyDataObject.setCurrency1(Connect.getDescription(currencyDataObject.getCurrency1()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(currencyDataObject.getCurrency2().getDescription() == null){
            try {
                currencyDataObject.setCurrency2(Connect.getDescription(currencyDataObject.getCurrency2()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return currencyDataObject;
    }

    /***
     * This class calculates the conversion rates between two currencies and assigns the adjusted conversion rates.
     * @param currencyDataObject Requires the data object.
     * @return It returns an appended data object.
     */
    public CurrencyDataObject calculateRate(CurrencyDataObject currencyDataObject) {
        String formatRate;
        double rate = (double) 1 / Double.parseDouble(currencyDataObject.getCurrency1().getRawRate());
        rate = rate * Double.parseDouble(currencyDataObject.getCurrency2().getRawRate());
        formatRate = String.format("%.3f", rate);
        if (formatRate.equals("0.000"))
            formatRate = String.format("%.7f", rate);
        currencyDataObject.getCurrency1().setAdjustedRate("1");
        currencyDataObject.getCurrency2().setAdjustedRate(formatRate);
        return currencyDataObject;
    }

    /**
     * The main method is only needed for the IDE with limited JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}