package com.bootcamp.mybank.service.account;

import com.bootcamp.mybank.request.transaction.AccountTransactionRequest;
import com.bootcamp.mybank.request.transaction.TransactionDate;
import com.bootcamp.mybank.request.transaction.TransferTransactionRequest;
import com.bootcamp.mybank.entity.transaction.AccountTransaction;
import com.bootcamp.mybank.request.account.CreateSavingAccountRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface SavingAccountService {

    public ResponseEntity<Object> createAccount(CreateSavingAccountRequest request) throws IOException;
    public ResponseEntity<Object> addBalance(AccountTransactionRequest request) throws IOException;
    public ResponseEntity<Object> withdrawBalance(AccountTransactionRequest request) throws IOException;
    public ResponseEntity<Object> sendMoneyToDeposit(TransferTransactionRequest request) throws IOException;
    public ResponseEntity<Object> sendMoneyToSaving(TransferTransactionRequest request) throws IOException;
    public ResponseEntity<Object> deleteAccount(String accountNumber);
    public List<AccountTransaction> findTransaction(TransactionDate date, String accountNumber) throws Exception;
    public ResponseEntity<Object> withDrawAllMoney(String accountNumber) throws IOException;
}
