package com.cg.payment_wallet.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.payment_wallet.model.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
}

