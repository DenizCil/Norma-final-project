package com.bootcamp.mybank.request.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSavingAccountRequest {

    private String currencyType;
    private long customerId;
    private int day;
    private double balance;
}
