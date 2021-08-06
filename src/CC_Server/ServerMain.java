package CC_Server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


// --module-path "C:\Program Files\JavaFX\javafx-sdk-16\lib" --add-modules javafx.controls,javafx.fxml

public class ServerMain extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader1 = new FXMLLoader(ServerMain.class.getResource("Server.fxml"));
            Parent root = fxmlLoader1.load();
            Stage primaryStage = new Stage();
            Image icon = new Image("CC_Icons/ServerIcon.png");
            primaryStage.getIcons().add(icon);
            primaryStage.setTitle("Server");
            primaryStage.setResizable(false);
            primaryStage.setTitle("Server Interface");
            primaryStage.setScene(new Scene(root));
            primaryStage.setWidth(600);
            primaryStage.setHeight(240);
            primaryStage.show();
            primaryStage.setOnCloseRequest(e -> {
                Platform.exit(); // This shuts down all server threads when the window closes.
                System.exit(0);
            });
        } catch (Exception ex) {
            System.out.println("An exception occurred.");
            System.out.println(ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
