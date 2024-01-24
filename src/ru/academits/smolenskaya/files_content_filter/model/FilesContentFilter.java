package ru.academits.smolenskaya.files_content_filter.model;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FilesContentFilter {
    private static final char LABELED_DECIMAL_SYMBOL = '.';
    private String floatsFileName = "floats.txt";
    private String integersFileName = "integers.txt";
    private String stringsFileName = "strings.txt";
    private final boolean isNeedToAppendOutputFiles;
    private final boolean isStatisticFull;

    public enum DataType {
        FLOAT, INTEGER, STRING
    }

    public FilesContentFilter(String outputFilesPrefix, String outputFilesPath, boolean isNeedToAppendOutputFiles, boolean isStatisticFull) {
        this.isNeedToAppendOutputFiles = isNeedToAppendOutputFiles;
        this.isStatisticFull = isStatisticFull;

        floatsFileName = Paths.get(outputFilesPath, outputFilesPrefix + floatsFileName).toString();
        integersFileName = Paths.get(outputFilesPath, outputFilesPrefix + integersFileName).toString();
        stringsFileName = Paths.get(outputFilesPath, outputFilesPrefix + stringsFileName).toString();
    }

    public void filterFiles(ArrayList<String> inputFilesNames) {
        ArrayList<String> floats = new ArrayList<>();
        ArrayList<String> integers = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();

        ArrayList<Thread> fileReadersThreads = new ArrayList<>();
        final Object lock = new Object();

        for (String fileName : inputFilesNames) {
            Thread fileReaderThread = new Thread(() -> {
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
                    String inputLine;

                    while ((inputLine = bufferedReader.readLine()) != null) {
                        inputLine = inputLine.trim();

                        if (!inputLine.isEmpty()) {
                            synchronized (lock) {
                                switch (getDataType(inputLine)) {
                                    case FLOAT -> floats.add(inputLine);
                                    case INTEGER -> integers.add(inputLine);
                                    case STRING -> strings.add(inputLine);
                                }
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Input file " + fileName + " was not found");
                } catch (IOException e) {
                    System.out.println("An exception occurred while reading file " + fileName + ": " + e.getMessage());
                }
            });

            fileReadersThreads.add(fileReaderThread);
            fileReaderThread.start();
        }

        for (Thread thread : fileReadersThreads) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        }

        if (floats.isEmpty() && integers.isEmpty() && strings.isEmpty()) {
            System.out.println("No files were created: there are no any data to filter");

            return;
        }

        writeDataToFile(floatsFileName, floats, isStatisticFull, new NumbersStatistic());
        writeDataToFile(integersFileName, integers, isStatisticFull, new NumbersStatistic());
        writeDataToFile(stringsFileName, strings, isStatisticFull, new StringsStatistic());
    }

    private void writeDataToFile(String fileName, ArrayList<String> strings, boolean isStatisticFull, Statistic statistic) {
        if (strings != null && !strings.isEmpty()) {
            try (PrintWriter printWriter = new PrintWriter(new java.io.FileWriter(fileName, isNeedToAppendOutputFiles))) {
                for (String string : strings) {
                    printWriter.println(string);

                    if (isStatisticFull) {
                        statistic.addData(string);
                    }
                }

                System.out.println("---------- " + fileName + ": " + System.lineSeparator() + "lines count = " + strings.size());

                if (isStatisticFull) {
                    System.out.println(statistic.getStatistic());
                }
            } catch (IOException e) {
                System.out.println("An exception occurred while trying to write the file " + fileName + ": " + e.getMessage());
            }
        }
    }

    private static DataType getDataType(String string) {
        if (NumberUtils.isCreatable(string)) {
            String upperCaseString = string.toUpperCase();

            final String negativePowerDesignation = "E-";
            final String positivePowerDesignation = "E+";

            if (upperCaseString.indexOf(negativePowerDesignation) > 0 || upperCaseString.indexOf(positivePowerDesignation) > 0) {
                return DataType.FLOAT;
            }
        }

        if (NumberUtils.isParsable(string)) {
            return string.indexOf(LABELED_DECIMAL_SYMBOL) < 0 ? DataType.INTEGER : DataType.FLOAT;
        }

        return DataType.STRING;
    }
}