package com.mo.bank.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "car_insurance")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CarInsurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer carInsuranceId;

    @Column(name = "car_contract_active", nullable = false)
    private Boolean isActive = false;

    @Column(name = "car_contract_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Column(name = "car_contract_price", nullable = false)
    private Double price = 50.00;

    @Column(name = "car_contract_paid", nullable = false)
    private Boolean isPaid = false;

    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    @Column(name = "account_id", nullable = false)
    private Integer accountId;
}
