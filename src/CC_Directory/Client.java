/*
Ethan J. Nephew
July 9, 2021
Client connection class.
*/

package CC_Directory;

import CC_Server.CurrencyDataObject;
import CC_Server.ServerCurrency;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/***
 * The Client class definition that is used to provide an a portal to and from the server.
 */
public class Client implements CONSTANTS {

    private ObjectOutputStream toServer = null;
    private ObjectInputStream fromServer = null;
    private CurrencyDataObject dataObject = new CurrencyDataObject();
    private String comboBox1, comboBox2, inputArea, outputRate, conversionIndicator, textArea, toolTip1, toolTip2;

    /***
     * Accessor method for the data object.
     * @return The data object.
     */
    public CurrencyDataObject getDataObject() {
        return dataObject;
    }

    /***
     * Accessor for the output rate.
     * @return The string that will be displayed as the output rate.
     */
    public String getOutputRate() {
        return outputRate;
    }

    /***
     * Accessor method for the conversion indicator.
     * @return The string of the conversion indicator.
     */
    public String getConversionIndicator() {
        return conversionIndicator;
    }

    /***
     * Accessor method for the text area string.
     * @return The string that is to be assigned to the text area.
     */
    public String getTextArea() {
        return textArea;
    }

    /***
     * A three parameter constructor.
     * @param comboBox1 The contents of the first combo box.
     * @param comboBox2 The contents of the second combo box.
     * @param inputArea The contents from the input area.
     */
    public Client(String comboBox1, String comboBox2, String inputArea){
        this.comboBox1 = comboBox1;
        this.comboBox2 = comboBox2;
        this.inputArea = inputArea;
        this.dataObject = new CurrencyDataObject(new ServerCurrency(comboBox1), new ServerCurrency(comboBox2), DATE_TODAY);
        if (!inputArea.equals("")) {
            dataObject.getCurrency1().setExchangeAmount(inputArea);
        }
        serverRequest();
    }

    /***
     * A single parameter constructor that requires the currency data object that is to be assigned to the client.
     * @param currencyDataObject The currency data object that is to be assigned to the client.
     */
    public Client(CurrencyDataObject currencyDataObject){
        this.dataObject = currencyDataObject;
        serverRequest();
    }

    /***
     * This is where the client to server request is established. Data is passed from here to the server and from the server back to here.
     */
    public void serverRequest(){
        try {
            Socket socket = new Socket("localhost", 8000);
            // Socket socket = new Socket("192.168.0.95", 8000); // this is where this program connects to the server
            fromServer = new ObjectInputStream(socket.getInputStream());
            toServer = new ObjectOutputStream(socket.getOutputStream());
        } catch (
                IOException ex) {
            System.out.println("An IO exception occurred on the client side.");
        }
        try {
            toServer.writeObject(dataObject); // send object to server
            toServer.flush(); // flush request
            CurrencyDataObject returnedInfo = (CurrencyDataObject) fromServer.readObject();
            outputRate = returnedInfo.getCurrency1().getAdjustedRate() + " " + returnedInfo.getCurrency1().getName() + " = " + returnedInfo.getCurrency2().getAdjustedRate() + " " + returnedInfo.getCurrency2().getName();
            conversionIndicator = "Converting the " + returnedInfo.getCurrency1().getDescription() + " to the " + returnedInfo.getCurrency2().getDescription();
            if (returnedInfo.getCurrency1().getExchangeAmount() != null) {
                textArea = returnedInfo.getCurrency1().getExchangeAmount() + " " + returnedInfo.getCurrency1().getName() + " = " + returnedInfo.getCurrency2().getExchangeAmount() + " " + returnedInfo.getCurrency2().getName() + "\n";
            }
            toolTip1 = returnedInfo.getCurrency1().getDescription();
            toolTip2 = returnedInfo.getCurrency2().getDescription();
            dataObject = returnedInfo;

        } catch (IOException ex) {
            System.out.println("The client threw an io exception.");
            System.out.println("The server may not be running.");
        } catch (NullPointerException npe) {
            System.out.println("A null pointer exception was thrown on the client side.");
            System.out.println("The server may not be running.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /***
     * This is the overridden toString.
     * @return Returns a string with the object contents listed.
     */
    @Override
    public String toString() {
        return "Client{" +
                "\ntoServer=" + toServer +
                "\n, fromServer=" + fromServer +
                "\n, dataObject=" + dataObject +
                "\n, comboBox1='" + comboBox1 + '\'' +
                "\n, comboBox2='" + comboBox2 + '\'' +
                "\n, inputArea='" + inputArea + '\'' +
                "\n, outputRate='" + outputRate + '\'' +
                "\n, conversionIndicator='" + conversionIndicator + '\'' +
                "\n, textArea='" + textArea + '\'' +
                "\n, toolTip1='" + toolTip1 + '\'' +
                "\n, toolTip2='" + toolTip2 + '\'' +
                '}';
    }
}
