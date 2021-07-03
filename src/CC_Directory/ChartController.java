package CC_Directory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChartController {

    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;

    public void connectToServer(){
        // I PASTED THIS CODE HERE FOR FUTURE USE
        try {
        // Create a socket to connect to the server
        Socket socket = new Socket("localhost", 8000);
        // Socket socket = new Socket("192.168.0.95", 8000); // this is where this program connects to the server

        // Create an input stream to receive data from the server
        fromServer = new DataInputStream(socket.getInputStream());

        // Create an output stream to send data to the server
        toServer = new DataOutputStream(socket.getOutputStream());
        // System.out.println("Server connection was successful.");
        // serverOutputLabel.setText("Connection established.");
    } catch (
    IOException ex) {
        System.out.println("An IO exception occurred on the client side.");
    }

            try {
        // Get the number from the server
        // double sendNumber = Double.parseDouble(inputArea.getText().trim());
        // inputArea.setText("");
        // Send the number to the server
        // toServer.writeDouble(sendNumber);
        toServer.flush();
        // Get area from the server
        double returnedNumber = fromServer.readDouble();

        // Display to the label area
        // serverOutputLabel.setText("SN: " + sendNumber + " Received: " + returnedNumber);
    } catch (IOException ex) {
        System.out.println("The client threw an io exception.");
        System.out.println("The server may not be running.");
    } catch (NullPointerException npe){
        System.out.println("A null pointer exception was thrown on the client side.");
        System.out.println("The server may not be running.");
        }
    }
}
