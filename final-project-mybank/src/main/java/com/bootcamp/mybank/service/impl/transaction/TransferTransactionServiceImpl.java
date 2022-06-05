package com.bootcamp.mybank.service.impl.transaction;

import com.bootcamp.mybank.request.transaction.TransferTransactionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bootcamp.mybank.checker.FormatChecker;
import com.bootcamp.mybank.domain.CurrencyOperation;
import com.bootcamp.mybank.entity.customer.Customer;
import com.bootcamp.mybank.entity.account.Account;
import com.bootcamp.mybank.repository.CustomerRepository;
import com.bootcamp.mybank.repository.account.AccountRepository;
import com.bootcamp.mybank.service.currency.CurrencyService;
import com.bootcamp.mybank.service.impl.transaction.helper.AccountTypeHelper;
import com.bootcamp.mybank.service.transaction.TransferTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URL;

@Service
public class TransferTransactionServiceImpl implements TransferTransactionService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private AccountTypeHelper accountTypeHelper;

    @Autowired
    private SaveTransactionServiceImpl saveTransactionServiceImpl;

    @PersistenceContext
    private EntityManager entityManager;

    private final FormatChecker formatChecker = new FormatChecker();


    public TransferTransactionServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

    }

    public TransferTransactionServiceImpl() {

    }

    @Transactional
    @Override
    public <T extends Account> ResponseEntity<Object> sendMoney(T a, T b, TransferTransactionRequest request) throws IOException {


        Account senderAccount=accountTypeHelper.setAccountTypeByIban(a,request.getFromIbanNo());
        Account receiverAccount=accountTypeHelper.setAccountTypeByIban(b,request.getToIbanNo());

        AccountRepository senderRepo = accountTypeHelper.setAccountRepo(a);
        AccountRepository receiverRepo =accountTypeHelper.setAccountRepo(b);

        if(senderAccount==null || receiverAccount==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account not found");
        }


        if(senderAccount.getAccountType().equals("SavingAccount")){

            Customer senderCustomer=senderAccount.getCustomer();
            Customer receiverCustomer=receiverAccount.getCustomer();

            if(senderCustomer != receiverCustomer)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sender account and saving account must be have same customer");
        }

        if(senderAccount.getBalance()>=request.getAmount()){

            Customer senderCustomer=senderAccount.getCustomer();
            Customer receiverCustomer=receiverAccount.getCustomer();


            String senderType =senderAccount.getCurrencyType().toString();
            String receiverType =receiverAccount.getCurrencyType().toString();

            ObjectMapper objectMapper =new ObjectMapper();
            URL url = new URL("https://api.exchangeratesapi.io/latest?base=TRY");
            CurrencyOperation currencyClass =objectMapper.readValue(url,CurrencyOperation.class);

            double transactionRate = currencyClass.getRates().get(receiverType) /currencyClass.getRates().get(senderType) ;

            double newSenderBalance = senderAccount.getBalance()-request.getAmount();
            double newReceiverBalance =receiverAccount.getBalance()+ (request.getAmount()*transactionRate);

            double newSenderCustomerAsset = request.getAmount()/currencyClass.getRates().get(senderType);
            double newReceiverCustomerAsset = request.getAmount()/currencyClass.getRates().get(senderType);


            entityManager.lock(senderAccount, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
            entityManager.lock(receiverAccount,LockModeType.PESSIMISTIC_FORCE_INCREMENT);

            try {

                Thread.sleep(5000);

                senderAccount.setBalance(newSenderBalance);
                receiverAccount.setBalance(newReceiverBalance);

                senderCustomer.setAsset(senderCustomer.getAsset()-newSenderCustomerAsset);
                receiverCustomer.setAsset(receiverCustomer.getAsset()+newReceiverCustomerAsset);

                senderRepo.save(senderAccount);
                receiverRepo.save(receiverAccount);

                customerRepository.save(senderCustomer);
                customerRepository.save(receiverCustomer);


            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Account is busy");
            }


            saveTransactionServiceImpl.saveTransferTransaction(request,senderAccount,receiverAccount,"SendMoney",currencyClass);
            saveTransactionServiceImpl.saveTransferTransaction(request,senderAccount,receiverAccount,"ReceiveMoney",currencyClass);

            saveTransactionServiceImpl.saveTransferTransactionBetweenAccounts(senderAccount,request.getAmount(),"SendMoney");
            saveTransactionServiceImpl.saveTransferTransactionBetweenAccounts(receiverAccount,transactionRate*request.getAmount(),"ReceiveMoney");

            return ResponseEntity.status(HttpStatus.OK).body("Transaction completed");
        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Sender dont have a money for transaction");
    }
}
