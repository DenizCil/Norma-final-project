package com.bootcamp.mybank.request.customer.create;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder(value = {"name","surname","gender","address","contact"})
public class CreateCustomerRequest {

    private String name;
    private String surname;
    private String gender;
    private String identificationNumber;

    private CreateCustomerAddressRequest address;
    private CreateCustomerContactRequest contact;


}