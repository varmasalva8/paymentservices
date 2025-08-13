package com.cg.payment_wallet.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.payment_wallet.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
