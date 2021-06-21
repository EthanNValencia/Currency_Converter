/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.

Note to future self.
This project took several days longer than anticipated,
because Grandpa had several accidents.

Objectives
I want to add a client-server architecture
The server side will include a database
The server side will store rates
The server side will store rates with the time the rate was collected
The client side will have the ability to display the historical rates
in a useful way, historical rates will be received from the server.

What about adding in the unique currency symbols? $ (USD), £ (GBP), ₽ (RUB), etc

In the inputText area, make it so that a user can input comas: 3,600 for example
This will probably mean replacing commas with closed spaces.
*/

package CC_Directory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
// --module-path "C:\Program Files\JavaFX\javafx-sdk-16\lib" --add-modules javafx.controls,javafx.fxml

/***
 * Main method for the application.
 */
public class Main extends Application {

    /***
     * Overridden start method for the JavaFX application.
     * @param primaryStage Primary stage of the GUI.
     * @throws Exception It can throw a number of exceptions.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("UserInterface.fxml"));
        primaryStage.setTitle("Currency Converter");
        primaryStage.setScene(new Scene(root, 400, 280));
        primaryStage.show();
    }


    /***
     * Main method with the launch() method.
     * @param args Passes the args to the launch() method.
     */
    public static void main(String[] args) {

        /*
        Thread music_Thread = new Thread(new PlayMusic());
        music_Thread.start();
        */

        launch(args);
    }
}
