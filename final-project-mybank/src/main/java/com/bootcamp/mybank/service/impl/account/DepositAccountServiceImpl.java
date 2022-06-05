package com.bootcamp.mybank.service.impl.account;

import com.bootcamp.mybank.request.transaction.AccountTransactionRequest;
import com.bootcamp.mybank.request.transaction.TransactionDate;
import com.bootcamp.mybank.request.transaction.TransferTransactionRequest;
import com.bootcamp.mybank.service.account.AccountService;
import com.bootcamp.mybank.entity.account.DepositAccount;
import com.bootcamp.mybank.entity.account.SavingAccount;
import com.bootcamp.mybank.entity.transaction.AccountTransaction;
import com.bootcamp.mybank.repository.account.DepositAccountRepository;
import com.bootcamp.mybank.request.account.CreateAccountRequest;
import com.bootcamp.mybank.service.account.DepositAccountService;
import com.bootcamp.mybank.service.transaction.AccountTransactionService;
import com.bootcamp.mybank.service.transaction.TransferTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.io.IOException;

@Service
public class DepositAccountServiceImpl implements DepositAccountService {

    @Autowired
    private final AccountService accountService;

    @Autowired
    private final AccountTransactionService accountTransactionService;

    @Autowired
    private final TransferTransactionService transactionService;

    @Autowired
    private DepositAccountRepository depositAccountRepository;


    public DepositAccountServiceImpl(AccountService accountService, AccountTransactionService accountTransactionService, TransferTransactionService transactionService) {

        this.accountService = accountService;
        this.accountTransactionService = accountTransactionService;

        this.transactionService = transactionService;
    }

    @Override
    public ResponseEntity<Object> createAccount(CreateAccountRequest request) throws IOException {
        DepositAccount depositAccount = new DepositAccount();
        return accountService.createAccount(depositAccount,request);
    }

    @Override
    public ResponseEntity<Object> addBalance(AccountTransactionRequest request) throws IOException {

        DepositAccount depositAccount = new DepositAccount();
        String type="AddBalance";
        return accountTransactionService.balanceOnAccount(depositAccount,type,request);


    }

    @Override
    public ResponseEntity<Object> withdrawBalance(AccountTransactionRequest request) throws IOException {
        DepositAccount depositAccount = new DepositAccount();
        String type="WithdrawBalance";
        return accountTransactionService.balanceOnAccount(depositAccount,type,request);
    }

    @Override
    public ResponseEntity<Object> sendMoneyToDeposit(TransferTransactionRequest request)throws IOException {

        DepositAccount sender = new DepositAccount();
        DepositAccount receiver = new DepositAccount();

        return transactionService.sendMoney(sender,receiver,request);

    }

    @Override
    public ResponseEntity<Object> sendMoneyToSaving(TransferTransactionRequest request) throws IOException {
        DepositAccount sender = new DepositAccount();
        SavingAccount receiver = new SavingAccount();

        return transactionService.sendMoney(sender,receiver,request);
    }

    @Override
    public ResponseEntity<Object> deleteAccount(String accountNumber){

        DepositAccount depositAccount = new DepositAccount();
        return accountService.deleteAccount(depositAccount,accountNumber);

    }

    public List<AccountTransaction> findTransaction(TransactionDate date, String accountNumber) throws Exception {

        DepositAccount depositAccount = new DepositAccount();
        return accountTransactionService.findAccountTransactionByDateAndAccountNumber(depositAccount,date,accountNumber);
    }

    @Override
    public ResponseEntity<Object> withDrawAllMoney(String accountNumber) throws IOException {
        DepositAccount depositAccount = new DepositAccount();
        return accountTransactionService.withDrawAllMoney(depositAccount,accountNumber);

    }

    @Override
    public ResponseEntity<Object> autoPaymentRequest(String accountNumber, double amount) {
        return null;
    }

}

