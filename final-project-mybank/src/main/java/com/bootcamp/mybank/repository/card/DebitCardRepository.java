package com.bootcamp.mybank.repository.card;

import com.bootcamp.mybank.entity.card.DebitCard;
import org.springframework.stereotype.Repository;

@Repository
public interface DebitCardRepository extends CardRepository {

    DebitCard findByCardNumber(String cardNumber);
}
