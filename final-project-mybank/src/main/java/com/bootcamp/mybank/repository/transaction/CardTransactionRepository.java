package com.bootcamp.mybank.repository.transaction;

import com.bootcamp.mybank.entity.transaction.CardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CardTransactionRepository extends JpaRepository<CardTransaction,Long> {


    List<CardTransaction> findByCardNo(String cardNo);

    List<CardTransaction> findByDateBetweenAndCardNo(Date start, Date end,String cardNo);



}