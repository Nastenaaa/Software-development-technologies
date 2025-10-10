package app.patterns.decorator;

import app.model.User;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public interface Exporter {
    File export(User user, LocalDate from, LocalDate to) throws IOException;
}

