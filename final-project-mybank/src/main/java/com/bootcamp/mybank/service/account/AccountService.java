package com.bootcamp.mybank.service.account;

import com.bootcamp.mybank.entity.account.Account;
import com.bootcamp.mybank.request.account.CreateAccountRequest;
import com.bootcamp.mybank.request.account.CreateSavingAccountRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface AccountService {

    public  <T extends Account> ResponseEntity<Object> createAccount(T a, CreateAccountRequest request) throws IOException;
    public <T extends Account> ResponseEntity<Object> deleteAccount(T a, String accountNumber);
    public <T extends Account> ResponseEntity<Object> createSavingAccount(T a, CreateSavingAccountRequest request) throws IOException;

}
