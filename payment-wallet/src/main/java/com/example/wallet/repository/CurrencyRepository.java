package com.example.wallet.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wallet.model.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
	
		 Optional<Currency> findByAbbreviation(String abbreviation);
}

