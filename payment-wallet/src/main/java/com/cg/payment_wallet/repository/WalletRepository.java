package com.cg.payment_wallet.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.payment_wallet.model.User;
import com.cg.payment_wallet.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Optional<Wallet> findByUser(User user);
}

