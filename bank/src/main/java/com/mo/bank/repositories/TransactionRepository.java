package com.mo.bank.repositories;

import com.mo.bank.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT * FROM transaction WHERE sender_account_id = :id OR receiver_account_id = :id", nativeQuery = true)
    List<Transaction> findByAccountId(@Param("id") Integer id);
}
