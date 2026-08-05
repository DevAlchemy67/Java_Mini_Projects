# Personal Finance Tracker (CLI)

This is a starter Java CLI personal finance tracker designed for someone learning full-stack development and computer science. It's small but functional, interactive, and easy to extend.

Features
- Create accounts
- Add income/expense transactions
- View balances and transaction history
- Persist data to a JSON file (data/finance.json)
- Simple, modular code structure to learn from and extend

Quick start
1. Install JDK 17 and Maven
2. Clone the repo and checkout the branch `starter/finance-cli`
3. Run:

   mvn -q compile exec:java

Project structure
- src/main/java/devalchemy/finance - main app and core services
- data/finance.json - persistent storage (auto-created)

Next steps / ideas to extend
- Add categories and budgeting
- Add authentication (local user profiles)
- Add CSV import/export or integrate with a web frontend
- Add unit tests and CI

