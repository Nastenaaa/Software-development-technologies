package app.patterns.decorator;

import app.model.User;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public abstract class AbstractExportDecorator implements Exporter {
    protected final Exporter inner;

    protected AbstractExportDecorator(Exporter inner) {
        this.inner = inner;
    }

    @Override
    public File export(User user, LocalDate from, LocalDate to) throws IOException {
        return inner.export(user, from, to);
    }
}
