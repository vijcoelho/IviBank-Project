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

    private Random random;

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

    public String generateCardNumber() {
        random = new Random();
        long card = random.nextLong(9999999999999999L);
        return String.valueOf(card);
    }
}
