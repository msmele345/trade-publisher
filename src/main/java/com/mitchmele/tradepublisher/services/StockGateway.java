package com.mitchmele.tradepublisher.services;

import com.mitchmele.tradepublisher.domain.Stock;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Component;

import java.util.List;

@MessagingGateway
@Component
public interface StockGateway {

    @Gateway(requestChannel = "single-stock-channel")
    void generateStock(Stock stocks);
}
