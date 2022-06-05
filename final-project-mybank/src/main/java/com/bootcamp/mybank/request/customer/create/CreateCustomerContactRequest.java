package com.bootcamp.mybank.request.customer.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerContactRequest {

    private String email;
    private String phoneNumber;
}
