package com.mo.bank.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_account")
    private Integer idAccount;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "email_password", nullable = false)
    private String emailPassword;

    @Column(name = "card", length = 16)
    private String card;

    @Column(name = "password_card")
    private String passwordCard;
}
