package com.mo.bank.services;

import com.mo.bank.entities.Account;
import com.mo.bank.entities.Transaction;
import com.mo.bank.repositories.AccountRepository;
import com.mo.bank.repositories.TransactionRepository;
import com.mo.bank.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final JwtService jwtService;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            JwtService jwtService,
            AccountService accountService,
            AccountRepository accountRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.jwtService = jwtService;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    public ResponseEntity<?> makeTransaction(Map<String, String> input, String token) {
        String senderAccount = input.get("sender_account");
        String receiverAccount = input.get("receiver_account");
        String money = input.get("money");

        Optional<Account> sender = accountRepository.findById(Integer.valueOf(senderAccount));
        Optional<Account> receiver = accountRepository.findById(Integer.valueOf(receiverAccount));
        String emailFromToken = jwtService.extractEmail(token);

        if (sender.isEmpty()) {
            return ResponseEntity.badRequest().body("Sender account is null");
        }
        if (receiver.isEmpty()) {
            return ResponseEntity.badRequest().body("Receiver account is null");
        }
        if (!accountService.isEmailValid(sender.get(), emailFromToken)) {
            return ResponseEntity.badRequest().body("Account not found");
        }
        if (!accountService.isAccountActive(sender.get()) || !accountService.isAccountActive(receiver.get())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    "Your account is invalid (status = false), " +
                    "or receiver account is invalid (status = false)"
            );
        }

        if (Double.parseDouble(money) > sender.get().getMoney()) {
            return ResponseEntity.badRequest().body("You can´t send more money than you have");
        }
        if (Double.parseDouble(money) == 0) {
            return ResponseEntity.badRequest().body("You can´t send nothing");
        }

        Double newAmount = Double.parseDouble(money);
        Double senderMoney = sender.get().getMoney();
        Double receiverMoney = receiver.get().getMoney();
        sender.get().setMoney(senderMoney - newAmount);
        receiver.get().setMoney(receiverMoney + newAmount);

        accountRepository.save(sender.get());
        accountRepository.save(receiver.get());

        Transaction transaction = Transaction.builder()
                .amount(newAmount)
                .date(new Date())
                .senderAccountId(sender.get().getAccountId())
                .receiverAccountId(receiver.get().getAccountId())
                .build();

        return ResponseEntity.ok(transactionRepository.save(transaction));
    }

    public List<Transaction> findAllAccountTransaction(Integer id, String token) {
        Optional<Account> account = accountRepository.findById(id);
        String emailFromToken = jwtService.extractEmail(token);

        if (account.isEmpty()) {
            throw new IllegalArgumentException("Account not found");
        }
        if (!accountService.isEmailValid(account.get(), emailFromToken)) {
            throw new SecurityException("Invalid email for this token");
        }

        List<Transaction> transactions = transactionRepository.findByAccountId(id);
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for account ID: " + id);
        }
        return transactions;
    }

}
