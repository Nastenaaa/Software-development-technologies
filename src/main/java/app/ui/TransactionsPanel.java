package app.ui;

import app.model.Account;
import app.model.Transaction;
import app.model.User;
import app.repo.AccountRepository;
import app.repo.TransactionRepository;
import app.service.ExportService;
import app.patterns.prototype.PrototypeTransaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
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

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Дата", "Рахунок", "Категорія", "Тип", "Сума", "Нотатка"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);

    private final JLabel lbBalances = new JLabel("Баланс: —");

    // Кеш останнього набору, щоб зручно брати вибраний запис
    private List<Transaction> current = new ArrayList<>();

    TransactionsPanel(User user,
                      TransactionRepository txRepo,
                      ExportService exportService,
                      AccountRepository accountRepo) {
        this.user = user;
        this.txRepo = txRepo;
        this.exportService = exportService;
        this.accountRepo = accountRepo;

        setLayout(new BorderLayout(8,8));

        // Верхня панель керування
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

        JButton btnDuplicate = new JButton("Дублювати");
        btnDuplicate.addActionListener(e -> {
            duplicateSelected();
            updateBalances();
        });
        top.add(btnDuplicate);

        JButton btnExport = new JButton("Експорт CSV");
        btnExport.addActionListener(e -> exportCsv());
        top.add(btnExport);

        JButton btnExportHtml = new JButton("Експорт HTML");
        btnExportHtml.addActionListener(e -> exportHtml());
        top.add(btnExportHtml);

        top.add(Box.createHorizontalStrut(16));
        lbBalances.setFont(lbBalances.getFont().deriveFont(Font.BOLD));
        top.add(lbBalances);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);


        load();
        updateBalances();

        table.setRowHeight(22);
        table.setAutoCreateRowSorter(true);
    }


    private void load() {
        try {
            LocalDate from = LocalDate.parse(tfFrom.getText().trim());
            LocalDate to   = LocalDate.parse(tfTo.getText().trim());
            current = txRepo.findByUserAndPeriod(user, from, to);

            model.setRowCount(0);
            for (Transaction t : current) {
                String accName = (t.getAccount()  != null) ? t.getAccount().getName()  : "";
                String catName = (t.getCategory() != null) ? t.getCategory().getName() : "";
                model.addRow(new Object[]{
                        t.getDate(),
                        accName,
                        catName,
                        t.getType(),
                        t.getAmount(),
                        t.getNote()
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Помилка завантаження: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void updateBalances() {
        try {
            var balances = txRepo.balancesByUser(user.getId()); // Map<accountId, BigDecimal>

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
            lbBalances.setText("Баланс: (помилка)");
        }
    }


    private void duplicateSelected() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Виберіть рядок у таблиці для дублювання.");
            return;
        }
        int row = table.convertRowIndexToModel(viewRow);
        if (row < 0 || row >= current.size()) return;

        Transaction original = current.get(row);
        try {
            Transaction clone = PrototypeTransaction
                    .from(original)
                    .copyWith(LocalDate.now(), null, "Повтор: " + (original.getNote() == null ? "" : original.getNote()));

            txRepo.save(clone);
            JOptionPane.showMessageDialog(this, "Створено копію транзакції.");
            load();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Не вдалося дублювати: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
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

    private void exportHtml() {
        try {
            LocalDate from = LocalDate.parse(tfFrom.getText().trim());
            LocalDate to   = LocalDate.parse(tfTo.getText().trim());
            File f = exportService.exportTransactions(user, from, to, ExportService.Format.HTML);
            JOptionPane.showMessageDialog(this, "Експортовано у файл:\n" + f.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Помилка експорту: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
