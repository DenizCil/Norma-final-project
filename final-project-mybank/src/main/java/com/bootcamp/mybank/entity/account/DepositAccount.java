package com.bootcamp.mybank.entity.account;

import com.bootcamp.mybank.entity.card.DebitCard;
import lombok.Getter;
import lombok.Setter;
import com.bootcamp.mybank.entity.customer.Customer;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class DepositAccount extends Account{


    @OneToMany(mappedBy = "depositAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DebitCard> debitCards;


    public DepositAccount() {
    }

    public DepositAccount(long accountId, String accountNumber, String accountType, String ibanNo, CurrencyType currencyType, Customer customer) {
        super(accountId, accountNumber, accountType, ibanNo, currencyType, customer);
    }
}