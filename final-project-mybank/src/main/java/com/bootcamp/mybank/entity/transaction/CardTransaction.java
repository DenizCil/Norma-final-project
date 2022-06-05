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
@AllArgsConstructor
@NoArgsConstructor
public class CardTransaction extends Transaction{

    private double amount;
    private String transactionType;
    private Timestamp date;
    private String cardNo;
    private String transactionCardType;
    private String currencyType;


}
