package com.mo.bank.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Random;

@Entity
@Table(name = "card")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Integer cardId;

    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;

    @Column(name = "cardholder_name", nullable = false)
    private String cardholderName;

    @Column(name = "card_password")
    private String cardPassword;

    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    @Column(name = "account_id")
    private Integer accountId;

    public Card(String cardNumber, String cardholderName, String cardPassword, Integer accountId) {
        this.cardNumber = cardNumber;
        this.cardholderName = cardholderName;
        this.cardPassword = cardPassword;
        this.accountId = accountId;
    }
}
