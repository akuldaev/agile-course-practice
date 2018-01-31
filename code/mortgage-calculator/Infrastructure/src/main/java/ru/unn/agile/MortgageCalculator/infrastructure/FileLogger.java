package ru.unn.agile.MortgageCalculator.infrastructure;

import ru.unn.agile.MortgageCalculator.viewmodel.ILogger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FileLogger implements ILogger {
    public FileLogger(final String fileLog) {
        this.fileLog = fileLog;

        BufferedWriter logWriter = null;
        try {
            logWriter = new BufferedWriter(new FileWriter(fileLog));
        } catch (Exception e) {
            e.printStackTrace();
        }
        writer = logWriter;
    }

    @Override
    public List<String> getLog() {
        ArrayList<String> loger = new ArrayList<String>();
        try {
            BufferedReader bufferedReader = getReader();
            String row = bufferedReader.readLine();

            while (row != null) {
                loger.add(row);
                row = bufferedReader.readLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return loger;
    }

    @Override
    public void log(final String logMessager) {
        try {
            writer.write(now() + " > " + logMessager);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static String now() {
        SimpleDateFormat dataFormatter = new SimpleDateFormat(DATE_FORMAT_NOW, Locale.ENGLISH);
        return dataFormatter.format(Calendar.getInstance().getTime());
    }

    private BufferedReader getReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(fileLog));
    }

    private static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    private final String fileLog;
    private final BufferedWriter writer;
}
