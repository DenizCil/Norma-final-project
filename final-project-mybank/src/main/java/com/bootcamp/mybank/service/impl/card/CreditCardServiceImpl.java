package com.bootcamp.mybank.service.impl.card;

import com.bootcamp.mybank.request.transaction.CardTransactionRequest;
import com.bootcamp.mybank.request.transaction.DebtOnAccountRequest;
import com.bootcamp.mybank.request.transaction.DebtOnCardRequest;
import com.bootcamp.mybank.request.transaction.TransactionDate;
import com.bootcamp.mybank.entity.account.DepositAccount;
import com.bootcamp.mybank.entity.account.SavingAccount;
import com.bootcamp.mybank.entity.card.CreditCard;
import com.bootcamp.mybank.entity.customer.Customer;
import com.bootcamp.mybank.entity.transaction.CardTransaction;
import com.bootcamp.mybank.repository.CustomerRepository;
import com.bootcamp.mybank.repository.card.CreditCardRepository;
import com.bootcamp.mybank.repository.transaction.CardTransactionRepository;
import com.bootcamp.mybank.request.card.CreateCreditCardRequest;
import com.bootcamp.mybank.service.card.CardService;
import com.bootcamp.mybank.service.card.CreditCardService;
import com.bootcamp.mybank.service.transaction.AccountTransactionService;
import com.bootcamp.mybank.service.transaction.CardTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CreditCardServiceImpl implements CreditCardService {


    @Autowired
    private CardService cardService;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CardTransactionService cardTransactionService;

    @Autowired
    private CardTransactionRepository cardTransactionRepository;

    @Autowired
    private AccountTransactionService accountTransactionService;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ResponseEntity<Object> createCredit(CreateCreditCardRequest request) {

        CreditCard creditCard = new CreditCard();
        return cardService.createCreditCard(creditCard,request);
    }

    @Override
    public ResponseEntity<Object> withdrawBalance(CardTransactionRequest request) throws IOException {
        return cardTransactionService.withdrawCreditCard(request);
    }

    @Override
    public ResponseEntity<Object> debtInquiry(String cardNumber) {

        CreditCard creditCard =creditCardRepository.findByCardNumber(cardNumber);
        if(creditCard==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credit card not found");
        }

        double debt = creditCard.getCardLimit()-creditCard.getCurrentLimit();
        return ResponseEntity.status(HttpStatus.OK).body("Credit card debt is:"+debt);
    }

    @Override
    public ResponseEntity<Object> debtOnDepositAccount(DebtOnAccountRequest request) throws IOException {

        DepositAccount depositAccount =new DepositAccount();
        return accountTransactionService.debtOnAccount(depositAccount,request);
    }

    @Override
    public ResponseEntity<Object> debtOnSavingAccount(DebtOnAccountRequest request) throws IOException {
        SavingAccount savingAccount =new SavingAccount();
        return accountTransactionService.debtOnAccount(savingAccount,request);
    }

    @Override
    public ResponseEntity<Object> debtOnDebitCard(DebtOnCardRequest request) throws IOException {

        DepositAccount depositAccount = new DepositAccount();
        return cardTransactionService.debtOnCard(depositAccount,request);

    }

    @Override
    public ResponseEntity<Object> deleteCreditCardByCardNumber(String cardNumber) {

        CreditCard creditCard = creditCardRepository.findByCardNumber(cardNumber);

        if(creditCard==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credit card not found");
        }

        double debt = creditCard.getCardLimit()-creditCard.getCurrentLimit();

        if(debt==0){

            creditCardRepository.delete(creditCard);
            return ResponseEntity.status(HttpStatus.OK).body("Credit card deleted");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Credit card have a debt.Delete operation is not allowed.");
    }

    @Override
    public List<CardTransaction> findByTransactionCardTypeAndCardNo(String cardNo) {

        String type="CreditCard";

        CreditCard creditCard = creditCardRepository.findByCardNumber(cardNo);

        if(creditCard==null){

            return null;
        }
        return cardTransactionRepository.findByCardNo(cardNo);

    }

    @Override
    public List<CardTransaction> findTransactionDateBetweenAndCardNo(TransactionDate date, String cardNo) throws Exception {

        CreditCard creditCard = new CreditCard();
        return cardTransactionService.findByDateBetweenAndCardNo(creditCard,date,cardNo);
    }

    @Override
    public ResponseEntity<Object> getMaxCreditLimit(long customerId){

        Customer customer = customerRepository.findById(customerId);
        if(customer==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");

        return null;
    }


}
