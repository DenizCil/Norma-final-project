package com.bootcamp.mybank.service.transaction;

import com.bootcamp.mybank.request.transaction.AccountTransactionRequest;
import com.bootcamp.mybank.request.transaction.CardTransactionRequest;
import com.bootcamp.mybank.request.transaction.DebtOnAccountRequest;
import com.bootcamp.mybank.request.transaction.TransferTransactionRequest;
import com.bootcamp.mybank.domain.CurrencyOperation;
import com.bootcamp.mybank.entity.customer.Customer;
import com.bootcamp.mybank.entity.account.Account;

public interface SaveTransactionService {

    public void saveBalanceOnAccount(String txnType, Account account, AccountTransactionRequest request);
    public void saveDebtOnAccount(Account account, DebtOnAccountRequest request, double txnAmount);
    public void saveCreditCardTransaction(CardTransactionRequest request, Customer customer, String txnType);
    public void saveDebitCardTransaction(CardTransactionRequest request, Account account, String txnType,double txnAmount);
    public void saveTransferTransaction(TransferTransactionRequest request, Account sender, Account receiver, String txnType, CurrencyOperation currencyClass);
    public void saveTransferTransactionBetweenAccounts(Account account,double amount,String txnType);
    public void saveAutoPaymentTransaction(Account account,double amount,String txnType);

}
