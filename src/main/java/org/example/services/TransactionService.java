package org.example.services;

import org.example.models.Account;
import org.example.models.FeeRule;
import org.example.models.Transaction;
import org.example.repositories.implementations.TransactionRepositoryImpl;
import org.example.repositories.interfaces.AccountRepository;
import org.example.repositories.interfaces.FeeRuleRepository;
import org.example.repositories.interfaces.TransactionRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final FeeRuleRepository feeRuleRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository,FeeRuleRepository feeRuleRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.feeRuleRepository=feeRuleRepository;
    }

    public boolean deposit(String accountId, BigDecimal amount) {
        Optional<Account> optAccount = accountRepository.findById(accountId);

        if (optAccount.isEmpty()) {
            System.out.println("Compte introuvable !");
            return false;
        }

        Account account = optAccount.get();
        account.setBalance(account.getBalance().add(amount));
        Transaction transaction=new Transaction(null,
                amount,
                Transaction.TransactionType.DEPOSIT,
                Transaction.TransactionStatus.COMPLETED,
                Instant.now(),
                optAccount.get(),null,null);

        transactionRepository.save(transaction);
        return accountRepository.update(account);
    }

    public boolean transfer(String idCompteSource,String idCompteDestination ,BigDecimal amount,Transaction.TransactionType type) {
        Optional<Account> accountSource = accountRepository.findById(idCompteSource);
        Optional<Account> accountDestination = accountRepository.findById(idCompteDestination);

        if (accountSource.isEmpty() || accountDestination.isEmpty()) {
            System.out.println("Compte introuvable !");
            return false;
        }


        Account accountS = accountSource.get();
        Account accountD = accountDestination.get();

        if (accountS.getBalance().compareTo(amount) < 0) {
            System.out.println("Fonds insuffisants sur le compte source !");
            return false;
        }
        Transaction.TransactionStatus status;

        if (type == Transaction.TransactionType.TRANSFER_OUT) {
            status = Transaction.TransactionStatus.PENDING;
        } else {
            status = Transaction.TransactionStatus.COMPLETED;
        }

        Transaction transaction=new Transaction(null,
                amount,
                type,
                status,
                Instant.now(),
                accountS,null,null);

        transactionRepository.save(transaction);

        return transactionRepository.transactionTransferIn(accountS, accountD, amount);
    }

    public boolean transferExterne(String idCompteSource,BigDecimal amount,Transaction.TransactionType type) {
        Optional<Account> accountSource = accountRepository.findById(idCompteSource);

        if (accountSource.isEmpty()) {
            System.out.println("Compte introuvable !");
            return false;
        }

        Account accountS = accountSource.get();

        if (accountS.getBalance().compareTo(amount) < 0) {
            System.out.println("Fonds insuffisants sur le compte source !");
            return false;
        }
        Optional<FeeRule>feeRuleOpt=feeRuleRepository.findByAmount(amount,FeeRule.OperationType.TRANSACTION_EXTERNE);

        BigDecimal totalAmount = amount;
        BigDecimal feeValue = BigDecimal.ZERO;
        FeeRule appliedFeeRule = null;

        if (feeRuleOpt.isPresent()) {
            FeeRule feeRule = feeRuleOpt.get();

            if (feeRule.getMode() == FeeRule.FeeMode.PERCENT) {
                // Calcul du pourcentage : (amount * fee_value / 100)
                feeValue = amount.multiply(feeRule.getFeeValue()).divide(BigDecimal.valueOf(100));
            } else if (feeRule.getMode() == FeeRule.FeeMode.FIX) {
                feeValue = feeRule.getFeeValue();
            }
            totalAmount = amount.add(feeValue);
            appliedFeeRule = feeRule;
        }else {
                System.out.println("Aucune règle de frais applicable trouvée.");
            }

            if (accountS.getBalance().compareTo(totalAmount) < 0) {
                System.out.println("Fonds insuffisants sur le compte source !");
                return false;
            }
        accountS.setBalance(accountS.getBalance().subtract(totalAmount));
        Transaction transaction=new Transaction(null,
                amount,
                type,
                Transaction.TransactionStatus.PENDING,
                Instant.now(),
                accountS,
                totalAmount,
                appliedFeeRule);



        transactionRepository.save(transaction);

        System.out.println("Transaction en attente créée avec succès.");

        return true;
    }


    public boolean validertransferOut(String transactionId){
        Optional<Transaction> transaction =transactionRepository.findById(transactionId);
        if ( transaction.isEmpty()) {
            System.out.println("Transaction introuvable !");
            return false;
        }
        return transactionRepository.validate(transactionId);
    }
    public boolean validerTransactionExterne(String transactionId) {
        Optional<Transaction> transactionOpt = transactionRepository.findById(transactionId);

        if (transactionOpt.isEmpty()) {
            System.out.println("Transaction introuvable !");
            return false;
        }
        Transaction transaction = transactionOpt.get();

        if (transaction.getStatus() != Transaction.TransactionStatus.PENDING) {
            System.out.println("La transaction n'est pas en attente !");
            return false;
        }

        Account accountSource = transaction.getAccount();
        BigDecimal amount = transaction.getTotalAmount();


        accountSource.setBalance(accountSource.getBalance().subtract(transaction.getTotalAmount()));
        accountRepository.update(accountSource);

        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionRepository.validate(String.valueOf(transactionId));

        System.out.println("Transaction validée et complétée avec succès.");
        return true;
    }

}
