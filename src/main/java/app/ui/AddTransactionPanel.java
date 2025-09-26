package app.ui;

import app.model.Account;
import app.model.Category;
import app.model.User;
import app.repo.AccountRepository;
import app.repo.CategoryRepository;
import app.service.FinanceService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

class AddTransactionPanel extends JPanel {
    private final User user;
    private final AccountRepository accountRepo;
    private final CategoryRepository categoryRepo;
    private final FinanceService finance;

    private final JComboBox<Account> cbAccount = new JComboBox<>();
    private final JTextField tfAmount = new JTextField(10);
    private final JComboBox<String> cbType = new JComboBox<>(new String[]{"INCOME", "EXPENSE"});
    private final JComboBox<Category> cbCategory = new JComboBox<>();   // ← НОВЕ
    private final JTextField tfDate = new JTextField(10); // yyyy-mm-dd
    private final JTextField tfNote = new JTextField(20);

    AddTransactionPanel(User user,
                        AccountRepository accountRepo,
                        CategoryRepository categoryRepo,
                        FinanceService finance) {
        this.user = user;
        this.accountRepo = accountRepo;
        this.categoryRepo = categoryRepo;
        this.finance = finance;

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        c.gridx = 0; c.gridy = row; add(new JLabel("Рахунок:"), c);
        c.gridx = 1; add(cbAccount, c); row++;

        c.gridx = 0; c.gridy = row; add(new JLabel("Сума:"), c);
        c.gridx = 1; add(tfAmount, c); row++;

        c.gridx = 0; c.gridy = row; add(new JLabel("Тип:"), c);
        c.gridx = 1; add(cbType, c); row++;

        c.gridx = 0; c.gridy = row; add(new JLabel("Категорія:"), c);   // ← НОВЕ
        c.gridx = 1; add(cbCategory, c); row++;

        c.gridx = 0; c.gridy = row; add(new JLabel("Дата (yyyy-mm-dd):"), c);
        c.gridx = 1; add(tfDate, c); row++;

        c.gridx = 0; c.gridy = row; add(new JLabel("Примітка:"), c);
        c.gridx = 1; add(tfNote, c); row++;

        JButton btnSave = new JButton("Зберегти");
        btnSave.addActionListener(e -> onSave());
        c.gridx = 1; c.gridy = row; add(btnSave, c);

        // форматування
        cbAccount.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.getName()));
        cbCategory.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.getName()));

        // підписка: при зміні типу — оновлюємо список категорій
        cbType.addActionListener(e -> reloadCategories());

        // початкові дані
        tfDate.setText(LocalDate.now().toString());
        reloadAccounts();
        reloadCategories();
    }

    private void reloadAccounts() {
        cbAccount.removeAllItems();
        List<Account> accounts = accountRepo.findByUser(user);
        for (Account a : accounts) cbAccount.addItem(a);
    }

    private void reloadCategories() {
        cbCategory.removeAllItems();
        String type = (String) cbType.getSelectedItem();
        if (type == null) return;
        List<Category> cats = categoryRepo.findByType(type);
        for (Category c : cats) cbCategory.addItem(c);
    }

    private void onSave() {
        try {
            Account acc = (Account) cbAccount.getSelectedItem();
            if (acc == null) { JOptionPane.showMessageDialog(this, "Немає рахунків"); return; }

            BigDecimal amount = new BigDecimal(tfAmount.getText().trim());
            LocalDate date = LocalDate.parse(tfDate.getText().trim());
            String note = tfNote.getText().trim();
            String type = (String) cbType.getSelectedItem();

            Category cat = (Category) cbCategory.getSelectedItem(); // ← беремо обрану категорію

            if ("INCOME".equals(type)) {
                finance.addIncome(user, acc, cat, amount, date, note);
            } else {
                finance.addExpense(user, acc, cat, amount, date, note);
            }

            JOptionPane.showMessageDialog(this, "Збережено");
            tfAmount.setText("");
            tfNote.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Помилка: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
