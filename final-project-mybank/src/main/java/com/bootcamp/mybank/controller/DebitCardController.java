package com.bootcamp.mybank.controller;

import com.bootcamp.mybank.request.transaction.CardTransactionRequest;
import com.bootcamp.mybank.request.transaction.TransactionDate;
import com.bootcamp.mybank.service.card.DebitCardService;
import com.bootcamp.mybank.entity.transaction.CardTransaction;
import com.bootcamp.mybank.repository.transaction.CardTransactionRepository;
import com.bootcamp.mybank.request.card.CreateDebitCardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/customer/account/deposit/debitCard")
public class DebitCardController {

    @Autowired
    private DebitCardService debitCardService;

    @Autowired
    private CardTransactionRepository cardTransactionRepository;


    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody CreateDebitCardRequest request){
        return debitCardService.createDebitCard(request);
    }

    @PostMapping("/addBalance")
    public ResponseEntity<Object> addBalance(@RequestBody CardTransactionRequest request) throws IOException {

        try {
            return debitCardService.addBalance(request);
        }
        catch (Exception e){

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("This debit card already used in another transaction.");
        }
    }

    @PostMapping("/withDrawBalance")
    public ResponseEntity<Object> withdrawBalance(@RequestBody CardTransactionRequest request) throws IOException {

        try {
            return debitCardService.withdrawBalance(request);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("This debit card already used in another transaction.");
        }
    }

    @DeleteMapping("/deleteByCardNumber/{cardNumber}")
    private ResponseEntity<Object> deleteById(@PathVariable String cardNumber) throws IOException {
        return debitCardService.deleteDebitByCardNumber(cardNumber);
    }

    @GetMapping("/list/allTransactions/{cardNumber}")
    public List<CardTransaction> findTransaction(@PathVariable String cardNumber){

        return cardTransactionRepository.findByCardNo(cardNumber);
    }

    @PostMapping("/list/transaction/byDate/{cardNo}")
    public List<CardTransaction> findAccountTransactionByDate(@RequestBody TransactionDate date, @PathVariable String cardNo) throws Exception {
        try {
            return debitCardService.findTransactionDateBetweenAndCardNo(date,cardNo);
        }catch (Exception e){
            throw new Exception("Card not found");
        }
    }
}
