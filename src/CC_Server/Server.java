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

public class Server extends Application {

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Text area for displaying contents
        TextArea ta = new TextArea();

        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            //System.out.println("IP: " + ip);
            //System.out.println("Hostname: " + hostname);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


        new Thread( () -> {
            try {

                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() ->
                        ta.appendText("Server started at " + new Date() + '\n'));

                // Listen for a connection request
                Socket socket = serverSocket.accept(); // it waits here for a client message

                // Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    // Receive radius from the client
                    double number = inputFromClient.readDouble();

                    // Compute area
                    boolean check = Server.checkPrime(number);

                    // Send area back to the client
                    //outputToClient.writeDouble(number);
                    outputToClient.writeBoolean(check);

                    Platform.runLater(() -> {
                        ta.appendText("Number recieved from client: "
                                + number + '\n');
                        ta.appendText("The number is a prime number: " + check + '\n');
                    });
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public static boolean checkPrime(double n){
        if (n <= 1)
            return false;
        for (int i = 2; i < n; i++) {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    /**
     * The main method is only needed for the IDE with limited JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}