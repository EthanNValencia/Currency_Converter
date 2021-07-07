package CC_Directory;

import CC_Server.CurrencyDataObject;
import CC_Server.ServerCurrency;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements CONSTANTS {

    private ObjectOutputStream toServer = null;
    private ObjectInputStream fromServer = null;
    private CurrencyDataObject dataObject = new CurrencyDataObject();
    private String comboBox1, comboBox2, inputArea, outputRate, conversionIndicator, textArea, toolTip1, toolTip2;

    public CurrencyDataObject getDataObject() {
        return dataObject;
    }

    public void setDataObject(CurrencyDataObject dataObject) {
        this.dataObject = dataObject;
    }

    public String getComboBox1() {
        return comboBox1;
    }

    public void setComboBox1(String comboBox1) {
        this.comboBox1 = comboBox1;
    }

    public String getComboBox2() {
        return comboBox2;
    }

    public void setComboBox2(String comboBox2) {
        this.comboBox2 = comboBox2;
    }

    public String getInputArea() {
        return inputArea;
    }

    public void setInputArea(String inputArea) {
        this.inputArea = inputArea;
    }

    public String getOutputRate() {
        return outputRate;
    }

    public void setOutputRate(String outputRate) {
        this.outputRate = outputRate;
    }

    public String getConversionIndicator() {
        return conversionIndicator;
    }

    public void setConversionIndicator(String conversionIndicator) {
        this.conversionIndicator = conversionIndicator;
    }

    public String getTextArea() {
        return textArea;
    }

    public void setTextArea(String textArea) {
        this.textArea = textArea;
    }

    public String getToolTip1() {
        return toolTip1;
    }

    public void setToolTip1(String toolTip1) {
        this.toolTip1 = toolTip1;
    }

    public String getToolTip2() {
        return toolTip2;
    }

    public void setToolTip2(String toolTip2) {
        this.toolTip2 = toolTip2;
    }

    public Client(String comboBox1, String comboBox2, String inputArea){
        this.comboBox1 = comboBox1;
        this.comboBox2 = comboBox2;
        this.inputArea = inputArea;
        this.dataObject = new CurrencyDataObject(new ServerCurrency(comboBox1), new ServerCurrency(comboBox2), DATE_TODAY);
        if (!inputArea.equals("")) {
            dataObject.getCurrency1().setExchangeAmount(inputArea);
        }
        serverRequest(dataObject);
    }

    public Client(CurrencyDataObject currencyDataObject){
        this.dataObject = currencyDataObject;
        serverRequest(this.dataObject);
    }

    public void serverRequest(CurrencyDataObject currencyDataObject){
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
