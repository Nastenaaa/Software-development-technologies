package app.ui;

import app.model.Account;
import app.model.Category;
import app.model.User;
import app.repo.AccountRepository;
import app.repo.CategoryRepository;
import app.repo.TransactionRepository;
import app.service.FinanceService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AddTransactionPanel extends JPanel {

    private final User user;
    private final AccountRepository accountRepo;
    private final CategoryRepository categoryRepo;
    private final TransactionRepository txRepo;     // ← Потрібно для балансу
    private final FinanceService finance;

    // UI
    private final JComboBox<Account> cbAccount = new JComboBox<>();
    private final JTextField tfAmount = new JTextField(10);
    private final JComboBox<String> cbType = new JComboBox<>(new String[]{"INCOME", "EXPENSE"});
    private final JComboBox<Category> cbCategory = new JComboBox<>();
    private final JTextField tfDate = new JTextField(10);
    private final JTextField tfNote = new JTextField(20);


    private final JLabel  lbStateTitle = new JLabel("Статус рахунку:");
    private final JLabel  lbStateChip  = new JLabel("—");
    private final JButton btnFreeze    = new JButton("Заморозити");
    private final JButton btnUnfreeze  = new JButton("Розморозити");
    private final JButton btnClose     = new JButton("Закрити");

    public AddTransactionPanel(User user,
                               AccountRepository accountRepo,
                               CategoryRepository categoryRepo,
                               TransactionRepository txRepo,
                               FinanceService finance) {

        this.user = user;
        this.accountRepo = accountRepo;
        this.categoryRepo = categoryRepo;
        this.txRepo = txRepo;
        this.finance = finance;

        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(12, 12, 12, 12));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;

        // Рахунок
        c.gridx = 0; c.gridy = row; add(new JLabel("Рахунок:"), c);
        c.gridx = 1; add(cbAccount, c); row++;

        // Статусна смужка
        JPanel statusBar = buildStatusBar();
        c.gridx = 0; c.gridy = row; add(lbStateTitle, c);
        c.gridx = 1; add(statusBar, c); row++;

        // Сума
        c.gridx = 0; c.gridy = row; add(new JLabel("Сума:"), c);
        c.gridx = 1; add(tfAmount, c); row++;

        // Тип
        c.gridx = 0; c.gridy = row; add(new JLabel("Тип:"), c);
        c.gridx = 1; add(cbType, c); row++;

        // Категорія
        c.gridx = 0; c.gridy = row; add(new JLabel("Категорія:"), c);
        c.gridx = 1; add(cbCategory, c); row++;

        // Дата
        c.gridx = 0; c.gridy = row; add(new JLabel("Дата (yyyy-mm-dd):"), c);
        c.gridx = 1; add(tfDate, c); row++;

        // Нотатка
        c.gridx = 0; c.gridy = row; add(new JLabel("Примітка:"), c);
        c.gridx = 1; add(tfNote, c); row++;

        // Кнопка «Зберегти»
        JButton btnSave = new JButton("Зберегти");
        btnSave.addActionListener(e -> onSave());
        c.gridx = 1; c.gridy = row; add(btnSave, c);

        // Рендери комбобоксів
        cbAccount.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.getName()));
        cbCategory.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.getName()));

        // Слухачі
        cbType.addActionListener(e -> reloadCategories());
        cbAccount.addActionListener(e -> updateStateUI()); // змінюємо рахунок → оновлюємо стан

        // Початкові значення
        tfDate.setText(LocalDate.now().toString());
        reloadAccounts();
        reloadCategories();
        updateStateUI(); // один раз після побудови
    }

    /* ---------- UI helpers ---------- */

    private JPanel buildStatusBar() {
        // «чіп» статусу
        lbStateChip.setOpaque(true);
        lbStateChip.setForeground(Color.WHITE);
        lbStateChip.setBorder(new EmptyBorder(4, 10, 4, 10));
        lbStateChip.setBackground(new Color(0x2E7D32)); // зелений стартом

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.add(lbStateChip);
        p.add(btnFreeze);
        p.add(btnUnfreeze);
        p.add(btnClose);

        // Кнопки переходів
        btnFreeze.addActionListener(e -> {
            Account acc = (Account) cbAccount.getSelectedItem();
            if (acc == null) return;
            acc.block();               // ACTIVE → BLOCKED
            updateStateUI();
        });

        btnUnfreeze.addActionListener(e -> {
            Account acc = (Account) cbAccount.getSelectedItem();
            if (acc == null) return;
            acc.activate();            // BLOCKED → ACTIVE
            updateStateUI();
        });

        btnClose.addActionListener(e -> {
            Account acc = (Account) cbAccount.getSelectedItem();
            if (acc == null) return;

            BigDecimal bal = currentBalanceFor(acc);
            if (bal.compareTo(BigDecimal.ZERO) != 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Закрити рахунок можна лише з нульовим балансом.",
                        "Неможливо закрити", JOptionPane.WARNING_MESSAGE);
                return;
            }
            acc.close();               // ACTIVE/BLOCKED → CLOSED
            updateStateUI();
        });

        return p;
    }

    private void setChip(String text, Color bg) {
        lbStateChip.setText(text);
        lbStateChip.setBackground(bg);
    }

    private void updateStateUI() {
        Account acc = (Account) cbAccount.getSelectedItem();
        if (acc == null) {
            setChip("—", Color.GRAY);
            btnFreeze.setEnabled(false);
            btnUnfreeze.setEnabled(false);
            btnClose.setEnabled(false);
            return;
        }

        // Який стан (беремо назву класу)
        String name = acc.getState().getClass().getSimpleName();
        BigDecimal bal = currentBalanceFor(acc);

        switch (name) {
            case "ActiveState" -> {
                setChip("Активний", new Color(0x2E7D32));
                btnFreeze.setEnabled(true);
                btnUnfreeze.setEnabled(false);
                btnClose.setEnabled(bal.compareTo(BigDecimal.ZERO) == 0);
            }
            case "BlockedState" -> {
                setChip("Заморожений", new Color(0xEF6C00));
                btnFreeze.setEnabled(false);
                btnUnfreeze.setEnabled(true);
                btnClose.setEnabled(bal.compareTo(BigDecimal.ZERO) == 0);
            }
            default -> { // ClosedState
                setChip("Закритий", new Color(0x607D8B));
                btnFreeze.setEnabled(false);
                btnUnfreeze.setEnabled(false);
                btnClose.setEnabled(false);
            }
        }
        revalidate();
        repaint();
    }

    private BigDecimal currentBalanceFor(Account acc) {
        // З карти балансів користувача беремо баланс саме цього рахунку
        return txRepo
                .balancesByUser(user.getId())
                .getOrDefault(acc.getId(), BigDecimal.ZERO);
    }

    /* ---------- Data loaders ---------- */

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
        for (Category cat : cats) cbCategory.addItem(cat);
    }

    /* ---------- Save ---------- */

    private void onSave() {
        try {
            Account acc = (Account) cbAccount.getSelectedItem();
            if (acc == null) {
                JOptionPane.showMessageDialog(this, "Немає рахунків");
                return;
            }

            BigDecimal amount = new BigDecimal(tfAmount.getText().trim());
            LocalDate date = LocalDate.parse(tfDate.getText().trim());
            String note = tfNote.getText().trim();
            String type = (String) cbType.getSelectedItem();
            Category cat = (Category) cbCategory.getSelectedItem();

            if ("INCOME".equals(type)) {
                finance.addIncome(user, acc, cat, amount, date, note);
            } else {
                finance.addExpense(user, acc, cat, amount, date, note);
            }

            JOptionPane.showMessageDialog(this, "Збережено");
            tfAmount.setText("");
            tfNote.setText("");
            // після успішної операції оновлюємо панель (баланс міг змінитись → впливає на доступність «Закрити»)
            updateStateUI();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Помилка: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
