package com.springprojects.reactivetransaction.repository;

import com.springprojects.reactivetransaction.controller.model.Account;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface AccountRepository extends R2dbcRepository<Account, Long> {
    Mono<Account> findByName(String name);
}