package com.bootcamp.mybank.entity.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferTransaction extends Transaction{


    private String fromIban;
    private String toIban;
    private String fromCurrencyType;
    private String toCurrencyType;
    private double amount;
    private String transactionType;
    private Timestamp date;
}
