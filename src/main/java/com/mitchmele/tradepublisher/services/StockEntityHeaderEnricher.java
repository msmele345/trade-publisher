package com.mitchmele.tradepublisher.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;

@MessageEndpoint
public class StockEntityHeaderEnricher {

    public Message<?> populateHeaderWithSymbol(Message<?> incomingStock) {
        Map<String, Object> headers = incomingStock.getHeaders();

        JsonObject object = (JsonObject) new JsonParser().parse((String) incomingStock.getPayload());
        String symbol =  object.get("symbol").toString();
        String parsedSymbol = symbol.substring(1, symbol.length() -1);

            return MessageBuilder
                    .withPayload(incomingStock)
                    .copyHeadersIfAbsent(headers)
                    .setHeader("Symbol", parsedSymbol)
                    .build();
        }
}
