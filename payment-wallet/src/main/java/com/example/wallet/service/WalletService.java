package com.example.wallet.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.wallet.dto.AddAmountRequest;
import com.example.wallet.dto.BalanceResponse;
import com.example.wallet.dto.MessageResponse;
import com.example.wallet.dto.TransferRequest;
import com.example.wallet.dto.UserRequest;
import com.example.wallet.exception.DuplicateResourceException;
import com.example.wallet.exception.InsufficientFundsException;
import com.example.wallet.exception.ResourceNotFoundException;
import com.example.wallet.model.Currency;
import com.example.wallet.model.Transaction;
import com.example.wallet.model.TransactionType;
import com.example.wallet.model.User;
import com.example.wallet.model.Wallet;
import com.example.wallet.repository.CurrencyRepository;
import com.example.wallet.repository.TransactionRepository;
import com.example.wallet.repository.UserRepository;
import com.example.wallet.repository.WalletRepository;

@Service
public class WalletService {

	private final UserRepository userRepo;
	private final WalletRepository walletRepo;
	private final CurrencyRepository currencyRepo;
	private final TransactionRepository txnRepo;

	public WalletService(UserRepository u, WalletRepository w, CurrencyRepository c, TransactionRepository t) {
		this.userRepo = u;
		this.walletRepo = w;
		this.currencyRepo = c;
		this.txnRepo = t;
	}

	@Transactional
	public MessageResponse createUser(UserRequest req, Link selfLink) {
		userRepo.findByEmail(req.getEmail()).ifPresent(u -> {
			throw new DuplicateResourceException("User with the given email id already exists");
		});

		User user = User.builder().name(req.getName()).password(req.getPassword()).email(req.getEmail()).build();
		user = userRepo.save(user);
		Currency currency = currencyRepo.findByAbbreviation("Rs")
				.orElseGet(() -> currencyRepo.save(Currency.builder().name("Indian Rupee").abbreviation("Rs").build()));
		Wallet wallet = Wallet.builder().user(user).currency(currency).balance(BigDecimal.ZERO).build();
		
		walletRepo.save(wallet);

		return MessageResponse.builder().content("Wallet account created successfully")._links(List.of(selfLink))
				.build();
	}

	@Transactional
	public MessageResponse addFunds(AddAmountRequest req, Link selfLink) {
		User user = userRepo.findByEmail(req.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("There is no user registered with above email id"));

		Wallet wallet = walletRepo.findById(user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

		BigDecimal amountBD = req.getAmount();
		
		wallet.setBalance(wallet.getBalance().add(amountBD));
		walletRepo.save(wallet);

		recordTransaction(wallet, amountBD, "CREDIT");

		return MessageResponse.builder().content("Balance added successfully")._links(List.of(selfLink)).build();
	}

	@Transactional(readOnly = true)
	public BalanceResponse getBalance(Long userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Wallet wallet = walletRepo.findById(user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

		return BalanceResponse.builder().userId(user.getId()).balance(wallet.getBalance())
				.currency(wallet.getCurrency().getAbbreviation()).build();
	}

	@Transactional
	public MessageResponse transfer(TransferRequest req, Link selfLink) {
		Wallet from = walletRepo.findById(req.getFromWalletId())
				.orElseThrow(() -> new ResourceNotFoundException("From wallet not found"));

		Wallet to = walletRepo.findById(req.getToWalletId())
				.orElseThrow(() -> new ResourceNotFoundException("To wallet not found"));

	
		BigDecimal amountBD = req.getAmount();
		// Check balance
		if (from.getBalance().compareTo(amountBD) < 0) {
		    throw new InsufficientFundsException("Insufficient balance. Kindly review your balance.");
		}

		// Update balances
		from.setBalance(from.getBalance().subtract(amountBD));
		to.setBalance(to.getBalance().add(amountBD));

		walletRepo.save(from);
		walletRepo.save(to);
		

		// Record debit & credit transactions
		recordTransaction(from, amountBD, "DEBIT");
		recordTransaction(to, amountBD, "CREDIT");

		return MessageResponse.builder().content("Transfer done successfully.")._links(List.of(selfLink)).build();
	}

	private void recordTransaction(Wallet wallet, BigDecimal amount, String status) {
		txnRepo.save(Transaction.builder().wallet(wallet).status(status).amount(amount).date(LocalDate.now())
				.time(LocalTime.now()).build());
	}
	


	private void recordTransaction(Wallet wallet, BigDecimal amount, TransactionType type) {
		txnRepo.save(Transaction.builder().wallet(wallet).currency(wallet.getCurrency()).amount(amount).type(type)
				.status("SUCCESS").date(LocalDate.now()).time(LocalTime.now()).build());
	}
}
