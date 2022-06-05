package com.bootcamp.mybank.entity.operation;


import lombok.Getter;

@Getter
public enum OperationType {


    CREATE_CUSTOMER,DELETE_CUSTOMER,

    CREATE_DEPOSIT_ACCOUNT,DELETE_DEPOSIT_ACCOUNT,
    CREATE_SAVING_ACCOUNT,DELETE_SAVING_ACCOUNT,

    CREATE_DEBIT_CARD,DELETE_DEBIT_CARD,
    CREATE_CREDIT_CARD,DELETE_CREDIT_CARD,

    UPDATE_MAIL,UPDATE_PHONE,UPDATE_ADDRESS
}