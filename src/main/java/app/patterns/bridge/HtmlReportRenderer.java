package app.patterns.bridge;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HtmlReportRenderer implements ReportRenderer {
    private File out;
    private BufferedWriter bw;

    @Override
    public void startReport(String title) {
        try {
            out = File.createTempFile("report-", ".html");
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(out), StandardCharsets.UTF_8));
            bw.write("<!doctype html><html><head><meta charset='utf-8'>");
            bw.write("<style>table{border-collapse:collapse}td,th{border:1px solid #999;padding:4px 8px}</style>");
            bw.write("</head><body>");
            bw.write("<h3>" + esc(title) + "</h3><table>");
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    @Override public void addHeader(List<String> cols) {
        writeRow(cols, "th");
    }
    @Override public void addRow(List<String> cells) {
        writeRow(cells, "td");
    }
    @Override
    public File endReport() {
        try {
            bw.write("</table></body></html>");
            bw.flush(); bw.close();
        } catch (IOException ignored) {}
        return out;
    }
    private void writeRow(List<String> cells, String tag) {
        try {
            bw.write("<tr>");
            for (String c : cells) bw.write("<" + tag + ">" + esc(c) + "</" + tag + ">");
            bw.write("</tr>");
        } catch (IOException e) { throw new RuntimeException(e); }
    }
    private String esc(String s){ return s==null? "": s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;"); }
}

