package com.eurekalabs.stockmarketapi.service;

import com.eurekalabs.stockmarketapi.error.UserValidationException;
import com.eurekalabs.stockmarketapi.model.Price;
import com.eurekalabs.stockmarketapi.model.User;
import com.eurekalabs.stockmarketapi.rest.AlphaVantageClient;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StockMarketService {

    @Value("${client.apikey}")
    private String API_KEY;

    @Autowired
    private AlphaVantageClient client;

    public Map<String, String> getApiKey(User user) {
        if (!validateName(user.getName())) {
            throw new UserValidationException("not valid user");
        }
        if (!validateLastname(user.getLastname())) {
            throw new UserValidationException("not valid lastname");
        }
        if (!validateEmail(user.getEmail())) {
            throw new UserValidationException("not valid email");
        }

        Map<String, String> key = new HashMap<>();
        key.put("apikey", API_KEY);

        return key;
    }

    public Price getPriceIntraday(String apiKey, String function, String symbol, String interval) {
        if (!validateApiKey(apiKey)) {
            throw new UserValidationException("not valid apikey");
        }

        return client.getStockPrice(apiKey, function, symbol, interval);
    }

    public boolean validateName(String name) {
        return !Strings.isBlank(name) && name.matches("[a-zA-Z]+");
    }

    public boolean validateLastname(String lastname) {
        return !Strings.isBlank(lastname) && lastname.matches("[a-zA-Z]+");
    }

    public boolean validateEmail(String email) {
        return !Strings.isBlank(email) && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public boolean validateApiKey(String key) {
        return API_KEY.equals(key);
    }
}
