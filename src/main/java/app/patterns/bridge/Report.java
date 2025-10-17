package app.patterns.bridge;

import java.io.File;

public abstract class Report {
    protected final ReportRenderer renderer;
    protected Report(ReportRenderer renderer) { this.renderer = renderer; }
    public abstract File generate();
}

