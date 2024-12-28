package com.mo.bank.repositories;

import com.mo.bank.entities.CarInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarInsuranceRepository extends JpaRepository<CarInsurance, Integer> {

    Optional<CarInsurance> findCarInsuranceByAccountId(Integer accountId);
}
