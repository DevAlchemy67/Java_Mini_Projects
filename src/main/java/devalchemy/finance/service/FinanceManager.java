package devalchemy.finance.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import devalchemy.finance.model.Account;
import devalchemy.finance.model.Transaction;
import devalchemy.finance.util.LocalDateAdapter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Comparator;

public class FinanceManager {
    private final File storage;
    private final Gson gson;
    private List<Account> accounts = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    public FinanceManager(String storagePath) {
        this.storage = new File(storagePath);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }

    public void load() {
        try {
            if (!storage.exists()) {
                storage.getParentFile().mkdirs();
                save();
                return;
            }
            try (FileReader fr = new FileReader(storage)) {
                DataStore ds = gson.fromJson(fr, DataStore.class);
                if (ds != null) {
                    this.accounts = ds.accounts != null ? ds.accounts : new ArrayList<>();
                    this.transactions = ds.transactions != null ? ds.transactions : new ArrayList<>();
                }
                recalcBalances();
            }
        } catch (Exception e) {
            System.err.println("Failed to load data: " + e.getMessage());
        }
    }

    public void save() {
        try {
            DataStore ds = new DataStore();
            ds.accounts = this.accounts;
            ds.transactions = this.transactions;
            try (FileWriter fw = new FileWriter(storage)) {
                gson.toJson(ds, fw);
            }
        } catch (IOException e) {
            System.err.println("Failed to save data: " + e.getMessage());
        }
    }

    public void addAccount(Account a) {
        accounts.add(a);
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        Account acc = findAccount(t.getAccountId());
        if (acc != null) acc.applyTransaction(t.getAmount());
    }

    public Account findAccount(String id) {
        return accounts.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    public void listTransactions() {
        List<Transaction> sorted = transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .collect(Collectors.toList());
        if (sorted.isEmpty()) System.out.println("No transactions.");
        for (Transaction t : sorted) {
            System.out.println(t);
        }
    }

    public void printAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts yet. Create one first.");
            return;
        }
        System.out.println("Accounts:");
        for (Account a : accounts) System.out.println(a);
    }

    public void printBalances() {
        if (accounts.isEmpty()) { System.out.println("No accounts."); return; }
        System.out.println("Balances:");
        for (Account a : accounts) System.out.println(a.getName() + " -> " + a.getBalance());
    }

    private void recalcBalances() {
        Map<String, BigDecimal> map = new HashMap<>();
        for (Account a : accounts) map.put(a.getId(), BigDecimal.ZERO);
        for (Transaction t : transactions) {
            map.put(t.getAccountId(), map.getOrDefault(t.getAccountId(), BigDecimal.ZERO).add(t.getAmount()));
        }
        for (Account a : accounts) a.applyTransaction(map.getOrDefault(a.getId(), BigDecimal.ZERO).subtract(a.getBalance()));
    }

    public void exportCsv(String path) {
        try {
            List<String> lines = new ArrayList<>();
            lines.add("id,date,amount,category,description,accountId");
            for (Transaction t : transactions) {
                lines.add(String.format("%s,%s,%s,%s,%s,%s",
                        t.getId(), t.getDate(), t.getAmount(), safe(t.getCategory()), safe(t.getDescription()), t.getAccountId()));
            }
            Files.write(new File(path).toPath(), lines);
        } catch (IOException e) {
            System.err.println("Failed to export CSV: " + e.getMessage());
        }
    }

    private String safe(String s) { return s == null ? "" : s.replace(",", " "); }

    public void createSampleData() {
        Account a1 = new Account("Checking");
        Account a2 = new Account("Savings");
        accounts.add(a1);
        accounts.add(a2);
        transactions.add(new Transaction(LocalDate.now().minusDays(3), new BigDecimal("-12.50"), "Food", "Lunch", a1.getId()));
        transactions.add(new Transaction(LocalDate.now().minusDays(2), new BigDecimal("1500.00"), "Salary", "Paycheck", a1.getId()));
        transactions.add(new Transaction(LocalDate.now().minusDays(1), new BigDecimal("-200.00"), "Rent", "Monthly rent", a1.getId()));
        recalcBalances();
    }

    private static class DataStore {
        List<Account> accounts = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();
    }
}
