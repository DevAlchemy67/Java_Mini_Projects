package devalchemy.finance;

import devalchemy.finance.service.FinanceManager;
import devalchemy.finance.util.InputUtils;
import devalchemy.finance.model.Transaction;
import devalchemy.finance.model.Account;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Personal Finance Tracker (CLI) - Starter");
        FinanceManager manager = new FinanceManager("data/finance.json");
        manager.load();

        boolean running = true;
        while (running) {
            System.out.println("\n==== Main Menu ====");
            System.out.println("1) Add account");
            System.out.println("2) Add transaction");
            System.out.println("3) List transactions");
            System.out.println("4) View balances");
            System.out.println("5) Export CSV");
            System.out.println("6) Sample data");
            System.out.println("0) Exit");

            int choice = InputUtils.nextInt("Choose an option: ");
            switch (choice) {
                case 1 -> {
                    String name = InputUtils.nextLine("Account name: ");
                    Account account = new Account(name);
                    manager.addAccount(account);
                    manager.save();
                    System.out.println("Account added: " + name);
                }
                case 2 -> {
                    manager.printAccounts();
                    String accId = InputUtils.nextLine("Enter account ID (or blank to cancel): ");
                    if (accId.isBlank()) break;
                    String dateStr = InputUtils.nextLine("Date (YYYY-MM-DD) [today]: ");
                    LocalDate date = dateStr.isBlank() ? LocalDate.now() : LocalDate.parse(dateStr);
                    BigDecimal amount = InputUtils.nextBigDecimal("Amount (use negative for expenses): ");
                    String category = InputUtils.nextLine("Category: ");
                    String desc = InputUtils.nextLine("Description: ");
                    Transaction t = new Transaction(date, amount, category, desc, accId);
                    manager.addTransaction(t);
                    manager.save();
                    System.out.println("Transaction added: " + t.getId());
                }
                case 3 -> {
                    manager.listTransactions();
                }
                case 4 -> {
                    manager.printBalances();
                }
                case 5 -> {
                    String path = InputUtils.nextLine("CSV path [export.csv]: ");
                    if (path.isBlank()) path = "export.csv";
                    manager.exportCsv(path);
                    System.out.println("Exported to " + path);
                }
                case 6 -> {
                    manager.createSampleData();
                    manager.save();
                    System.out.println("Sample data created.");
                }
                case 0 -> {
                    running = false;
                    manager.save();
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid option");
            }
        }
    }
}
