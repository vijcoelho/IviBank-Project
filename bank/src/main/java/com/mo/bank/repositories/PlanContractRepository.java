package com.mo.bank.repositories;

import com.mo.bank.entities.PlanContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanContractRepository extends JpaRepository<PlanContract, Integer> {
}
