/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.

-- Objectives --
What about adding in the unique currency symbols? $ (USD), £ (GBP), ₽ (RUB), etc
In the inputText area, make it so that a user can input comas: 3,600
This will probably mean replacing commas with closed spaces.
Implement server-side multi-threading when verifying database integrity. WORKING ON THIS
Make the server generate all conversion rates based on current data. WORKING ON THIS
Make combo boxes friendly to typing.
Add tool tips in the chart currencies.
*/

package CC_Directory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.awt.*;
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
        Parent root = FXMLLoader.load(getClass().getResource("MainUI.fxml"));
        primaryStage.setTitle("Currency Converter");
        primaryStage.setResizable(false);
        Image icon = new Image("CC_Icons/ConverterIcon.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(new Scene(root));
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
