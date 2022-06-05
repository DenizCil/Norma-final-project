package com.bootcamp.mybank.entity.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.bootcamp.mybank.entity.customer.Customer;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard extends Card{


    private double cardLimit;
    private double currentLimit;

    @ManyToOne
    private Customer customer;


}