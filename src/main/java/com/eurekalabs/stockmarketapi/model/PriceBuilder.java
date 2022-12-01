package com.eurekalabs.stockmarketapi.model;

import java.util.List;

public class PriceBuilder {
    private Price price;

    public PriceBuilder() {
        this.price = new Price();
    }

    public PriceBuilder withMetadata(Metadata metadata) {
        this.price.setMetadata(metadata);
        return this;
    }

    public PriceBuilder withTimeSeries(List<Sample> series) {
        this.price.setTimeSeries(series);
        return this;
    }

    public Price build() {
        return this.price;
    }
}

