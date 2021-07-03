package CC_Server;

import java.io.Serializable;
import java.time.LocalDate;

/***
 * The idea behind this class is for it to be a data packet that is sent between the client and server.
 */
public class CurrencyDataObject implements Serializable {

    public ServerCurrency getCurrency1() {
        return currency1;
    }

    public void setCurrency1(ServerCurrency currency1) {
        this.currency1 = currency1;
    }

    public ServerCurrency getCurrency2() {
        return currency2;
    }

    public void setCurrency2(ServerCurrency currency2) {
        this.currency2 = currency2;
    }

    private ServerCurrency currency1 = new ServerCurrency(); // combobox1
    private ServerCurrency currency2 = new ServerCurrency(); // combobox2

    public CurrencyDataObject(String curName1, String curName2, LocalDate localDate) {
        this.currency1.setName(curName1);
        this.currency2.setName(curName2);
        this.currency1.setDate(localDate.toString());
        this.currency2.setDate(localDate.toString());
    }

    @Override
    public String toString() {
        return "CurrencyDataObject{" +
                "currency1=" + currency1 +
                ", currency2=" + currency2 +
                '}';
    }
}
