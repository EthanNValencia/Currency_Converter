package CC_Server;

import CC_Directory.Currency;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this.name == o && this.date == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ServerCurrency that = (ServerCurrency) o;
        return Objects.equals(hashCode(), that.hashCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date);
    }
}
