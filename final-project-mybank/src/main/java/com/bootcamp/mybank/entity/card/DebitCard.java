package com.bootcamp.mybank.entity.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.bootcamp.mybank.entity.account.DepositAccount;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebitCard extends Card{


    @ManyToOne
    private DepositAccount depositAccount;



}
