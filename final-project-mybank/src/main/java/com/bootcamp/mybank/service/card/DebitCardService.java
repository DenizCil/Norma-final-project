package com.bootcamp.mybank.service.card;

import com.bootcamp.mybank.request.transaction.CardTransactionRequest;
import com.bootcamp.mybank.request.transaction.TransactionDate;
import com.bootcamp.mybank.entity.transaction.CardTransaction;
import com.bootcamp.mybank.request.card.CreateDebitCardRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface DebitCardService {

    ResponseEntity<Object> createDebitCard(CreateDebitCardRequest request);

    ResponseEntity<Object> withdrawBalance(CardTransactionRequest request) throws IOException;

    ResponseEntity<Object> addBalance(CardTransactionRequest request) throws IOException;

    ResponseEntity<Object> deleteDebitByCardNumber(String cardNumber) throws IOException;

    List<CardTransaction> findTransactionDateBetweenAndCardNo(TransactionDate date, String cardNo) throws Exception;
}
