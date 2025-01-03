package com.mo.bank.repositories;

import com.mo.bank.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findTokenByAccountId (Integer id);
}
