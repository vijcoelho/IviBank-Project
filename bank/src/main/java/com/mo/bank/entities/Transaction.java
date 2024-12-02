package com.mo.bank.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    @Column(name = "sender_account_id", nullable = false)
    private Integer senderAccountId;

    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    @Column(name = "receiver_account_id", nullable = false)
    private Integer receiverAccountId;

    @Column(name = "amount", nullable = false)
    private Double amount;
}
