package com.mitchmele.tradepublisher.services;

import com.mitchmele.tradepublisher.domain.Ask;
import com.mitchmele.tradepublisher.domain.Bid;
import com.mitchmele.tradepublisher.domain.StockEntity;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

public class StockEntityHeaderEnricherTest {

    StockEntityHeaderEnricher subject = new StockEntityHeaderEnricher();

    @Test
    public void populateHeaderWithSymbol_success_shouldPopulateHeaderSymbolOfObj() {

        String incomingStockEntity = "{\"symbol\":\"CAY\",\"bidPrice\":155.75}";
        Message<?> incomingMessage = message(incomingStockEntity);

        Map<String, Object> headers = new HashMap<>();
        headers.put("ContentType", "application/json");
        headers.put("someHeader", "some value");

      Message<?> actual = subject.populateHeaderWithSymbol(incomingMessage);

        Object symbol = actual.getHeaders().get("Symbol");
        assertThat(symbol).isEqualTo("CAY");
    }

    private static <T> Message<T> message(T val) {
        return MessageBuilder.withPayload(val).build();
    }
}