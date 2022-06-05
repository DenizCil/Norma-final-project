package com.bootcamp.mybank.service.currency;

import com.bootcamp.mybank.entity.account.Account;

import java.io.IOException;

public interface CurrencyService {

    public void setCustomerAsset(double amount, Account account,String txnType) throws IOException;

    public double setCurrencyAsset(double amount,Account account) throws IOException;
    public double setAccountBalance(double amount, Account account) throws IOException;
}
