package com.example.products.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DiscountRequest {

    @NotNull 
    @Min(1)
    @Max(100)
    Double percentage;

    public DiscountRequest() {
    }


    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    


}
