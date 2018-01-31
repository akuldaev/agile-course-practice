package ru.unn.agile.MortgageCalculator.infrastructure;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

public class FileLoggerTests {
    @Before
    public void setUp() {
        fileLogger = new FileLogger(FILELOGNAME);
    }

    @Test
    public void canCreateLogFileOnDisk() {
        try {
            new BufferedReader(new FileReader(FILELOGNAME));
        } catch (FileNotFoundException e) {
            fail("File " + FILELOGNAME + " wasn't found!");
        }
    }

    @Test
    public void canCreateLoggerWithFileName() {
        assertNotNull(fileLogger);
    }

    @Test
    public void canWriteLogMessage() {
        String testMessages = "Test message";

        fileLogger.log(testMessages);

        String messages = fileLogger.getLog().get(0);
        assertTrue(messages.matches(".*" + testMessages + "$"));
    }

    @Test
    public void doesLogContainDateAndTime() {
        String testMessage = "Test message";

        fileLogger.log(testMessage);

        String messages = fileLogger.getLog().get(0);
        assertTrue(messages.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} > .*"));
    }

    @Test
    public void canWriteSeveralLogMessage() {
        String[] testMessages = {"Test message one", "Test message two"};

        fileLogger.log(testMessages[0]);
        fileLogger.log(testMessages[1]);

        List<String> messagesLog = fileLogger.getLog();
        for (int i = 0; i < messagesLog.size(); i++) {
            assertTrue(messagesLog.get(i).matches(".*" + testMessages[i] + "$"));
        }
    }

    @Test
    public void canCreateLoggerWithEmptyFileName() {
        assertNotNull(new FileLogger("test"));
    }

    @Test
    public void canWriteLogMessageWhenEmptyFileName() {
        fileLogger = new FileLogger("test");
        String testMessage = "Test message";

        fileLogger.log(testMessage);
        List<String> messages = fileLogger.getLog();

        assertEquals(1, messages.size());
    }

    private static final String FILELOGNAME = "./FileLoggerTests.log";
    private FileLogger fileLogger;
}
