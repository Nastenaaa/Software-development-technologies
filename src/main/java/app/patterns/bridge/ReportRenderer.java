package app.patterns.bridge;

import java.io.File;
import java.util.List;

public interface ReportRenderer {
    void startReport(String title);
    void addHeader(List<String> cols);
    void addRow(List<String> cells);
    File endReport();
}
