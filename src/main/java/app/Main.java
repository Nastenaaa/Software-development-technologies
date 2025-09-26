package app;

import app.model.User;
import app.repo.AccountRepository;
import app.repo.TransactionRepository;
import app.repo.CategoryRepository;
import app.repo.jdbc.AccountRepositoryJdbc;
import app.repo.jdbc.TransactionRepositoryJdbc;
import app.repo.jdbc.CategoryRepositoryJdbc;
import app.service.ExportService;
import app.service.FinanceService;
import app.ui.AppFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        User currentUser = new User();
        currentUser.setId(1);

        AccountRepository accountRepo   = new AccountRepositoryJdbc();
        TransactionRepository txRepo    = new TransactionRepositoryJdbc();
        CategoryRepository categoryRepo = new CategoryRepositoryJdbc(); // ← додали
        FinanceService financeService   = new FinanceService(txRepo, accountRepo);
        ExportService exportService     = new ExportService(txRepo);

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new AppFrame(
                    currentUser,
                    accountRepo,
                    txRepo,
                    financeService,
                    exportService,
                    categoryRepo
            ).setVisible(true);
        });
    }
}
