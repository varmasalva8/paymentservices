package com.cg.payment_wallet.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.payment_wallet.dto.AddAmountRequest;
import com.cg.payment_wallet.dto.ApiResponse;
import com.cg.payment_wallet.dto.BalanceResponse;
import com.cg.payment_wallet.dto.TransferRequest;
import com.cg.payment_wallet.dto.UserRequest;
import com.cg.payment_wallet.service.WalletService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;
    public WalletController(WalletService walletService) { this.walletService = walletService; }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserRequest req) {
        return ResponseEntity.ok(walletService.registerUser(req));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> addAmount(@Valid @RequestBody AddAmountRequest req) {
        return ResponseEntity.ok(walletService.addAmount(req));
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<BalanceResponse>> getBalance(@RequestParam String email) {
        return ResponseEntity.ok(walletService.checkBalance(email));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<Void>> transfer(@Valid @RequestBody TransferRequest req) {
        return ResponseEntity.ok(walletService.transferFunds(req));
    }
}
