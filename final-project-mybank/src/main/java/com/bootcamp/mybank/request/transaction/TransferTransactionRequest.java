package com.bootcamp.mybank.request.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferTransactionRequest {

    private String fromIbanNo;
    private String toIbanNo;
    private double amount;

}