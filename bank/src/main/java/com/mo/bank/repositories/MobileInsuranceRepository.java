package com.mo.bank.repositories;

import com.mo.bank.entities.MobileInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MobileInsuranceRepository extends JpaRepository<MobileInsurance, Integer> {

    Optional<MobileInsurance> findMobileInsuranceByAccountId(Integer accountId);
}
