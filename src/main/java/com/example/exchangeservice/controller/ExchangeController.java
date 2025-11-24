package com.example.exchangeservice.controller;

import com.example.exchangeservice.environment.InstanceInformationService;
import com.example.exchangeservice.model.Exchange;
import com.example.exchangeservice.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("exchange-service")
public class ExchangeController {

    //http://localhost:8000/exchange-service/5/USD/BRL

    @Autowired
    private InstanceInformationService instanceInformationService;
    @Autowired
    private ExchangeRepository exchangeRepository;

    @GetMapping( value = "{amount}/{from}/{to}", produces=MediaType.APPLICATION_JSON_VALUE)
    public Exchange getExchange(@PathVariable("amount") BigDecimal amount,
                                @PathVariable("from") String from,
                                @PathVariable("to") String to) {

        Exchange exchange = exchangeRepository.findByFromAndTo(from, to);
//        if (exchange == null) throw new RuntimeException();

        BigDecimal conversionFactor = exchange.getConversionFactor();
        BigDecimal convertedValue = conversionFactor.multiply(amount);
        exchange.setConvertedValue(convertedValue);
        exchange.setEnviroment("PORT: " + instanceInformationService.retrieveServerPort());
        return exchange;

    }

}
