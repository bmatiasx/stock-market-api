package com.eurekalabs.stockmarketapi.controller;

import com.eurekalabs.stockmarketapi.error.UserValidationException;
import com.eurekalabs.stockmarketapi.model.Price;
import com.eurekalabs.stockmarketapi.model.User;
import com.eurekalabs.stockmarketapi.service.StockMarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/stock-market")
public class StockMarketController {


    public StockMarketService service;
    LocalDateTime time;

    @Autowired
    public StockMarketController(StockMarketService service) {
        this.service = service;
    }

    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() {
        log.info("welcome endpoint was called");
        return new ResponseEntity<>("Welcome to Stock Market API", HttpStatus.OK);
    }

    @PostMapping(path = "/api-key")
    @ResponseBody
    public ResponseEntity<Object> getApiKey(@RequestBody User user) {
        Map<String, String> apiKey;
        log.info(String.format("api-key endpoint was called. Request payload: %s",  user.toString()));
        try {
            apiKey = service.getApiKey(user);
        } catch (UserValidationException e) {
            log.error(String.format("validation error: %s",  e.getMessage()));
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(apiKey, HttpStatus.OK);
    }

    @GetMapping("/price")
    public ResponseEntity<?> getStockPrice(@RequestParam("key") String apiKey, @RequestParam("function") String function,
                                           @RequestParam("symbol") String symbol, @RequestParam("interval") String interval) {
        log.info(String.format("getStockPrice endpoint was called. Request params: key=%s, function=%s, symbol=%s, interval=%s",  apiKey, function, symbol, interval));

        Price price;
        try {
            price = service.getPriceIntraday(apiKey, function, symbol, interval);
        } catch (UserValidationException e) {
            log.error(String.format("validation error: %s", e.getMessage()));
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error(String.format("internal server error: %s", e.getMessage()));
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(price);
    }
}
