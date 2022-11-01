package bank.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Transaction {
    private String transactionType;
    private LocalDateTime localDateTime;
    private double amount;

    public Transaction(String transactionType, LocalDateTime localDateTime, double amount) {
        this.transactionType = transactionType;
        this.localDateTime = localDateTime;
        this.amount = amount;
    }
}
