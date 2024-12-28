package com.mo.bank.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "health_insurance")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class HealthInsurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer healthInsuranceId;

    @Column(name = "health_contract_active", nullable = false)
    private Boolean isActive;

    @Column(name = "health_contract_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Column(name = "health_contract_price", nullable = false)
    private Double price;

    @Column(name = "health_contract_paid", nullable = false)
    private Boolean isPaid;

    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    @Column(name = "account_id", nullable = false)
    private Integer accountId;
}
