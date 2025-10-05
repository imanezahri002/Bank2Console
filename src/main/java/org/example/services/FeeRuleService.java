package org.example.services;


import org.example.models.FeeRule;
import org.example.repositories.interfaces.FeeRuleRepository;

import java.math.BigDecimal;

public class FeeRuleService {
    private final FeeRuleRepository feeRuleRepository;

    public FeeRuleService(FeeRuleRepository feeRuleRepository){
        this.feeRuleRepository=feeRuleRepository;

    }

    public boolean addFeeRule(FeeRule feeRule){
        if(feeRule.getMinAmount().compareTo(feeRule.getMaxAmount())>=0){
            System.out.println("Erreur : le montant minimum doit être inférieur au montant maximum !");
            return false;
        }
        if (feeRule.getFeeValue().compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Erreur : la valeur du frais doit être positive !");
            return false;
        }
        if (feeRule.getMode() == FeeRule.FeeMode.PERCENT && feeRule.getFeeValue().compareTo(new BigDecimal("100")) > 0) {
            System.out.println("Erreur : le pourcentage ne peut pas dépasser 100% !");
            return false;
        }
        return feeRuleRepository.save(feeRule);
    }
}
