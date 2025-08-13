package com.cg.payment_wallet.service;

import org.springframework.stereotype.Service;

import com.cg.payment_wallet.dto.AddAmountRequest;
import com.cg.payment_wallet.dto.ApiResponse;
import com.cg.payment_wallet.dto.BalanceResponse;
import com.cg.payment_wallet.dto.TransferRequest;
import com.cg.payment_wallet.dto.UserRequest;
import com.cg.payment_wallet.exception.BadRequestException;
import com.cg.payment_wallet.exception.DuplicateResourceException;
import com.cg.payment_wallet.exception.InsufficientFundsException;
import com.cg.payment_wallet.exception.ResourceNotFoundException;
import com.cg.payment_wallet.model.User;
import com.cg.payment_wallet.model.Wallet;
import com.cg.payment_wallet.repository.UserRepository;
import com.cg.payment_wallet.repository.WalletRepository;

import jakarta.transaction.Transactional;

@Service
public class WalletService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    public WalletService(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public ApiResponse<Void> registerUser(UserRequest req) {
        userRepository.findByEmail(req.getEmail()).ifPresent(u -> {
            throw new DuplicateResourceException("User with the given email id already exists");
        });

        User user = User.builder()
                .username(req.getUsername())
                .password(req.getPassword()) // in real app hash password
                .email(req.getEmail())
                .build();

        User saved = userRepository.save(user);
        Wallet wallet = Wallet.builder().user(saved).balance(0.0).build();
        walletRepository.save(wallet);

        return new ApiResponse<>(true, "User registered successfully", null);
    }

    @Transactional
    public ApiResponse<Void> addAmount(AddAmountRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("There is no user registered with above email id"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user"));

        if (req.getAmount() <= 0) throw new BadRequestException("Amount must be positive");
        wallet.setBalance(wallet.getBalance() + req.getAmount());
        walletRepository.save(wallet);

        return new ApiResponse<>(true, "Amount added successfully", null);
    }

    public ApiResponse<BalanceResponse> checkBalance(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        BalanceResponse resp = BalanceResponse.builder()
                .walletId(wallet.getId()).balance(wallet.getBalance()).build();

        return new ApiResponse<>(true, "Available balance", resp);
    }

    @Transactional
    public ApiResponse<Void> transferFunds(TransferRequest req) {
        if (req.getFromWalletId().equals(req.getToWalletId()))
            throw new BadRequestException("From and To wallet must be different");

        Wallet fromWallet = walletRepository.findById(req.getFromWalletId())
                .orElseThrow(() -> new ResourceNotFoundException("From wallet not found"));
        Wallet toWallet = walletRepository.findById(req.getToWalletId())
                .orElseThrow(() -> new ResourceNotFoundException("To wallet not found"));

        if (fromWallet.getBalance() < req.getAmount()) {
            throw new InsufficientFundsException("Insufficient balance. You have Rs " + fromWallet.getBalance() + " balance in your wallet");
        }

        fromWallet.setBalance(fromWallet.getBalance() - req.getAmount());
        toWallet.setBalance(toWallet.getBalance() + req.getAmount());
        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        return new ApiResponse<>(true, "Transfer successful", null);
    }
}
