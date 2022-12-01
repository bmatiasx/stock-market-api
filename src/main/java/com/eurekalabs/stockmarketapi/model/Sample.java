package com.eurekalabs.stockmarketapi.model;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Sample {
    private LocalDateTime dateTime;
    private Variations variations;
}
