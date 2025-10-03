package app.ui;

import app.model.User;
import app.repo.AccountRepository;
import app.repo.CategoryRepository;
import app.repo.TransactionRepository;
import app.service.ExportService;
import app.service.FinanceService;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {

    public AppFrame(User user,
                    AccountRepository accountRepo,
                    TransactionRepository txRepo,     // третій
                    FinanceService financeService,
                    ExportService exportService,
                    CategoryRepository categoryRepo) { // шостий (останній)


        super("Personal Accounting");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        // 1) Додати транзакцію (Варіант B: потрібні categoryRepo і txRepo)
        tabs.addTab(
                "Додати транзакцію",
                new AddTransactionPanel(user, accountRepo, categoryRepo, txRepo, financeService)
        );

        // 2) Перегляд / Експорт (без змін)
        tabs.addTab(
                "Транзакції / Експорт",
                new TransactionsPanel(user, txRepo, exportService, accountRepo)
        );

        add(tabs, BorderLayout.CENTER);
    }
}
