package com.bootcamp.mybank.service.card;

import com.bootcamp.mybank.entity.card.CreditCard;
import com.bootcamp.mybank.entity.card.DebitCard;
import com.bootcamp.mybank.request.card.CreateCreditCardRequest;
import com.bootcamp.mybank.request.card.CreateDebitCardRequest;
import org.springframework.http.ResponseEntity;

public interface CardService {

    public ResponseEntity<Object> createCreditCard(CreditCard creditCard, CreateCreditCardRequest request);
    public ResponseEntity<Object> createDebitCard(DebitCard debitCard, CreateDebitCardRequest request);

}
