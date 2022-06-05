package com.bootcamp.mybank.repository.transaction;

import com.bootcamp.mybank.entity.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface TransactionRepository extends JpaRepository<Transaction,Long> {


}
