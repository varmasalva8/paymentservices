package com.example.wallet.controller;


import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wallet.dto.AddAmountRequest;
import com.example.wallet.dto.BalanceResponse;
import com.example.wallet.dto.MessageResponse;
import com.example.wallet.dto.TransferRequest;
import com.example.wallet.dto.UserRequest;
import com.example.wallet.service.WalletService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class WalletController {

 private final WalletService walletService;

 // POST /v1/users  -> 201 + HATEOAS self link
 @PostMapping("/users")
 public ResponseEntity<MessageResponse> createUser(@Valid @RequestBody UserRequest req) {
     Link self = Link.of("http://localhost:8080/users/{id}").withSelfRel(); // illustrative
     var resp = walletService.createUser(req, self);
     return ResponseEntity.status(HttpStatus.CREATED).body(resp);
 }

 // PATCH /v1/users/wallet -> 200 + message
 @PatchMapping("/users/wallet")
 public ResponseEntity<MessageResponse> addFunds(@Valid @RequestBody AddAmountRequest req) {
     Link self = Link.of("http://localhost:8080/users/wallet").withSelfRel();
     return ResponseEntity.ok(walletService.addFunds(req, self));
 }

 // GET /v1/users/{userId} -> 200 (or 400 when userId not numeric handled by Spring automatically if path variable typed Integer)
 @GetMapping("/users/{userId}")
 public ResponseEntity<BalanceResponse> getBalance(@PathVariable Long userId) {
     return ResponseEntity.ok(walletService.getBalance(userId));
 }

 // POST /v1/users/wallet/transfer -> 200 + message
 @PostMapping("/users/wallet/transfer")
 public ResponseEntity<MessageResponse> transfer(@Valid @RequestBody TransferRequest req) {
     Link self = Link.of("http://localhost:8080/users/{id}/wallet").withSelfRel();
     return ResponseEntity.ok(walletService.transfer(req, self));
 }
}