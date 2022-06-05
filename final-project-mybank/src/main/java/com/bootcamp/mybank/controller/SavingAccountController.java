package com.bootcamp.mybank.controller;

import com.bootcamp.mybank.request.transaction.AccountTransactionRequest;
import com.bootcamp.mybank.request.transaction.TransactionDate;
import com.bootcamp.mybank.request.transaction.TransferTransactionRequest;
import com.bootcamp.mybank.service.account.SavingAccountService;

import com.bootcamp.mybank.entity.transaction.AccountTransaction;
import com.bootcamp.mybank.repository.transaction.AccountTransactionRepository;
import com.bootcamp.mybank.request.account.CreateSavingAccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("customer/account/saving")
public class SavingAccountController {

    @Autowired
    private SavingAccountService savingAccountService;

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;


    @PostMapping("/create")
    public ResponseEntity<Object> createAccount(@RequestBody CreateSavingAccountRequest request) throws IOException {
        return savingAccountService.createAccount(request);
    }
    @PostMapping("/addBalance")
    public ResponseEntity<Object> addBalance(@RequestBody AccountTransactionRequest request) throws IOException {

        try {
            return savingAccountService.addBalance(request);
        }catch (Exception e){

            return ResponseEntity.status(HttpStatus.LOCKED).body("Saving account already used in another transaction");
        }
    }

    @PostMapping("/withdrawBalance")
    public ResponseEntity<Object> withdrawBalance(@RequestBody AccountTransactionRequest request) throws IOException {

        try {
            return savingAccountService.withdrawBalance(request);
        }catch (Exception e){

            return ResponseEntity.status(HttpStatus.LOCKED).body("Saving account already used in another transaction");
        }
    }

    @PostMapping("/send/deposit")
    public ResponseEntity<Object> sendMoneyToDeposit(@RequestBody TransferTransactionRequest request) throws IOException {
        try {

            return savingAccountService.sendMoneyToDeposit(request);
        }catch (Exception e){

            return ResponseEntity.status(HttpStatus.LOCKED).body("This account already used in another transaction");
        }
    }


    @PostMapping("/send/saving")
    public ResponseEntity<Object> sendMoneyToSaving(@RequestBody TransferTransactionRequest request) throws IOException {

        try {

            return savingAccountService.sendMoneyToSaving(request);
        }catch (Exception e){

            return ResponseEntity.status(HttpStatus.LOCKED).body("This account already used in another transaction");
        }

    }

    @DeleteMapping("/deleteByAccountNumber/{accountNumber}")
    private ResponseEntity<Object> deleteAccount(@PathVariable String accountNumber){

        return savingAccountService.deleteAccount(accountNumber);

    }

    @GetMapping("/list/allTransactions/{accountNumber}")
    private List<AccountTransaction> findTransaction(@PathVariable String accountNumber){

        return accountTransactionRepository.findByAccountNumber(accountNumber);

    }

    @PostMapping("/list/transaction/byDate/{accountNumber}")
    public List<AccountTransaction> findAccountTransactionByDate(@RequestBody TransactionDate date, @PathVariable String accountNumber) throws Exception {

        try {
            return savingAccountService.findTransaction(date,accountNumber);
        }catch (Exception e){
            throw new Exception("Account not found");
        }
    }

    @PostMapping("/withdrawBalance/allBalance/{accountNumber}")
    public ResponseEntity<Object> withDrawAllMoney(@PathVariable String accountNumber) throws IOException {
        return savingAccountService.withDrawAllMoney(accountNumber);
    }

}
