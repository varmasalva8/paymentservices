package com.cg.payment_wallet;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cg.payment_wallet.controller.WalletController;
import com.cg.payment_wallet.dto.ApiResponse;
import com.cg.payment_wallet.dto.UserRequest;
import com.cg.payment_wallet.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean WalletService walletService;
    @Autowired ObjectMapper objectMapper;

    @Test
    void registerEndpoint_shouldReturnSuccess() throws Exception {
        UserRequest req = new UserRequest("john","password","john@test.com");
        ApiResponse<Void> serviceResp = new ApiResponse<>(true,"User registered successfully", null);
        when(walletService.registerUser(any(UserRequest.class))).thenReturn(serviceResp);

        mockMvc.perform(post("/api/wallets/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }
}
