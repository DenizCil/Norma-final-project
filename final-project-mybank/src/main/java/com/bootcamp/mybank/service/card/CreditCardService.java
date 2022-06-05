package com.bootcamp.mybank.service.card;

import com.bootcamp.mybank.request.transaction.CardTransactionRequest;
import com.bootcamp.mybank.request.transaction.DebtOnAccountRequest;
import com.bootcamp.mybank.request.transaction.DebtOnCardRequest;
import com.bootcamp.mybank.request.transaction.TransactionDate;
import com.bootcamp.mybank.entity.transaction.CardTransaction;
import com.bootcamp.mybank.request.card.CreateCreditCardRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface CreditCardService {

    public ResponseEntity<Object> createCredit(CreateCreditCardRequest request);

    ResponseEntity<Object> withdrawBalance(CardTransactionRequest request) throws IOException;

    ResponseEntity<Object> debtInquiry(String cardNumber);

    ResponseEntity<Object> debtOnDepositAccount(DebtOnAccountRequest request) throws IOException;

    ResponseEntity<Object> debtOnSavingAccount(DebtOnAccountRequest request) throws IOException;

    ResponseEntity<Object> debtOnDebitCard(DebtOnCardRequest request) throws IOException;

    ResponseEntity<Object> deleteCreditCardByCardNumber(String cardNumber);

    List<CardTransaction> findByTransactionCardTypeAndCardNo(String cardNo);

    List<CardTransaction> findTransactionDateBetweenAndCardNo(TransactionDate date, String cardNo) throws Exception;

    public ResponseEntity<Object> getMaxCreditLimit(long customerId);
}
