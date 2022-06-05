package com.bootcamp.mybank.service.impl.card;


import com.bootcamp.mybank.request.transaction.CardTransactionRequest;
import com.bootcamp.mybank.request.transaction.TransactionDate;
import com.bootcamp.mybank.entity.card.DebitCard;
import com.bootcamp.mybank.entity.operation.OperationType;
import com.bootcamp.mybank.entity.transaction.CardTransaction;
import com.bootcamp.mybank.repository.CustomerRepository;
import com.bootcamp.mybank.repository.card.DebitCardRepository;
import com.bootcamp.mybank.request.card.CreateDebitCardRequest;
import com.bootcamp.mybank.service.card.CardService;
import com.bootcamp.mybank.service.card.DebitCardService;
import com.bootcamp.mybank.service.transaction.CardTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class DebitCardServiceImpl implements DebitCardService {

    @Autowired
    private CardService cardService;

    @Autowired
    private CardTransactionService cardTransactionService;

    @Autowired
    private DebitCardRepository debitCardRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ResponseEntity<Object> createDebitCard(CreateDebitCardRequest request) {

        DebitCard debitCard = new DebitCard();
        return cardService.createDebitCard(debitCard,request);
    }
    @Override
    public ResponseEntity<Object> withdrawBalance(CardTransactionRequest request) throws IOException {
        return cardTransactionService.debitCardTransaction(request,"WithdrawBalance");
    }

    @Override
    public ResponseEntity<Object> addBalance(CardTransactionRequest request) throws IOException {
        return cardTransactionService.debitCardTransaction(request,"AddBalance");
    }

    @Override
    public ResponseEntity<Object> deleteDebitByCardNumber(String cardNumber) throws IOException {

        DebitCard debitCard = debitCardRepository.findByCardNumber(cardNumber);

        if(debitCard==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Debit card not found");
        }

        debitCardRepository.delete(debitCard);


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Debit card deleted");
    }

    @Override
    public List<CardTransaction> findTransactionDateBetweenAndCardNo(TransactionDate date, String cardNo) throws Exception {
        DebitCard debitCard = new DebitCard();
        return cardTransactionService.findByDateBetweenAndCardNo(debitCard,date,cardNo);
    }


}
