package com.mitchmele.tradepublisher.services;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mitchmele.tradepublisher.domain.Stock;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = StocksControllerTest.TestConfig.class)
@ContextConfiguration(classes = StocksControllerTest.TestConfig.class)
//@AutoConfigureMockMvc
@WebAppConfiguration
public class StocksControllerTest {

    @Autowired
    StockGateway gateway;

//    @Autowired
//    StocksController stocksController;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Configuration
    static class TestConfig {
        @Bean
        StockGateway stockGateway() {
            return new StockGateway() {
                @Override
                public void generateStock(Stock stocks) { }
            };
        }
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesController() {
        ServletContext servletContext = wac.getServletContext();

        assertThat(servletContext).isNotNull();
        assertThat(servletContext instanceof MockServletContext).isTrue();

    }




//    @Test
//    public void postStock_success_shouldSendToGateway() throws Exception {
//        Stock stock1 = new Stock("AAPL", 200.00, 200.50, 200.25);
//
//        mockMvc.perform(
//                post("/single/stock")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(stockToString(stock1))
//        )
//                .andDo(print())
//                .andExpect(status().is2xxSuccessful());
//    }



    public String serializeStocks(List<Stock> stocks) {
        Gson gson = new Gson();
        try {
            return gson.toJson(stocks);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String stockToString(Stock stock) {
        Gson gson = new Gson();
        try {
            return gson.toJson(stock);
        } catch (JsonSyntaxException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}