package com.bootcamp.mybank.service.transaction;


import java.util.*;

import com.bootcamp.mybank.request.transaction.AccountTransactionRequest;
import com.bootcamp.mybank.request.transaction.DebtOnAccountRequest;
import com.bootcamp.mybank.request.transaction.TransactionDate;
import com.bootcamp.mybank.entity.account.Account;
import com.bootcamp.mybank.entity.transaction.AccountTransaction;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface AccountTransactionService {

    public <T extends Account> ResponseEntity<Object> balanceOnAccount(T a, String transactionType, AccountTransactionRequest request) throws IOException;
    public <T extends Account> ResponseEntity<Object> debtOnAccount(T a, DebtOnAccountRequest request) throws IOException;
    public <T extends Account> List<AccountTransaction> findAccountTransactionByDateAndAccountNumber(T a, TransactionDate date, String accountNumber) throws Exception;
    public <T extends Account> ResponseEntity<Object> withDrawAllMoney(T a,String accountNumber) throws IOException;
}
