package com.example.wallet.dto;


import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BalanceResponse {
    private Long userId;
    private String currency;

    private BigDecimal balance;
    
    


}
