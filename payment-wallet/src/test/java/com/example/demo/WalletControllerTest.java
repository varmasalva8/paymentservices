//package com.example.demo;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.example.wallet.config.AddFundsRequest;
//import com.example.wallet.config.CreateUserRequest;
//import com.example.wallet.dto.TransferRequest;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import lombok.var;
//@SpringBootTest
//@AutoConfigureMockMvc
//public class WalletControllerTest {
// @Autowired MockMvc mvc;
// @Autowired ObjectMapper om;
//
// @Test
// void createUser_thenAddFunds_thenGetBalance() throws Exception {
//     var create = CreateUserRequest.builder().username("John").password("password123").email("john@example.com").build();
//     mvc.perform(post("/v1/users").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(create)))
//             .andExpect(status().isCreated());
//
//     var add = AddFundsRequest.builder().email("john@example.com").amount(1000.0).build();
//     mvc.perform(patch("/v1/users/wallet").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(add)))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.content").value("Balance added successfully"));
//
//     mvc.perform(get("/v1/users/{id}", 1))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.userId").value(1))
//             .andExpect(jsonPath("$.balance").value(1000.0));
// }
//
// @Test
// void transfer_insufficientBalance() throws Exception {
//     var createA = CreateUserRequest.builder().username("A").password("secret123").email("a@x.com").build();
//     var createB = CreateUserRequest.builder().username("B").password("secret123").email("b@x.com").build();
//     mvc.perform(post("/v1/users").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(createA))).andExpect(status().isCreated());
//     mvc.perform(post("/v1/users").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(createB))).andExpect(status().isCreated());
//
//     var req = TransferRequest.builder().walletId(1).toWalletId(2).balance(10000.0).build();
//     mvc.perform(post("/v1/users/wallet/transfer").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(req)))
//             .andExpect(status().isNotFound())
//             .andExpect(jsonPath("$.content").value("Insufficient balance. Kindly review your balance."));
// }
//}
