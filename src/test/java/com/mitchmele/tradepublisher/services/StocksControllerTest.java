package com.mitchmele.tradepublisher.services;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mitchmele.tradepublisher.domain.Stock;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StocksControllerTest {

    StockGateway mockGateway = mock(StockGateway.class);

    StocksController subject = new StocksController();

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(subject).build();

    @Before
    public void setUp() throws Exception {
        subject.stocksGateway = mockGateway;
    }


    @Test
    public void postNewStocks_success_shouldCallTheGateway() throws Exception {


        Stock stock1 = new Stock("AAPL", 200.00, 200.50, 200.25);
        Stock stock2 = new Stock("MSFT", 50.00, 50.55, 50.40);
        Stock stock3 = new Stock("ABC", 2.00, 2.50, 2.25);

        List<Stock> allStocks = Arrays.asList(stock1, stock2, stock3);

        mockMvc.perform(
                post("/rabbitmq/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializeStocks(allStocks)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }



    public String serializeStocks(List<Stock> stocks) {
        Gson gson = new Gson();
        try {
            return gson.toJson(stocks);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}