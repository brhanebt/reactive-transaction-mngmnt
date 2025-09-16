package com.springprojects.reactivetransaction.service;

import com.springprojects.reactivetransaction.controller.model.Account;
import com.springprojects.reactivetransaction.repository.AccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AccountService {
    
    private final AccountRepository accountRepository;
    
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    @Transactional()
    public Mono<Void> transfer(String fromAccount, String toAccount, Double amount) {
        return accountRepository.findByName(fromAccount)
            .flatMap(from -> {
                from.setBalance(from.getBalance() - amount);
                return accountRepository.save(from);
            })
            .flatMap(saved -> accountRepository.findByName(toAccount))
            .flatMap(to -> {
                to.setBalance(to.getBalance() + amount);
                return accountRepository.save(to);
            })
            .then();
    }

    public Mono<List<Account>> getAllAccounts() {
        return accountRepository.findAll().collectList();
    }
    
    public Flux<Account> findAllAsFlux() {
        return accountRepository.findAll();
    }
    
    public Mono<Account> findById(Long id) {
        return accountRepository.findById(id);
    }
    
    public Mono<Account> save(Account account) {
        return accountRepository.save(account);
    }
}