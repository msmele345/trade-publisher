package com.mitchmele.tradepublisher.services;

import com.mitchmele.tradepublisher.domain.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/rabbitmq/")
public class StocksController {

    private final static Logger logger = LoggerFactory.getLogger(StocksController.class);

    @Autowired
    StockGateway stocksGateway;

    public StocksController() {
    }

    @PostMapping("/single/stock")
    public @ResponseBody ResponseEntity<Stock> postStock(@RequestBody Stock stock) {
        logger.info("SENDING MESSAGE: {} to single-stock-queue. SYMBOL: ", stock.getSymbol());
        stocksGateway.generateStock(stock);
        return ResponseEntity.ok().body(stock);
    }


    private void displayStocks(List<Stock> stocks) {
        stocks.forEach(stock -> System.out.println(stock.getSymbol()));
    }
}
