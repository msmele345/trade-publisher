package com.mitchmele.tradepublisher.services;

import com.mitchmele.tradepublisher.domain.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HeaderEnricherTest {

    HeaderEnricher subject = new HeaderEnricher();

    @Test
    public void populateHeaderWithSymbol_success_shouldPopulateHeaderSymbolOfObj() {

      Message<Stock> incomingMessage = message(
              new Stock("ABC", 41.35, 41.56, 41.40)
      );

       Stock incomingStock  = new Stock("ABC", 41.35, 41.56, 41.40);

        Map<String, Object> headers = new HashMap<>();
        headers.put("ContentType", "application/json");
        headers.put("someHeader", "some value");

      Message<Stock> actual = subject.populateHeaderWithType(incomingStock, headers);

        Object symbol = actual.getHeaders().get("symbol");
        assertThat(symbol).isEqualTo("ABC");

        Object type = actual.getHeaders().get("category");
        assertThat(type).isEqualTo("BLUECHIP");
    }

    @Test
    public void populateHeaderWithType_success_shouldPopulateHeaderTypeOfObj() {
        Stock incomingStock  = new Stock("DDY", 1.35, 1.56, 1.40);

        Map<String, Object> headers = new HashMap<>();
        headers.put("ContentType", "application/json");
        headers.put("someHeader", "some value");

        Message<Stock> actual = subject.populateHeaderWithType(incomingStock, headers);

        Object type = actual.getHeaders().get("category");
        assertThat(type).isEqualTo("PENNYSTOCK");
    }

    private static <T> Message<T> message(T val) {
        return MessageBuilder.withPayload(val).build();
    }
}