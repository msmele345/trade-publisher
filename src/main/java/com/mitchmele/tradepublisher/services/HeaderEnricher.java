package com.mitchmele.tradepublisher.services;

import com.mitchmele.tradepublisher.domain.Stock;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;

@MessageEndpoint
public class HeaderEnricher {

    public Message<Stock> populateHeaderWithType(Stock incomingStock, @Headers Map<String, Object> headers) {
        String stockSymbol = incomingStock.getSymbol();
        Double lastPrice = incomingStock.getLastPrice();
        String category;

        if (lastPrice >= 30.00) {
            category = "BLUECHIP";
        } else if(lastPrice >= 15.00) {
            category = "MIDCAP";
        } else if (lastPrice >= 5.00){
            category = "SMALLCAP";
        } else {
            category = "PENNYSTOCK";
        }

        MessageBuilder<Stock> responseBuilder = MessageBuilder
                .withPayload(incomingStock)
                .copyHeadersIfAbsent(headers)
                .setHeader("symbol", stockSymbol)
                .setHeader("category", category);

        return responseBuilder.build();
    };
}
