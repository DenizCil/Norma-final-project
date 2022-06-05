package com.bootcamp.mybank.entity.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.bootcamp.mybank.entity.customer.Customer;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountId;

    private String accountNumber;
    private String accountType;
    private String ibanNo;

    private double balance;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @ManyToOne
    private Customer customer;

    public Account(long accountId, String accountNumber, String accountType, String ibanNo, CurrencyType currencyType, Customer customer) {
    }
}
