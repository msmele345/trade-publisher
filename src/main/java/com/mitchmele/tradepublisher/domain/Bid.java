package com.mitchmele.tradepublisher.domain;


import java.util.Objects;

public class Bid implements StockEntity {

    String type;
    String symbol;
    Double bidPrice;

    final static String ENTITY_TYPE = "BID";

    public Bid() {
    }

    public Bid(String symbol, Double bidPrice) {
        this.type = ENTITY_TYPE;
        this.symbol = symbol;
        this.bidPrice = bidPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bid)) return false;
        Bid bid = (Bid) o;
        return symbol.equals(bid.symbol) &&
                bidPrice.equals(bid.bidPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, bidPrice);
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getType() {
        return type;
    }

    public static String getEntityType() {
        return ENTITY_TYPE;
    }

    public Double getBidPrice() {
        return bidPrice;
    }
}
