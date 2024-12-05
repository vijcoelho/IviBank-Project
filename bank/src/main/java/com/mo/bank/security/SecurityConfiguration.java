package com.mo.bank.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    public static final String[] ENDPOINTS_WITH_AUTH_NO_REQUIRED = {
            "/account",
            "/account/login",
    };

    public static final String[] ENDPOINTS_WITH_AUTH_REQUIRED = {
            "/account/home",
            "/account/generate-card",
            "/account/generate-card-password",
            "/account/update-status",
            "/account/deposit",
            "/account/withdraw"
    };

    public static final String[] ENDPOINTS_WITH_ADMIN = {
            "/account/admin"
    };

    private final AccountAuthenticationFilter accountAuthenticationFilter;

    public SecurityConfiguration(AccountAuthenticationFilter accountAuthenticationFilter) {
        this.accountAuthenticationFilter = accountAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests()
                .requestMatchers(ENDPOINTS_WITH_AUTH_NO_REQUIRED).permitAll()
                .requestMatchers(ENDPOINTS_WITH_AUTH_REQUIRED).hasRole("CUSTOMER")
                .requestMatchers(ENDPOINTS_WITH_ADMIN).hasRole("ADMINISTRATOR")
                .anyRequest().denyAll()
                .and()
                .addFilterBefore(accountAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
