package com.bootcamp.mybank.entity.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public abstract class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long card_id;

    private String cardNumber;
    private String cardType;
    private boolean isUsable;
    private String securityNumber;

    private String expiredDate;

}