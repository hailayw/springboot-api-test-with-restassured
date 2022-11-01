package bank.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
public class Accounts {
    private List<Account> accounts;
}
