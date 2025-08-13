package com.cg.payment_wallet.dto;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AddAmountRequest {
    @NotBlank
    private String email;

    @NotNull
    @Min(value = 1, message = "Amount must be at least 1")
    private Double amount;
}
