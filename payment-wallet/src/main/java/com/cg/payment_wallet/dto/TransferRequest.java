package com.cg.payment_wallet.dto;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransferRequest {
    @NotNull
    private Integer fromWalletId;

    @NotNull
    private Integer toWalletId;

    @NotNull
    @Min(value = 1, message = "Amount must be positive")
    private Double amount;
}
