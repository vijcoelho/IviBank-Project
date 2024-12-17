package com.mo.bank.security;

import com.mo.bank.entities.Account;
import com.mo.bank.repositories.AccountRepository;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Getter
@Component
public class AccountDetailsImpl implements UserDetails {

    private final JwtTokenService jwtTokenService;
    private final AccountRepository accountRepository;
    private Optional<Account> account;

    public AccountDetailsImpl(JwtTokenService jwtTokenService, AccountRepository accountRepository) {
        this.jwtTokenService = jwtTokenService;
        this.accountRepository = accountRepository;
        this.account = Optional.empty();
    }


    public boolean loadAccountFromToken(String token) {
        String email = jwtTokenService.getEmailFromToken(token);
        if (email != null) {
            this.account = accountRepository.findByEmail(email);
            return account.isPresent();
        }
        return false;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return account.map(Account::getEmailPassword).orElse(null);
    }

    @Override
    public String getUsername() {
        return account.map(Account::getEmail).orElse(null);
    }

    @Override
    public boolean isAccountNonExpired() {
        return account.isPresent() && account.get().getStatus();
    }

    @Override
    public boolean isAccountNonLocked() {
        return account.isPresent() && account.get().getStatus();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return account.isPresent() && account.get().getStatus();
    }

    @Override
    public boolean isEnabled() {
        return account.isPresent() && account.get().getStatus();
    }
}
