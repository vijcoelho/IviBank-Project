package com.mo.bank.repositories;

import com.mo.bank.entities.HealthInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HealthInsuranceRepository extends JpaRepository<HealthInsurance, Integer> {

    Optional<HealthInsurance> findHealthInsuranceByAccountId(Integer accountId);
}
