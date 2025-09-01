package com.example.wallet.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wallet.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

}

