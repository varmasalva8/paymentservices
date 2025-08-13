package com.cg.payment_wallet.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Double balance = 0.0;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
}
