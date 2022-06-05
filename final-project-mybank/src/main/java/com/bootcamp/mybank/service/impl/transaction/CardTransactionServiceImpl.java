package com.bootcamp.mybank.service.impl.transaction;

import com.bootcamp.mybank.request.transaction.AccountTransactionRequest;
import com.bootcamp.mybank.request.transaction.CardTransactionRequest;
import com.bootcamp.mybank.request.transaction.DebtOnCardRequest;
import com.bootcamp.mybank.request.transaction.TransactionDate;
import com.bootcamp.mybank.checker.FormatChecker;
import com.bootcamp.mybank.entity.customer.Customer;
import com.bootcamp.mybank.entity.account.Account;
import com.bootcamp.mybank.entity.account.DepositAccount;
import com.bootcamp.mybank.entity.card.Card;
import com.bootcamp.mybank.entity.card.CreditCard;
import com.bootcamp.mybank.entity.card.DebitCard;
import com.bootcamp.mybank.entity.transaction.CardTransaction;
import com.bootcamp.mybank.repository.CustomerRepository;
import com.bootcamp.mybank.repository.account.DepositAccountRepository;
import com.bootcamp.mybank.repository.account.SavingAccountRepository;
import com.bootcamp.mybank.repository.card.CreditCardRepository;
import com.bootcamp.mybank.repository.card.DebitCardRepository;
import com.bootcamp.mybank.repository.transaction.AccountTransactionRepository;
import com.bootcamp.mybank.repository.transaction.CardTransactionRepository;
import com.bootcamp.mybank.service.currency.CurrencyService;
import com.bootcamp.mybank.service.transaction.CardTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CardTransactionServiceImpl implements CardTransactionService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private DepositAccountRepository depositAccountRepository;

    @Autowired
    private DebitCardRepository debitCardRepository;

    @Autowired
    private CardTransactionRepository cardTransactionRepository;

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;

    @Autowired
    private SaveTransactionServiceImpl saveTransactionServiceImpl;

    @Autowired
    private SavingAccountRepository savingAccountRepository;



    @Autowired
    private CurrencyService currencyService;

    @PersistenceContext
    private EntityManager entityManager;

    private final FormatChecker formatChecker = new FormatChecker();

    public CardTransactionServiceImpl(CustomerRepository customerRepository, CreditCardRepository creditCardRepository, DepositAccountRepository depositAccountRepository, DebitCardRepository debitCardRepository, CardTransactionRepository cardTransactionRepository) {
        this.customerRepository = customerRepository;
        this.creditCardRepository = creditCardRepository;
        this.depositAccountRepository = depositAccountRepository;
        this.debitCardRepository = debitCardRepository;
        this.cardTransactionRepository = cardTransactionRepository;
    }

    public CardTransactionServiceImpl() {

    }

    @Transactional
    @Override
    public ResponseEntity<Object> withdrawCreditCard(CardTransactionRequest transaction){

        boolean cardNumberChecker = formatChecker.numberFormatChecker(transaction.getCardNumber());

        if(!cardNumberChecker)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid card number type");


        CreditCard creditCard = creditCardRepository.findByCardNumber(transaction.getCardNumber());

        if(creditCard==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found");
        }

        if(!creditCard.isUsable()){

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Credit card expired date.Card is not usable");
        }

        if(creditCard.getCurrentLimit()>=transaction.getAmount()){

            entityManager.lock(creditCard, LockModeType.PESSIMISTIC_FORCE_INCREMENT);

            try {

                Thread.sleep(5000);
                creditCard.setCurrentLimit(creditCard.getCurrentLimit()-transaction.getAmount());
                creditCardRepository.save(creditCard);

            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Account is busy");
            }

            saveTransactionServiceImpl.saveCreditCardTransaction(transaction,creditCard.getCustomer(),"WithdrawBalance");


            return ResponseEntity.status(HttpStatus.OK).body("Transaction completed");
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Insufficient balance ");
    }

    @Transactional
    @Override
    public ResponseEntity<Object> debitCardTransaction(CardTransactionRequest transaction,String transactionType) throws IOException {

        boolean cardNumberChecker = formatChecker.numberFormatChecker(transaction.getCardNumber());

        if(!cardNumberChecker)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid card number type");


        DebitCard debitCard =debitCardRepository.findByCardNumber(transaction.getCardNumber());

        if(debitCard==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found");
        }

        DepositAccount depositAccount = debitCard.getDepositAccount();

        AccountTransactionRequest request = new AccountTransactionRequest();
        request.setAccountNumber(depositAccount.getAccountNumber());
        request.setAmount(transaction.getAmount());

        if(transactionType.equals("WithdrawBalance")){



            if(depositAccount.getBalance()>=transaction.getAmount()){

                Customer customer =depositAccount.getCustomer();

                entityManager.lock(depositAccount,LockModeType.PESSIMISTIC_FORCE_INCREMENT);

                try {

                    Thread.sleep(5000);
                    depositAccount.setBalance(depositAccount.getBalance()-transaction.getAmount());
                    currencyService.setCustomerAsset(request.getAmount(),depositAccount,"WithdrawBalance");

                    depositAccountRepository.save(depositAccount);

                }catch (Exception e){
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Account is busy");
                }

                saveTransactionServiceImpl.saveDebitCardTransaction(transaction,depositAccount,transactionType,currencyService.setAccountBalance(request.getAmount(),depositAccount));
                saveTransactionServiceImpl.saveBalanceOnAccount("WithdrawBalanceFromDebit",depositAccount,request);


                return ResponseEntity.status(HttpStatus.OK).body("Withdraw balance transaction completed");

            }

            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Insufficient balance ");
        }

        if(transactionType.equals("AddBalance")){

            Customer customer =depositAccount.getCustomer();

            entityManager.lock(depositAccount,LockModeType.PESSIMISTIC_FORCE_INCREMENT);

            try {

                Thread.sleep(5000);

                depositAccount.setBalance(depositAccount.getBalance()+transaction.getAmount());
                currencyService.setCustomerAsset(request.getAmount(),depositAccount,"AddBalance");

                customerRepository.save(customer);
                depositAccountRepository.save(depositAccount);


            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Account is busy");
            }


            saveTransactionServiceImpl.saveDebitCardTransaction(transaction,depositAccount,transactionType,currencyService.setAccountBalance(request.getAmount(),depositAccount));
            saveTransactionServiceImpl.saveBalanceOnAccount("AddBalanceFromDebit",depositAccount,request);


            return ResponseEntity.status(HttpStatus.OK).body("Add balance transaction completed");
        }

        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Timeout");
    }

    @Transactional
    @Override
    public <T extends Account> ResponseEntity<Object> debtOnCard(T a, DebtOnCardRequest request) throws IOException {

        boolean creditCardNumberChecker = formatChecker.numberFormatChecker(request.getCreditCardNumber());
        boolean debitCardNumberChecker = formatChecker.numberFormatChecker(request.getCardNumber());

        if(!creditCardNumberChecker)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credit card number type");

        if(!debitCardNumberChecker)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid debit card number type");


        CreditCard creditCard = creditCardRepository.findByCardNumber(request.getCreditCardNumber());
        DebitCard debitCard = debitCardRepository.findByCardNumber(request.getCardNumber());

        if(creditCard==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credit card not found");
        }
        if(debitCard==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Debit card not found");
        }


        Account account = null;
        Customer customer = account.getCustomer();

        double currencyAsset =currencyService.setCurrencyAsset(request.getDebt(),account);

        if(currencyAsset<request.getDebt()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Insufficient balance");
        }

        entityManager.lock(debitCard.getDepositAccount(),LockModeType.PESSIMISTIC_FORCE_INCREMENT);

        try {

            Thread.sleep(5000);

            creditCard.setCurrentLimit(creditCard.getCurrentLimit()+request.getDebt());
            account.setBalance(account.getBalance()-currencyService.setAccountBalance(request.getDebt(),account));

            customer.setAsset(customer.getAsset()-request.getDebt());
            customerRepository.save(customer);
            creditCardRepository.save(creditCard);


        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Account is busy");
        }

        double accountAmount=currencyService.setAccountBalance(request.getDebt(),account);

        CardTransactionRequest debitCardTransaction = new CardTransactionRequest();
        debitCardTransaction.setCardNumber(request.getCardNumber());
        debitCardTransaction.setAmount(request.getDebt());

        saveTransactionServiceImpl.saveDebitCardTransaction(debitCardTransaction,debitCard.getDepositAccount(),"DebtPayment",accountAmount);


        AccountTransactionRequest accountTransactionRequest = new AccountTransactionRequest();
        accountTransactionRequest.setAccountNumber(debitCard.getDepositAccount().getAccountNumber());
        accountTransactionRequest.setAmount(accountAmount);

        saveTransactionServiceImpl.saveBalanceOnAccount("DebtPaymentFromDebit",account,accountTransactionRequest);

        CardTransactionRequest creditCardTransaction = new CardTransactionRequest();
        creditCardTransaction.setCardNumber(request.getCreditCardNumber());
        creditCardTransaction.setAmount(request.getDebt());
        saveTransactionServiceImpl.saveCreditCardTransaction(creditCardTransaction,customer,"DebtPayment");

        return ResponseEntity.status(HttpStatus.OK).body("Credit card debt amounting to "+request.getDebt()+" TL has been paid");

    }
    @Override
    public <T extends Card> List<CardTransaction> findByDateBetweenAndCardNo(T a, TransactionDate date, String cardNo) throws Exception {

        Card card = null;

        if(a instanceof CreditCard)
            card = creditCardRepository.findByCardNumber(cardNo);

        if(a instanceof DebitCard)
            card =debitCardRepository.findByCardNumber(cardNo);

        if(a==null)
            throw new Exception("Card not found");

        String startYear=date.getStartYear() ;
        String startMonth=date.getStartMonth();
        String startDay=date.getStartDay();

        String endYear=date.getEndYear();
        String endMonth =date.getEndMonth();
        String endDay =date.getEndDay();


        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = dateFormat.parse(startYear+"-"+startMonth+"-"+startDay);
        Date endDate = dateFormat.parse(endYear+"-"+endMonth+"-"+endDay);

        return cardTransactionRepository.findByDateBetweenAndCardNo(startDate,endDate,cardNo);

    }

}

