package com.mo.bank.security;

import com.mo.bank.entities.Account;
import com.mo.bank.repositories.AccountRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class AccountAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (checkIfEndpointIsNotPublic(request)) {
            String token = recoveryToken(request);
            if (token != null && jwtTokenService.validateToken(token)) {
                String subject = jwtTokenService.getEmailFromToken(token);
                if (subject != null) {
                    Optional<Account> accountOpt = accountRepository.findByEmail(subject);
                    if (accountOpt.isPresent()) {
                        Account account = accountOpt.get();
                        AccountDetailsImpl accountDetails = new AccountDetailsImpl(jwtTokenService, accountRepository);
                        accountDetails.loadAccountFromToken(token);
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                accountDetails, null, accountDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        System.out.println("Conta não encontrada para o subject: " + subject);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "").trim();
        }
        return null;
    }

    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return Arrays.stream(SecurityConfiguration.ENDPOINTS_WITH_AUTH_NO_REQUIRED)
                .noneMatch(requestURI::startsWith);
    }
}
