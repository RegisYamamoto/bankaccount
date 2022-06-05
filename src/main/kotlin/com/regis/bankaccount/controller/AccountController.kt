package com.regis.bankaccount.controller

import com.regis.bankaccount.model.Account
import com.regis.bankaccount.repository.AccountRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException

@RestController
@RequestMapping("/accounts")
class AccountController(val repository: AccountRepository) {

    @PostMapping
    fun create(@RequestBody account: Account) =
        ResponseEntity.ok(repository.save(account))

    @GetMapping
    fun read() = ResponseEntity.ok(repository.findAll())

    @PutMapping("{document}")
    fun update(@PathVariable document: String, @RequestBody account: Account): ResponseEntity<Account> {
        val accountDBOptional = repository.findByDocument(document)
        val toSave = accountDBOptional
                .orElseThrow { RuntimeException("Account document: $document not found!") }
                .copy(name = account.name, balance = account.balance)
        return ResponseEntity.ok(repository.save(toSave))
    }

    @DeleteMapping("{document}")
    fun delete(@PathVariable document: String) {
        val accountDBOptional = repository.findByDocument(document)
        val toDelete = accountDBOptional
            .orElseThrow { RuntimeException("Account document: $document not found!") }
        repository.delete(toDelete)
    }

}