package com.mitchmele.tradepublisher.domain;


public class Stock {

    String symbol;
    Double bid;
    Double offer;
    Double lastPrice;

    final static String DEFAULT_SYMBOL = "XYZ";

    public Stock() {
        this.symbol = DEFAULT_SYMBOL;
        this.bid = 5.00;
        this.offer = 5.50;
        this.lastPrice = 5.25;
    }

    public Stock(String symbol, Double bid, Double offer, Double lastPrice) {
        this.symbol = symbol;
        this.bid = bid;
        this.offer = offer;
        this.lastPrice = lastPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public Double getOffer() {
        return offer;
    }

    public void setOffer(Double offer) {
        this.offer = offer;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }
}

