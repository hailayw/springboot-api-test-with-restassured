package bank.controller;

import bank.domain.Account;
import bank.domain.Accounts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
public class BankController {

    private Map<Integer, Account> accounts = new HashMap<>();

    public BankController() {
        accounts.put(12345, new Account(12345,"John Au"));
        accounts.put(12346, new Account(12346,"James Jo"));
    }

    @PostMapping("/accounts")
    public ResponseEntity<?> createAccount(Integer accountNumber, String accountHolder) {
        accounts.put(accountNumber, new Account(accountNumber, accountHolder));
        return new ResponseEntity<>(new Account(accountNumber, accountHolder), HttpStatus.CREATED);
    }

    @PostMapping("/accounts/{accountNumber}/deposits")
    public ResponseEntity<?> deposit(@PathVariable Integer accountNumber, @RequestParam double amount) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            return new ResponseEntity<>(new CustomErrorMessage("Account with accountNumber= "
                    + accountNumber + " is not available"), HttpStatus.NOT_FOUND);
        }

        account.deposit(amount);
        return new ResponseEntity<>(accounts.get(accountNumber), HttpStatus.OK);
    }

    @PostMapping("/accounts/{accountNumber}/withdrawals")
    public ResponseEntity<?> withdraw(@PathVariable Integer accountNumber, @RequestParam double amount) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            return new ResponseEntity<>(new CustomErrorMessage("Account with accountNumber= "
                    + accountNumber + " is not available"), HttpStatus.NOT_FOUND);
        }

        account.withdraw(amount);
        return new ResponseEntity<>(accounts.get(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<?> getAccountByAccNumber(@PathVariable Integer accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            return new ResponseEntity<>(new CustomErrorMessage("Account with accountNumber= "
                    + accountNumber + " is not available"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @DeleteMapping("/accounts/{accountNumber}")
    public ResponseEntity<?> removeAccount(@PathVariable Integer accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            return new ResponseEntity<>(new CustomErrorMessage("Account with accountNumber= " + accountNumber + " is not available"),HttpStatus.NOT_FOUND);
        }
        accounts.remove(accountNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/accounts")
    public ResponseEntity<?> getAccounts() {
        List<Account> accountList = new ArrayList<>(accounts.values());

        int page = 10;
        int limit = page < accountList.size() ? page : accountList.size();

        if (accountList == null || accountList.size()==0) {
            return new ResponseEntity<>(new CustomErrorMessage("No account was found in the accounts list"),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accountList.subList(0,limit), HttpStatus.OK);
    }

}
