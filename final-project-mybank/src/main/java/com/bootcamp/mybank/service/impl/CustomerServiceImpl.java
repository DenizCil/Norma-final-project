package com.bootcamp.mybank.service.impl;

import com.bootcamp.mybank.request.customer.create.CreateCustomerRequest;
import com.bootcamp.mybank.request.customer.update.UpdateAddress;
import com.bootcamp.mybank.request.customer.update.UpdateEmail;
import com.bootcamp.mybank.request.customer.update.UpdatePhoneNumber;
import com.bootcamp.mybank.service.CustomerService;
import com.bootcamp.mybank.entity.customer.Customer;
import com.bootcamp.mybank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

    }

    public CustomerServiceImpl() {

    }

    @Override
    public ResponseEntity<Object> create(CreateCustomerRequest request) throws Exception {
        return null;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public ResponseEntity<Object> deleteCustomer(long id) {

        Customer customer = customerRepository.findById(id);

        if(customer==null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Customer not found");
        }
        return isDeletable(customer);
    }

    @Override
    public Customer findByIdentificationNumber(String idNumber) {
        return customerRepository.findByIdentificationNumber(idNumber);
    }

    @Override
    public ResponseEntity<Object> updateEmail(UpdateEmail email, long id) {
        return null;
    }

    @Override
    public ResponseEntity<Object> updatePhoneNumber(UpdatePhoneNumber number, long id) {
        return null;
    }

    @Override
    public ResponseEntity<Object> updateAddress(UpdateAddress address, long id) {
        return null;
    }


    ResponseEntity<Object> isDeletable(Customer customer) {

        if (customer.getAsset() != 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Customer have a asset.Deletion not allowed");
        } //Bankada parası var silinemez.

        for (int i = 0; i < customer.getCreditCards().size(); i++) {

            double debt = customer.getCreditCards().get(i).getCardLimit() - customer.getCreditCards().get(i).getCurrentLimit();

            if (debt != 0) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Customer have a credit debt.Deletion not allowed");
            } //Kredi işleminden dolayı silinemez.


        }


        return null;
    }}