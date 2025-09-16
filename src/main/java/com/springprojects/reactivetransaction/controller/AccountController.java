package com.springprojects.reactivetransaction.controller;

import com.springprojects.reactivetransaction.controller.model.Account;
import com.springprojects.reactivetransaction.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public Mono<ResponseEntity<List<Account>>> getAllAccounts() {
        return accountService.getAllAccounts()
            .map(ResponseEntity::ok);
    }
    
    // More Mono examples
    @GetMapping("/{id}")
    public Mono<Account> getAccount(@PathVariable Long id) {
        return accountService.findById(id);
    }
    
    @PostMapping
    public Mono<Account> createAccount(@RequestBody Account account) {
        return accountService.save(account);
    }
    
    @PostMapping("/transfer")
    public Mono<String> transfer(@RequestParam String from, @RequestParam String to, @RequestParam Double amount) {
        return accountService.transfer(from, to, amount)
            .then(Mono.just("Transfer completed"));
    }
    
    // Flux examples
    @GetMapping("/stream")
    public Flux<Account> streamAccounts() {
        return accountService.findAllAsFlux();
    }
    
    @GetMapping("/high-balance")
    public Flux<Account> getHighBalanceAccounts(@RequestParam(defaultValue = "500") Double minBalance) {
        return accountService.findAllAsFlux()
            .filter(account -> account.getBalance() > minBalance);
    }
    
    @GetMapping("/names")
    public Mono<String> getAccountNames() {
        return accountService.findAllAsFlux()
            .map(Account::getName)
            .collectList()
            .map(names -> String.join(", ", names));
    }
}