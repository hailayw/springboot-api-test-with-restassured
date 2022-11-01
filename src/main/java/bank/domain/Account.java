package bank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {
    List<Transaction> transactionList = new ArrayList<>();
    private Integer accountNumber;
    private String accountHolder;
    private Double balance = 0.0;

    public Account(Integer accountNumber, String accountHolder) {
        this.accountHolder = accountHolder;
        this.accountNumber = accountNumber;
    }

    public void deposit(double amount) {
        this.balance += Double.valueOf(amount);
        transactionList.add(new Transaction("DEPOSIT", LocalDateTime.now(), amount));
    }

    public void withdraw(double amount) {
        this.balance -= Double.valueOf(amount);
        transactionList.add(new Transaction("WITHDRAWAL", LocalDateTime.now(), amount));
    }
}
