package com.bootcamp.mybank.request.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebtOnAccountRequest {

    private String accountNumber;
    private String cardNumber;
    private double debt;

}
