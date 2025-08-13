package com.cg.payment_wallet;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cg.payment_wallet.dto.UserRequest;
import com.cg.payment_wallet.exception.DuplicateResourceException;
import com.cg.payment_wallet.model.User;
import com.cg.payment_wallet.model.Wallet;
import com.cg.payment_wallet.repository.UserRepository;
import com.cg.payment_wallet.repository.WalletRepository;
import com.cg.payment_wallet.service.WalletService;

class WalletServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private WalletRepository walletRepository;
    @InjectMocks private WalletService walletService;

    @BeforeEach
    void init() { MockitoAnnotations.openMocks(this); }

    @Test
    void registerUser_success() {
        UserRequest req = new UserRequest("john","password","john@test.com");
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1);
            return u;
        });

        var resp = walletService.registerUser(req);
        assertTrue(resp.isSuccess());
        assertEquals("User registered successfully", resp.getMessage());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void registerUser_duplicate() {
        UserRequest req = new UserRequest("john","password","john@test.com");
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(new User()));
        assertThrows(DuplicateResourceException.class, () -> walletService.registerUser(req));
    }
}
