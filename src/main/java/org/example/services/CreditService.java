package org.example.services;

import org.example.models.Account;
import org.example.models.Credit;
import org.example.repositories.interfaces.AccountRepository;
import org.example.repositories.interfaces.CreditRepository;
import org.example.repositories.interfaces.FeeRuleRepository;

import java.math.BigDecimal;
import java.util.Optional;

public class CreditService {


    private final CreditRepository creditRepository;
    private final AccountRepository accountRepository;
    private final FeeRuleRepository feeRuleRepository;

    public CreditService(CreditRepository creditRepository,
                         AccountRepository accountRepository,
                         FeeRuleRepository feeRuleRepository) {
        this.creditRepository = creditRepository;
        this.accountRepository = accountRepository;
        this.feeRuleRepository = feeRuleRepository;
    }
public boolean addCredit(Credit credit){
    Optional<Account> accountOpt = accountRepository.findById(credit.getAccount().getId().toString());
    if (accountOpt.isEmpty()) {
        System.out.println(" Compte introuvable en base !");
        return false;
    }
    Account account = accountOpt.get();

    if (!account.isActive()) {
        System.out.println("Ce compte est inactif !");
        return false;
    }
    if (credit.getAmount() == null || credit.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
        System.out.println("Montant invalide !");
        return false;
    }
    BigDecimal soldeMinimumRequis = credit.getAmount().multiply(BigDecimal.valueOf(0.4));
    if (account.getBalance().compareTo(soldeMinimumRequis) < 0) {
        System.out.println("Le solde du compte (" + account.getBalance() +
                ") est insuffisant. Il faut au moins " + soldeMinimumRequis + " pour ce crédit.");
        return false;
    }
    return creditRepository.save(credit);

    }



}
