package com.bootcamp.mybank.repository.card;

import com.bootcamp.mybank.entity.card.CreditCard;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends CardRepository {

    CreditCard findByCardNumber(String cardNumber);

    CreditCard findByExpiredDate(String expiredDate);


}
