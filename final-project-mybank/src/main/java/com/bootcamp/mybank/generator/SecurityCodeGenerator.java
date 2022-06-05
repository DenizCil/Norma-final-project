package com.bootcamp.mybank.generator;

import lombok.Getter;
import lombok.Setter;
import java.util.Random;

@Getter
@Setter
public class SecurityCodeGenerator {



    public String generateSecurityCode(){

        Random random = new Random();
        String securityCode="";



        for(int i=0;i<3;i++){

            securityCode+=String.valueOf(random.nextInt(9));
        }

        return securityCode;
    }

}