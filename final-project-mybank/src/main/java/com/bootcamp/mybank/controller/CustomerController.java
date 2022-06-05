package com.bootcamp.mybank.controller;


import com.bootcamp.mybank.service.CustomerService;
import com.bootcamp.mybank.entity.customer.Customer;
import com.bootcamp.mybank.repository.CustomerRepository;
import com.bootcamp.mybank.request.customer.create.CreateCustomerRequest;
import com.bootcamp.mybank.request.customer.update.UpdateAddress;
import com.bootcamp.mybank.request.customer.update.UpdateEmail;
import com.bootcamp.mybank.request.customer.update.UpdatePhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/customer")
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody CreateCustomerRequest request) throws Exception {
        return customerService.create(request);
    }

    @GetMapping("/listAll")
    public List<Customer> listAll(){
        return customerService.findAll();
    }

    @DeleteMapping("/delete/ById/{id}")
    private ResponseEntity<Object> deleteById(@PathVariable long id){
        return customerService.deleteCustomer(id);
    }

    @PutMapping("/update/email/{id}")
    private ResponseEntity<Object> updateEmail(@PathVariable long id,@RequestBody UpdateEmail email){
        return customerService.updateEmail(email,id);
    }

    @PutMapping("/update/phoneNumber/{id}")
    private ResponseEntity<Object> updatePhoneNumber(@PathVariable long id,@RequestBody UpdatePhoneNumber number){
        return customerService.updatePhoneNumber(number,id);
    }
    @PutMapping("/update/address/{id}")
    private ResponseEntity<Object> updateAddress(@RequestBody UpdateAddress address, @PathVariable long id){
        return customerService.updateAddress(address,id);
    }




}
