package com.springprojects.reactivetransaction.controller;

import com.springprojects.reactivetransaction.model.Account;
import com.springprojects.reactivetransaction.model.RandomUser;
import com.springprojects.reactivetransaction.model.ApiResponse;
import com.springprojects.reactivetransaction.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final WebClient webClient;

    public AccountController(AccountService accountService, WebClient.Builder webClientBuilder) {
        this.accountService = accountService;
        this.webClient = webClientBuilder.build();
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


    @GetMapping("/external/{id}")
    public Mono<RandomUser> getOneExternalData(@PathVariable String id) {
        return webClient.get()
                .uri("https://api.freeapi.app/api/v1/public/randomusers/" + id)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .map(ApiResponse::getData);
    }

    @GetMapping("/external")
    public Flux<RandomUser> getExternalData(@RequestParam(defaultValue = "5") int count) {
        return Flux.range(1, count)
                .flatMap(i -> webClient.get()
                        .uri("https://api.freeapi.app/api/v1/public/randomusers")
                        .retrieve()
                        .bodyToMono(ApiResponse.class)
                        .map(ApiResponse::getData));
    }
}
