package com.bootcamp.mybank.service.impl.card;

import com.bootcamp.mybank.entity.customer.Customer;
import com.bootcamp.mybank.entity.account.DepositAccount;
import com.bootcamp.mybank.entity.card.Card;
import com.bootcamp.mybank.entity.card.CreditCard;
import com.bootcamp.mybank.entity.card.DebitCard;
import com.bootcamp.mybank.generator.CardNumberGenerator;
import com.bootcamp.mybank.generator.SecurityCodeGenerator;
import com.bootcamp.mybank.repository.CustomerRepository;
import com.bootcamp.mybank.repository.account.DepositAccountRepository;
import com.bootcamp.mybank.repository.card.CreditCardRepository;
import com.bootcamp.mybank.repository.card.DebitCardRepository;
import com.bootcamp.mybank.request.card.CreateCreditCardRequest;
import com.bootcamp.mybank.request.card.CreateDebitCardRequest;
import com.bootcamp.mybank.service.card.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private DepositAccountRepository depositAccountRepository;

    @Autowired
    private DebitCardRepository debitCardRepository;

    CardNumberGenerator cardNumberGenerator=new CardNumberGenerator();

    SecurityCodeGenerator securityCodeGenerator = new SecurityCodeGenerator();

    public CardServiceImpl(CustomerRepository customerRepository, CreditCardRepository creditCardRepository, DepositAccountRepository depositAccountRepository, DebitCardRepository debitCardRepository) {
        this.customerRepository = customerRepository;
        this.creditCardRepository = creditCardRepository;
        this.depositAccountRepository = depositAccountRepository;
        this.debitCardRepository = debitCardRepository;
    }

    public CardServiceImpl() {

    }

    @Override
    public ResponseEntity<Object> createCreditCard(CreditCard creditCard, CreateCreditCardRequest request){

        Customer customer =customerRepository.findById(request.getCustomer_id());

        if(customer==null){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        boolean isUnique=true;
        while (isUnique){

            String cardNumber = cardNumberGenerator.generateCardNumber();

            if(creditCardRepository.findByCardNumber(cardNumber)!=null){
                continue;
            }

            creditCard.setCardNumber(cardNumber);
            isUnique=false;
        }


        creditCard.setCardLimit(request.getCreditLimit());
        creditCard.setCardType(creditCard.getClass().getSimpleName());
        creditCard.setCurrentLimit(request.getCreditLimit());
        creditCard.setExpiredDate(this.expiredDate());
        creditCard.setUsable(true);
        creditCard.setCustomer(customer);
        creditCard.setSecurityNumber(securityCodeGenerator.generateSecurityCode());


        return null;
    }

    @Override
    public ResponseEntity<Object> createDebitCard(DebitCard debitCard, CreateDebitCardRequest request) {

        DepositAccount depositAccount = depositAccountRepository.findByAccountNumber(request.getAccountNumber());

        if(depositAccount==null){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");

        }
        boolean isUnique=true;

        while (isUnique){

            String cardNumber = cardNumberGenerator.generateCardNumber();
            if(debitCardRepository.findByCardNumber(cardNumber)!=null){
                continue;
            }
            debitCard.setCardNumber(cardNumber);
            isUnique=false;
        }


        debitCard.setCardType(debitCard.getClass().getSimpleName());
        debitCard.setExpiredDate(this.expiredDate());
        debitCard.setDepositAccount(depositAccount);
        debitCard.setUsable(true);
        debitCard.setSecurityNumber(securityCodeGenerator.generateSecurityCode());

        debitCardRepository.save(debitCard);

        return ResponseEntity.status(HttpStatus.OK).body("Debit card created");
    }


    public String expiredDate(){

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR)+5;
        String expiredDate= month +"/"+ year;

        return expiredDate;

    }

    public void isUsable(){


        Calendar calendar = Calendar.getInstance();
        int month =calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR)+1;

        String montString = String.valueOf(month);
        String yearString = String.valueOf(year);

        List<Card> creditCards=creditCardRepository.findAll();

        for (Card creditCard : creditCards) {

            if (creditCard.getExpiredDate().equals(montString+"/"+yearString)) {

                creditCard.setUsable(false);
                creditCardRepository.save(creditCard);
            }

        }
    }


}

