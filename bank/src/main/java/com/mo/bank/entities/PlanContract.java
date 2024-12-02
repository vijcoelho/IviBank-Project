package com.mo.bank.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plan_contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PlanContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_contract_id")
    private Integer planContractId;

    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "health_plan")
    private String healthPlan;

    @Column(name = "car_insurance")
    private String carInsurance;

    @Column(name = "mobile_plan")
    private String mobilePlan;
}
