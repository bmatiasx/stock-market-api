package com.eurekalabs.stockmarketapi.rest;

import com.eurekalabs.stockmarketapi.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class AlphaVantageClient {

    @Value("${client.url}")
    public String ALPHA_VANTAGE_URL;

    RestTemplate restTemplate = new RestTemplate();

    public User getAPIKey(String data) {
        return restTemplate.getForObject(ALPHA_VANTAGE_URL, User.class);
    }

    public Price getStockPrice(String apiKey, String function, String symbol, String interval) {

        String url = ALPHA_VANTAGE_URL + "/query?function=" + function + "&symbol=" + symbol + "&interval=" + interval + "&apikey=" + apiKey;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);

        Map<String, Object> priceMap = (HashMap<String, Object>) response.getBody();
        Map<String, Object> metadataMap = (HashMap<String, Object>) priceMap.get("Meta Data");
        Map<String, Object> seriesMap = (HashMap<String, Object>) priceMap.get(String.format("Time Series (%s)", interval));
        Map<String, Object> valuesMap = new HashMap<>();

        // Populate metadata
        Metadata metadata = new Metadata();
        metadata.setInformation((String) metadataMap.get("1. Information"));
        metadata.setSymbol((String) metadataMap.get("2. Symbol"));
        metadata.setLastRefreshed(LocalDateTime.parse((String) metadataMap.get("3. Last Refreshed"), format));
        metadata.setInterval((String) metadataMap.get("4. Interval"));
        metadata.setOutputSize((String) metadataMap.get("5. Output Size"));
        metadata.setTimezone((String) metadataMap.get("6. Time Zone"));

        // Populate samples
        List<Sample> samples = new ArrayList<>();
        for (Map.Entry<String, Object> e : seriesMap.entrySet()) {
            String key = e.getKey();

            valuesMap = (Map<String, Object>) e.getValue();

            Sample sample = new Sample();
            Variations variations = new Variations();

            variations.setOpen(new BigDecimal((String) valuesMap.get("1. open")));
            variations.setHigh(new BigDecimal((String) valuesMap.get("2. high")));
            variations.setLow(new BigDecimal((String) valuesMap.get("3. low")));
            variations.setClose(new BigDecimal((String) valuesMap.get("4. close")));
            variations.setVolume(Integer.parseInt((String) valuesMap.get("5. volume")));

            sample.setDateTime(LocalDateTime.parse(key, format));
            sample.setVariations(variations);

            samples.add(sample);
        }


        Price price = new PriceBuilder().withMetadata(metadata).withTimeSeries(samples).build();

        return price;
    }
}
