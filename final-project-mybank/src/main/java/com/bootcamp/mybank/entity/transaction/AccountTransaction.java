package com.bootcamp.mybank.entity.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AccountTransaction extends Transaction {

    private String accountNumber;
    private String currencyType;
    private double amount;
    private String transactionType;
    private Timestamp date;


}
