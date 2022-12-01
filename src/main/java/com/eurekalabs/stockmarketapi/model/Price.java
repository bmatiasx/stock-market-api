package com.eurekalabs.stockmarketapi.model;

import lombok.Data;

import java.util.List;

// This class will map the json response from external API
@Data
public class Price {
    private Metadata metadata;
    private List<Sample> timeSeries;
}
