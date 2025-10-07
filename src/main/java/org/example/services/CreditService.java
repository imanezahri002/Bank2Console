package org.example.services;

import org.example.models.Account;
import org.example.models.Client;
import org.example.models.Credit;
import org.example.models.FeeRule;
import org.example.repositories.interfaces.AccountRepository;
import org.example.repositories.interfaces.ClientRepository;
import org.example.repositories.interfaces.CreditRepository;
import org.example.repositories.interfaces.FeeRuleRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

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
    Client client = account.getClient();
    BigDecimal salaire = BigDecimal.valueOf(client.getSalaire());
    FeeRule appliedFeeRule = null;
    BigDecimal feeValue = BigDecimal.ZERO;
    BigDecimal totalAmount = credit.getAmount();

    Optional<FeeRule>feeRuleOpt=feeRuleRepository.findByAmount(credit.getAmount(),FeeRule.OperationType.CREDIT);

    if (feeRuleOpt.isPresent()) {
        FeeRule feeRule = feeRuleOpt.get();

        if (feeRule.getMode() == FeeRule.FeeMode.PERCENT) {
            // Calcul du pourcentage : (amount * fee_value / 100)
            feeValue = credit.getAmount().multiply(feeRule.getFeeValue()).divide(BigDecimal.valueOf(100));
        } else if (feeRule.getMode() == FeeRule.FeeMode.FIX) {
            feeValue = feeRule.getFeeValue();
        }

        totalAmount = totalAmount.add(feeValue);
        appliedFeeRule = feeRule;
    }else {
        System.out.println("Aucune règle de frais applicable trouvée.");
    }

    BigDecimal dureeMois = BigDecimal.valueOf(credit.getDuree()); // suppose que getDuree() renvoie le nombre de mois
    BigDecimal mensualite = totalAmount.divide(dureeMois, 2, RoundingMode.HALF_UP);


    BigDecimal maxMensualite = salaire.multiply(BigDecimal.valueOf(0.4)); // 40% du salaire
    if (mensualite.compareTo(maxMensualite) > 0) {
        System.out.println("Crédit refusé : la mensualité (" + mensualite + ") dépasse 40% du salaire (" + maxMensualite + ")");
        return false;
    }
    credit.setMensualite(mensualite);
    credit.setAmount(totalAmount);
    return creditRepository.save(credit);

    }

public boolean validerCredit(UUID creditId) {
    Optional<Credit> creditOp = creditRepository.findById(creditId);
    if (creditOp.isEmpty()) {
        System.out.println("Credit introuvable !");
        return false;
    }
    Credit credit=creditOp.get();
    if(credit.getStatus()!= Credit.CreditStatus.PENDING) {
        System.out.println("le credit n'est pas en attente");
        return false;
    }
    credit.setStatus(Credit.CreditStatus.ACTIVE);
    creditRepository.validate(credit.getId());
    return true;
}

}


