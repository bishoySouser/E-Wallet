package org.wallet.ewallet.transaction;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.wallet.ewallet.wallet.Currency;
import org.wallet.ewallet.wallet.Wallet;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transaction")
@Getter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_wallet_id", nullable = true)
    private Wallet sourceWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_wallet_id", nullable = true)
    private Wallet destinationWallet;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name="transaction_currency")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Currency cannot be null")
    private Currency currency;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type cannot be null")
    private TransactionType type;

    @Column(name = "transaction_status")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status cannot be null")
    private TransactionStatus status;

    @Column(name = "requested_at")
    private Instant requestedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "failed_at")
    private Instant failedAt;

    public static Transaction deposit(
            @Positive BigDecimal amount,
            Wallet destinationWallet
    ) {
        validateAmount(amount);

        Transaction transaction = new Transaction();
        transaction.type = TransactionType.DEPOSIT;
        transaction.status = TransactionStatus.PENDING;
        transaction.amount = amount;
        transaction.sourceWallet = null;
        transaction.destinationWallet = destinationWallet;
        transaction.currency = destinationWallet.getCurrency();
        transaction.requestedAt = Instant.now();
        return transaction;
    }

    public static Transaction withdrawal(
            @Positive BigDecimal amount,
            Wallet sourceWallet
    ) {
        validateAmount(amount);

        Transaction transaction = new Transaction();
        transaction.type = TransactionType.WITHDRAWAL;
        transaction.status = TransactionStatus.PENDING;
        transaction.amount = amount;
        transaction.sourceWallet = sourceWallet;
        transaction.currency = sourceWallet.getCurrency();
        transaction.destinationWallet = null;
        transaction.requestedAt = Instant.now();
        return transaction;
    }

    public static Transaction transfer(
            @Positive BigDecimal amount,
            Wallet sourceWallet,
            Wallet destinationWallet
    ) {
        validateAmount(amount);

        Transaction transaction = new Transaction();
        transaction.type = TransactionType.TRANSFER;
        transaction.status = TransactionStatus.PENDING;
        transaction.amount = amount;
        transaction.sourceWallet = sourceWallet;
        transaction.destinationWallet = destinationWallet;
        transaction.currency = sourceWallet.getCurrency();
        transaction.requestedAt = Instant.now();
        return transaction;
    }

    private static void validateAmount(BigDecimal newAmount) {
        if (newAmount == null || newAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    public void markCompleted() {
        if (status == TransactionStatus.PENDING) {
            this.status = TransactionStatus.COMPLETED;
            this.completedAt = Instant.now();;
        }
    }

    public void markFailed() {
        if (status == TransactionStatus.PENDING) {
            this.status = TransactionStatus.FAILED;
            this.failedAt = Instant.now();
        }
    }
}
