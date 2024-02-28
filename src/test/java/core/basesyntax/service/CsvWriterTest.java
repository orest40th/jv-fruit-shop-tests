package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CsvWriterTest {
    private static final String OUTPUT_DATA_FILE = "src/test/resources/report_";
    private static final String OUTPUT_DATA_FILE_INVALID = "src/test/invalid/report_";
    private static final String HEADER = "fruit,quantity" + System.lineSeparator();
    private static final DateTimeFormatter FORMATTED = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd_HH-mm-ss");
    private static CsvWriter csvWriter;

    @BeforeAll
    static void setUp() {
        csvWriter = new CsvWriter();
    }

    @Test
    void writeToFile_validInput_Ok() {
        String finalPath = OUTPUT_DATA_FILE
                + LocalDateTime.now().format(FORMATTED) + ".csv";
        String expected =
                HEADER
                + "banana,107" + System.lineSeparator()
                + "apple,108";
        csvWriter.writeToFile(OUTPUT_DATA_FILE, expected);
        String actual;

        try {
            actual = Files.readAllLines(Path.of(finalPath)).stream()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new RuntimeException("Error reading from file " + finalPath, e);
        }
        assertEquals(expected, actual);
    }

    @Test
    void writeToFile_invalidPath_notOk() {
        String finalPath = OUTPUT_DATA_FILE_INVALID
                + LocalDateTime.now().format(FORMATTED) + ".csv";
        String input =
                HEADER
                        + "banana,107" + System.lineSeparator()
                        + "apple,108";
        assertThrows(RuntimeException.class, () -> {
            csvWriter.writeToFile(finalPath, input);
        });
    }
}
