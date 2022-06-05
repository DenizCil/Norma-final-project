package com.bootcamp.mybank.request.customer.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerAddressRequest {

    private String street;
    private String city;
    private String zipcode;
}
