package app.ui;

import app.model.User;
import app.repo.AccountRepository;
import app.repo.TransactionRepository;
import app.repo.CategoryRepository;     // ← додано
import app.service.ExportService;
import app.service.FinanceService;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {

    public AppFrame(User user,
                    AccountRepository accountRepo,
                    TransactionRepository txRepo,
                    FinanceService financeService,
                    ExportService exportService,
                    CategoryRepository categoryRepo) {

        super("Personal Accounting");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Вкладки
        JTabbedPane tabs = new JTabbedPane();

        // 1) Додати транзакцію (передаємо categoryRepo)
        tabs.addTab(
                "Додати транзакцію",
                new AddTransactionPanel(user, accountRepo, categoryRepo, financeService)
        );

        // 2) Перегляд / Експорт
        tabs.addTab(
                "Транзакції / Експорт",
                new TransactionsPanel(user, txRepo, exportService, accountRepo)
        );

        add(tabs, BorderLayout.CENTER);
    }
}
