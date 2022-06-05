package com.bootcamp.mybank.generator;

import java.util.Random;

public class AccountNumberGenerator {

    Random random = new Random();

    public String generateAccountNumber(){

        String accountNumber="";

        for(int i=0;i<16;i++){

            int number = random.nextInt(10);
            accountNumber+=Integer.toString(number);
        }

        return accountNumber;
    }
}