package com.bootcamp.mybank.request.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDate {

    private String startYear;
    private String startMonth;
    private String startDay;

    private String endYear;
    private String endMonth;
    private String endDay;

}
