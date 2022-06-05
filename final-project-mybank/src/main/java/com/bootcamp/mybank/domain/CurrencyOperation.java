package com.bootcamp.mybank.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyOperation {

    @JsonProperty("date")
    private Date date;
    @JsonProperty("rates")
    private Map<String, Double> rates;
}
