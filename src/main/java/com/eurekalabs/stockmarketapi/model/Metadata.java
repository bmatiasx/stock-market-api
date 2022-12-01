package com.eurekalabs.stockmarketapi.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Metadata {
    private String information;
    private String symbol;
    private LocalDateTime lastRefreshed;
    private String interval;
    private String outputSize;
    private String timezone;
}
