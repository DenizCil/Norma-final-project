package com.bootcamp.mybank.service;

import com.bootcamp.mybank.entity.customer.Customer;
import com.bootcamp.mybank.request.customer.create.CreateCustomerRequest;
import com.bootcamp.mybank.request.customer.update.UpdateAddress;
import com.bootcamp.mybank.request.customer.update.UpdateEmail;
import com.bootcamp.mybank.request.customer.update.UpdatePhoneNumber;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {

    ResponseEntity<Object> create(CreateCustomerRequest request) throws Exception;
    List<Customer> findAll();
    ResponseEntity<Object> deleteCustomer(long id);
    Customer findByIdentificationNumber(String idNumber);
    ResponseEntity<Object> updateEmail(UpdateEmail email, long id);
    ResponseEntity<Object> updatePhoneNumber(UpdatePhoneNumber number, long id);
    ResponseEntity<Object> updateAddress(UpdateAddress address, long id);


}