package com.mitchmele.tradepublisher.services;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.*;
import com.mitchmele.tradepublisher.domain.Stock;
import com.mitchmele.tradepublisher.domain.StockEntity;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
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
