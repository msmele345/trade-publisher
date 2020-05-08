package com.mitchmele.tradepublisher.domain;

public class Ask implements StockEntity {

    String type;
    String symbol;
    Double askPrice;

    final static String ENTITY_TYPE = "ASK";

    public Ask() {}

    public Ask(String symbol, Double askPrice) {
        this.type = ENTITY_TYPE;
        this.symbol = symbol;
        this.askPrice = askPrice;
    }

    @Override
    public String toString() {
        return "Ask{" +
                "symbol='" + symbol + '\'' +
                ", askPrice=" + askPrice +
                '}';
    }

    public String getSymbol() {
        return symbol;
    }

    public String getType() {
        return ENTITY_TYPE;
    }

    public Double getAskPrice() {
        return askPrice;
    }
}
