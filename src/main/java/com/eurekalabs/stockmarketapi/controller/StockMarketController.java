package com.eurekalabs.stockmarketapi.controller;

import com.eurekalabs.stockmarketapi.error.UserValidationException;
import com.eurekalabs.stockmarketapi.model.Price;
import com.eurekalabs.stockmarketapi.model.User;
import com.eurekalabs.stockmarketapi.service.StockMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock-market")
public class StockMarketController {

    public StockMarketService service;

    @Autowired
    public StockMarketController(StockMarketService service) {
        this.service = service;
    }

    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() {
        return new ResponseEntity<>("Welcome to Stock Market API", HttpStatus.OK);
    }

    @PostMapping(path = "/api-key")
    @ResponseBody
    public ResponseEntity<String> getApiKey(@RequestBody User user) {
        String apiKey;

        try {
            apiKey = service.getApiKey(user);
        } catch (UserValidationException e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(apiKey);
    }

    @GetMapping("/price-intraday")
    public ResponseEntity<?> getStockPrice(@RequestParam("key") String apiKey, @RequestParam("function") String function,
                                           @RequestParam("symbol") String symbol, @RequestParam("interval") String interval) {
        Price price;
        try {
            price = service.getPriceIntraday(apiKey, function, symbol, interval);
        } catch (UserValidationException e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(price);
    }
}
