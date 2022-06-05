package com.bootcamp.mybank.repository.card;

import com.bootcamp.mybank.entity.card.Card;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface CardRepository extends JpaRepository<Card,Long> {
}
