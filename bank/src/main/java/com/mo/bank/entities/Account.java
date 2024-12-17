package com.mo.bank.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString(exclude = "roles")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "email_password", nullable = false)
    private String emailPassword;

    @Column(name = "credit_card_number", length = 16)
    private String creditCardNumber;

    @Column(name = "card_password")
    private String cardPassword;

    @Column(name = "money")
    private Double money;

    @Column(name = "status", nullable = false)
    private Boolean status;
}
