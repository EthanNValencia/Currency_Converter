/*
Ethan J. Nephew
June 11, 2021
Currency converter and presentation application.

Note to future self.
This project took several days longer than anticipated,
because Grandpa had several accidents. 
*/

package CC_Directory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
// --module-path "C:\Program Files\JavaFX\javafx-sdk-16\lib" --add-modules javafx.controls,javafx.fxml

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("UserInterface.fxml"));
        primaryStage.setTitle("Currency Converter");
        primaryStage.setScene(new Scene(root, 400, 150));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
