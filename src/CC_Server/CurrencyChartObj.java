/*
Ethan J. Nephew
July 16, 2021
Currency chart object that is used for charting.
*/

package CC_Server;

import java.io.Serializable;
import java.util.List;

public class CurrencyChartObj implements Serializable {

    String firstCurrencyName = "", secondCurrencyName = "";
    private List<ServerCurrency> serverCurrencyList;
    private boolean historicalList = false, rateOfChangeList = false;

    @Override
    public String toString() {
        return "CurrencyChartObj{" +
                "firstCurrencyName='" + firstCurrencyName + '\'' +
                ", secondCurrencyName='" + secondCurrencyName + '\'' +
                ", serverCurrencyList=" + serverCurrencyList +
                ", historicalList=" + historicalList +
                ", rateOfChangeList=" + rateOfChangeList +
                '}';
    }

    public CurrencyChartObj(String firstCurrencyName, String secondCurrencyName, boolean getHistoricalList, boolean getRateOfChangeList) {
        this.firstCurrencyName = firstCurrencyName;
        this.secondCurrencyName = secondCurrencyName;
        this.historicalList = getHistoricalList;
        this.rateOfChangeList = getRateOfChangeList;
    }

    public String getFirstCurrencyName() {
        return firstCurrencyName;
    }

    public void setFirstCurrencyName(String firstCurrencyName) {
        this.firstCurrencyName = firstCurrencyName;
    }

    public String getSecondCurrencyName() {
        return secondCurrencyName;
    }

    public void setSecondCurrencyName(String secondCurrencyName) {
        this.secondCurrencyName = secondCurrencyName;
    }

    public List<ServerCurrency> getServerCurrencyList() {
        return serverCurrencyList;
    }

    public void setServerCurrencyList(List<ServerCurrency> serverCurrencyList) {
        this.serverCurrencyList = serverCurrencyList;
    }

    public boolean isHistoricalList() {
        return historicalList;
    }

    public void setHistoricalList(boolean historicalList) {
        this.historicalList = historicalList;
    }

    public boolean isRateOfChangeList() {
        return rateOfChangeList;
    }

    public void setRateOfChangeList(boolean rateOfChangeList) {
        this.rateOfChangeList = rateOfChangeList;
    }
}
