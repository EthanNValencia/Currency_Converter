package CC_Server;

import CC_Directory.Currency;

public class ServerCurrency extends Currency {

    protected String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ServerCurrency(String name, String rate, String date) {
        super(name, rate);
        this.date = date;
    }

    @Override
    public String toString() {
        return "ServerCurrency{"+ name + " " + date + " " + rate + "}";
    }
}
