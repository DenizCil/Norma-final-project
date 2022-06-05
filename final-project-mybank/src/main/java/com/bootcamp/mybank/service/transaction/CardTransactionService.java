package com.bootcamp.mybank.service.transaction;


import com.bootcamp.mybank.request.transaction.CardTransactionRequest;
import com.bootcamp.mybank.request.transaction.DebtOnCardRequest;
import com.bootcamp.mybank.request.transaction.TransactionDate;
import com.bootcamp.mybank.entity.account.Account;
import com.bootcamp.mybank.entity.card.Card;
import com.bootcamp.mybank.entity.transaction.CardTransaction;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface CardTransactionService {

    public ResponseEntity<Object> withdrawCreditCard(CardTransactionRequest transaction);

    public ResponseEntity<Object> debitCardTransaction(CardTransactionRequest transaction,String transactionType) throws IOException;

    public <T extends Account> ResponseEntity<Object> debtOnCard(T a, DebtOnCardRequest request) throws IOException;

    public <T extends Card> List<CardTransaction> findByDateBetweenAndCardNo(T a, TransactionDate date, String cardNo) throws Exception;


}