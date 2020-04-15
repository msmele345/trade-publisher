package com.mitchmele.tradepublisher.services;

import com.mitchmele.tradepublisher.domain.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/rabbitmq/")
public class StocksController {

    private final static Logger logger = LoggerFactory.getLogger(StocksController.class);

    @Autowired
    StockGateway stocksGateway;

    public StocksController() {
    }

    @PostMapping(value = "/stocks")
    public @ResponseBody ResponseEntity<List<Stock>> postNewStocks(@RequestBody List<Stock> stocks) {
        logger.info("Sending Message: {} to stocks queue.", stocks);
        stocksGateway.generateStocks(stocks);
        return ResponseEntity.ok().body(stocks);
    }

    @PostMapping("/single/stock")
    public @ResponseBody ResponseEntity<Stock> postStock(@RequestBody Stock stock) {
        logger.info("Sending Message: {} to single-stock-queue.", stock);
        stocksGateway.generateStock(stock);
        return ResponseEntity.ok().body(stock);
    }
}
