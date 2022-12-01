package com.eurekalabs.stockmarketapi.model;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class Variations {
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private int volume;
}
