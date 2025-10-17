package app.patterns.bridge;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvReportRenderer implements ReportRenderer {
    private final String sep = ";";
    private File out;
    private BufferedWriter bw;

    @Override
    public void startReport(String title) {
        try {
            out = File.createTempFile("report-", ".csv");
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(out), StandardCharsets.UTF_8));
            bw.write('\uFEFF');
            bw.write("\"" + title + "\"\r\n");
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    @Override public void addHeader(List<String> cols) {
        writeLine(cols);
    }
    @Override public void addRow(List<String> cells) {
        writeLine(cells);
    }
    @Override
    public File endReport() {
        try { if (bw != null) { bw.flush(); bw.close(); } }
        catch (IOException ignored) {}
        return out;
    }

    private void writeLine(List<String> cells) {
        try {
            String line = cells.stream()
                    .map(this::q)
                    .reduce((a,b) -> a + sep + b).orElse("");
            bw.write(line + "\r\n");
        } catch (IOException e) { throw new RuntimeException(e); }
    }
    private String q(String s){ return "\"" + (s==null?"":s.replace("\"","\"\"")) + "\""; }
}
