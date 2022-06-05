package com.bootcamp.mybank.request.customer.update;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAddress {

    private String street;
    private String city;
    private String zipcode;
}