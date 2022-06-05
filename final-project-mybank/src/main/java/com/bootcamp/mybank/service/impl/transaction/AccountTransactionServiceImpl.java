package com.bootcamp.mybank.service.impl.transaction;
import java.text.DateFormat;
import java.util.*;

import com.bootcamp.mybank.request.transaction.AccountTransactionRequest;
import com.bootcamp.mybank.request.transaction.CardTransactionRequest;
import com.bootcamp.mybank.request.transaction.DebtOnAccountRequest;
import com.bootcamp.mybank.request.transaction.TransactionDate;
import com.bootcamp.mybank.checker.FormatChecker;
import com.bootcamp.mybank.entity.customer.Customer;
import com.bootcamp.mybank.entity.account.Account;
import com.bootcamp.mybank.entity.card.CreditCard;
import com.bootcamp.mybank.entity.transaction.AccountTransaction;
import com.bootcamp.mybank.repository.CustomerRepository;
import com.bootcamp.mybank.repository.account.AccountRepository;
import com.bootcamp.mybank.repository.card.CreditCardRepository;
import com.bootcamp.mybank.repository.transaction.AccountTransactionRepository;
import com.bootcamp.mybank.service.currency.CurrencyService;
import com.bootcamp.mybank.service.impl.transaction.helper.AccountTypeHelper;
import com.bootcamp.mybank.service.transaction.AccountTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Service
public class AccountTransactionServiceImpl implements AccountTransactionService {


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;

    @Autowired
    private SaveTransactionServiceImpl saveTransactionServiceImpl;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private AccountTypeHelper accountTypeHelper;


    @PersistenceContext
    private EntityManager entityManager;

    
    private final FormatChecker formatChecker = new FormatChecker();

    @Transactional
    @Override
    public <T extends Account> ResponseEntity<Object> balanceOnAccount(T a, String transactionType, AccountTransactionRequest request) throws IOException {

        boolean accountNumberCheck=formatChecker.numberFormatChecker(request.getAccountNumber());

        if(!accountNumberCheck)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid account type");

        Account account=accountTypeHelper.setAccountType(a,request.getAccountNumber());
        AccountRepository repository=accountTypeHelper.setAccountRepo(a);


        Customer customer =customerRepository.findById(account.getCustomer().getId());

        if(transactionType.equals("AddBalance")){


            entityManager.lock(account,LockModeType.PESSIMISTIC_FORCE_INCREMENT);
            try {

                Thread.sleep(5000);
                account.setBalance(account.getBalance()+request.getAmount());
                currencyService.setCustomerAsset(request.getAmount(),account,"AddBalance");
                repository.save(account);


            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Account is busy");
            }

            saveTransactionServiceImpl.saveBalanceOnAccount("AddBalance",account,request);
            return ResponseEntity.status(HttpStatus.OK).body("Balance is added");

        }

        if(transactionType.equals("WithdrawBalance")){

            if(account.getBalance()>=request.getAmount()){


                entityManager.lock(account,LockModeType.PESSIMISTIC_FORCE_INCREMENT);
                try {

                    Thread.sleep(5000);
                    account.setBalance(account.getBalance()-request.getAmount());
                    currencyService.setCustomerAsset(request.getAmount(),account,"WithdrawBalance");

                    repository.save(account);


                }catch (Exception e){
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Account is busy");
                }

                saveTransactionServiceImpl.saveBalanceOnAccount("WithdrawBalance",account,request);

                return ResponseEntity.status(HttpStatus.OK).body("Balance is withdraw");
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("You dont have enough money for this transaction");

    }

    @Transactional
    @Override
    public <T extends Account> ResponseEntity<Object> debtOnAccount(T a, DebtOnAccountRequest request) throws IOException {


        boolean accountNumberCheck=formatChecker.numberFormatChecker(request.getAccountNumber());
        boolean cardNumberCheck=formatChecker.numberFormatChecker(request.getCardNumber());

        Account account = accountTypeHelper.setAccountType(a,request.getAccountNumber());
        AccountRepository accountRepository =accountTypeHelper.setAccountRepo(a);
        CreditCard creditCard = creditCardRepository.findByCardNumber(request.getCardNumber());


        if(!accountNumberCheck)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid account number type");

        if(!cardNumberCheck)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid card number type");

        if(creditCard==null){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credit card not found");
        }

        if(account==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");

        if(account.getCustomer()!=creditCard.getCustomer()){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer can pay only own credit card debt");
        }

        double currencyAsset =currencyService.setCurrencyAsset(request.getDebt(),account);

        if(currencyAsset<request.getDebt()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Insufficient balance");
        }


        Customer customer = account.getCustomer();

        entityManager.lock(creditCard,LockModeType.PESSIMISTIC_FORCE_INCREMENT);
        entityManager.lock(account,LockModeType.PESSIMISTIC_FORCE_INCREMENT);
        try {

            Thread.sleep(5000);

            creditCard.setCurrentLimit(creditCard.getCurrentLimit()+request.getDebt());
            account.setBalance(account.getBalance()- currencyService.setAccountBalance(request.getDebt(),account));
            customer.setAsset(customer.getAsset()-request.getDebt());

            accountRepository.save(account);
            creditCardRepository.save(creditCard);
            customerRepository.save(customer);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Account is busy");
        }

        CardTransactionRequest cardTransactionRequest = new CardTransactionRequest();

        cardTransactionRequest.setCardNumber(creditCard.getCardNumber());
        cardTransactionRequest.setAmount(request.getDebt());

        saveTransactionServiceImpl.saveCreditCardTransaction(cardTransactionRequest,customer,"DebtPayment");
        saveTransactionServiceImpl.saveDebtOnAccount(account,request,currencyService.setAccountBalance(request.getDebt(),account));

        return ResponseEntity.status(HttpStatus.OK).body("Credit card debt amounting to "+request.getDebt()+" TL has been paid");
    }


    @Override
    public <T extends Account> List<AccountTransaction> findAccountTransactionByDateAndAccountNumber(T a, TransactionDate date, String accountNumber) throws Exception {

        Account account = accountTypeHelper.setAccountType(a,accountNumber);

        if(account==null){
            throw new Exception("Account not found");
        }
        String startYear=date.getStartYear() ;
        String startMonth=date.getStartMonth();
        String startDay=date.getStartDay();

        String endYear=date.getEndYear();
        String endMonth =date.getEndMonth();
        String endDay =date.getEndDay();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = dateFormat.parse(startYear+"-"+startMonth+"-"+startDay);
        Date endDate = dateFormat.parse(endYear+"-"+endMonth+"-"+endDay);

        return accountTransactionRepository.findByDateBetweenAndAccountNumber(startDate,endDate,accountNumber);

    }

    @Transactional
    @Override
    public <T extends Account> ResponseEntity<Object> withDrawAllMoney(T a,String accountNumber) throws IOException {

        Account account = accountTypeHelper.setAccountType(a,accountNumber);
        AccountRepository repository = accountTypeHelper.setAccountRepo(a);

        if(account==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");

        if(account.getBalance()==0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have not balance");


        double currentMoney=account.getBalance();

        entityManager.lock(account,LockModeType.PESSIMISTIC_FORCE_INCREMENT);
        try {

            Thread.sleep(2000);

            account.setBalance(0);
            currencyService.setCustomerAsset(currentMoney,account,"WithdrawBalance");
            repository.save(account);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Account is busy");
        }


        AccountTransactionRequest request = new AccountTransactionRequest();
        request.setAmount(currentMoney);
        request.setAccountNumber(accountNumber);

        saveTransactionServiceImpl.saveBalanceOnAccount("Withdraw",account,request);

        return ResponseEntity.status(HttpStatus.OK).body(currentMoney+" "+account.getCurrencyType().toString()+" is withdrawed");


    }

}