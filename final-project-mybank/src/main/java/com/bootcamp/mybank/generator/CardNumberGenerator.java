package com.bootcamp.mybank.generator;

import java.util.Random;

public class CardNumberGenerator {

    Random random = new Random();

    public String generateCardNumber(){

        String cardNumber="";

        for(int i=0;i<16;i++){

            int number = random.nextInt(10);
            cardNumber+=Integer.toString(number);
        }

        return cardNumber;
    }
}