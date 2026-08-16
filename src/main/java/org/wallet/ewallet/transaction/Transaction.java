package org.wallet.ewallet.transaction;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.wallet.ewallet.wallet.Currency;
import org.wallet.ewallet.wallet.Wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Getter
@Setter
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
    @Positive
    private BigDecimal amount;

    @Column(name="transaction_currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "transaction_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

}
