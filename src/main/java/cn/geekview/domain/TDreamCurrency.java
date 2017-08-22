package cn.geekview.domain;

import java.math.BigDecimal;

public class TDreamCurrency {

    private String currencyNick;

    private BigDecimal currencyExchange;

    public String getCurrencyNick() {
        return currencyNick;
    }

    public void setCurrencyNick(String currencyNick) {
        this.currencyNick = currencyNick;
    }

    public BigDecimal getCurrencyExchange() {
        return currencyExchange;
    }

    public void setCurrencyExchange(BigDecimal currencyExchange) {
        this.currencyExchange = currencyExchange;
    }

    @Override
    public String toString() {
        return "TDreamCurrency{" +
                "currencyNick='" + currencyNick + '\'' +
                ", currencyExchange=" + currencyExchange +
                '}';
    }
}
