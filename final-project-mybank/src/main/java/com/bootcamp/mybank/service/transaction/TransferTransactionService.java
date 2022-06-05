package com.bootcamp.mybank.service.transaction;

import com.bootcamp.mybank.request.transaction.TransferTransactionRequest;
import com.bootcamp.mybank.entity.account.Account;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface TransferTransactionService {

    public <T extends Account> ResponseEntity<Object> sendMoney(T a, T b, TransferTransactionRequest request) throws IOException;


}