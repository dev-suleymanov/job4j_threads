package ru.job4j.cash;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class AccountStorage {
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        return accounts.putIfAbsent(account.id(), account) == null;
    }

    public synchronized boolean update(Account account) {
        return accounts.replace(account.id(), account) != null;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        Optional<Account> fromAccount = getById(fromId);
        Optional<Account> toAccount = getById(toId);
        int fromAmount = fromAccount.get().amount();
        boolean result = !fromAccount.isEmpty() && !toAccount.isEmpty()
                && fromAmount >= amount;
        if (result) {
            update(new Account(fromId, fromAmount - amount));
            update(new Account(toId, toAccount.get().amount() + amount));
        }
        return result;
    }
}
