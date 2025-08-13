package com.cg.payment_wallet.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.payment_wallet.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
