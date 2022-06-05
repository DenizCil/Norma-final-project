package com.bootcamp.mybank.request.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTransactionRequest {

    private String accountNumber;
    private double amount;

}
