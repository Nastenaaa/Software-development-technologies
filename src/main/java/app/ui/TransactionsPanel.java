package app.ui;

import app.model.Account;
import app.model.Transaction;
import app.model.User;
import app.repo.AccountRepository;
import app.repo.TransactionRepository;
import app.service.ExportService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TransactionsPanel extends JPanel {
    private final User user;
    private final TransactionRepository txRepo;
    private final ExportService exportService;
    private final AccountRepository accountRepo;

    private final JTextField tfFrom = new JTextField(10);
    private final JTextField tfTo   = new JTextField(10);
    private final JTable table = new JTable(new DefaultTableModel(
            new Object[]{"Дата", "Сума", "Тип", "Нотатка"}, 0));

    // NEW: баланс по рахунках
    private final JLabel lbBalances = new JLabel("Баланс: —");

    TransactionsPanel(User user,
                      TransactionRepository txRepo,
                      ExportService exportService,
                      AccountRepository accountRepo) {
        this.user = user;
        this.txRepo = txRepo;
        this.exportService = exportService;
        this.accountRepo = accountRepo;

        setLayout(new BorderLayout(8,8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Від:"));
        tfFrom.setText(LocalDate.now().minusMonths(1).toString());
        top.add(tfFrom);
        top.add(new JLabel("До:"));
        tfTo.setText(LocalDate.now().toString());
        top.add(tfTo);

        JButton btnLoad = new JButton("Завантажити");
        btnLoad.addActionListener(e -> {
            load();
            updateBalances();
        });
        top.add(btnLoad);

        JButton btnExport = new JButton("Експорт CSV");
        btnExport.addActionListener(e -> exportCsv());
        top.add(btnExport);

        // NEW: трохи відступу і підпис з балансами
        top.add(Box.createHorizontalStrut(16));
        lbBalances.setFont(lbBalances.getFont().deriveFont(Font.BOLD));
        top.add(lbBalances);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // початкове оновлення балансів
        updateBalances();
    }

    private void load() {
        try {
            LocalDate from = LocalDate.parse(tfFrom.getText().trim());
            LocalDate to   = LocalDate.parse(tfTo.getText().trim());
            List<Transaction> list = txRepo.findByUserAndPeriod(user, from, to);

            DefaultTableModel m = (DefaultTableModel) table.getModel();
            m.setRowCount(0);
            for (Transaction t : list) {
                m.addRow(new Object[]{
                        t.getDate(),
                        t.getAmount(),
                        t.getType(),
                        t.getNote()
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Помилка: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // NEW: оновлення підпису з балансами
    private void updateBalances() {
        try {
            var balances = txRepo.balancesByUser(user.getId()); // Map<accountId, BigDecimal>

            // Підтягнути назви рахунків
            Map<Integer, String> names = new HashMap<>();
            for (Account a : accountRepo.findByUser(user)) {
                names.put(a.getId(), a.getName());
            }

            StringBuilder sb = new StringBuilder("Баланс:  ");
            balances.forEach((accId, bal) -> {
                String name = names.getOrDefault(accId, "#" + accId);
                sb.append(name).append(" = ").append(bal).append("   ");
            });
            lbBalances.setText(sb.toString());
        } catch (Exception ex) {
            // якщо щось пішло не так — не валимо UI
            lbBalances.setText("Баланс: (помилка)");
        }
    }

    private void exportCsv() {
        try {
            LocalDate from = LocalDate.parse(tfFrom.getText().trim());
            LocalDate to   = LocalDate.parse(tfTo.getText().trim());
            File f = exportService.exportTransactions(user, from, to, ExportService.Format.CSV);
            JOptionPane.showMessageDialog(this, "Експортовано у файл:\n" + f.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Помилка експорту: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
