package ru.academits.smolenskaya.files_contents_filter.model;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FilesContentFilter {
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

        floatsFileName = Paths.get(outputFilesPath,  outputFilesPrefix + floatsFileName).toString();
        integersFileName = Paths.get(outputFilesPath, outputFilesPrefix + integersFileName).toString();
        stringsFileName = Paths.get(outputFilesPath, outputFilesPrefix + stringsFileName).toString();
    }

    public void filterFiles(ArrayList<String> inputFilesNames) {
        ArrayList<String> floats = new ArrayList<>();
        ArrayList<String> integers = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();

        for (String fileName : inputFilesNames) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
                String inputLine;

                while ((inputLine = bufferedReader.readLine()) != null) {
                    inputLine = inputLine.trim();

                    switch (getDataType(inputLine)) {
                        case FLOAT -> floats.add(inputLine);
                        case INTEGER -> integers.add(inputLine);
                        case STRING -> strings.add(inputLine);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Input file " + fileName + " was not found");
            } catch (IOException e) {
                System.out.println("An exception occurred while reading file " + fileName + ": " + e.getMessage());
            }
        }

        if (floats.isEmpty() && integers.isEmpty() && strings.isEmpty()) {
            System.out.println("No files were created: there are no any data to filter");

            return;
        }

        if (isStatisticFull) {
            writeDataToFiles(floatsFileName, floats, integersFileName, integers, stringsFileName, strings);
        } else {
            writeDataToFile(floatsFileName, floats);
            writeDataToFile(integersFileName, integers);
            writeDataToFile(stringsFileName, strings);
        }
    }

    private void writeDataToFile(String fileName, ArrayList<String> strings) {
        if (strings == null || strings.isEmpty()) {
            return;
        }

        try (PrintWriter printWriter = new PrintWriter(new java.io.FileWriter(fileName, isNeedToAppendOutputFiles))) {
            for (String string : strings) {
                printWriter.println(string);
            }

            System.out.println("File " + fileName + ": " + System.lineSeparator() + "lines count = " + strings.size());
        } catch (IOException e) {
            System.out.println("An exception occurred while trying to write the file " + fileName + ": " + e.getMessage());
        }
    }

    private void writeDataToFiles(String floatsFileName, ArrayList<String> floats,
                                  String integersFileName, ArrayList<String> integers,
                                  String stringsFileName, ArrayList<String> strings) {
        if (floats != null && !floats.isEmpty()) {
            try (PrintWriter printWriter = new PrintWriter(new java.io.FileWriter(floatsFileName, isNeedToAppendOutputFiles))) {
                NumbersStatistic<Double> statistic = new NumbersStatistic<>();

                for (String string : floats) {
                    printWriter.println(string);

                    statistic.addData(Double.parseDouble(string));
                }

                System.out.println("File " + floatsFileName + ": " + System.lineSeparator() + "lines count = " + strings.size());
                System.out.println(statistic.getStatistic());
            } catch (IOException e) {
                System.out.println("An exception occurred while trying to write the file " + floatsFileName + ": " + e.getMessage());
            }
        }

        if (integers != null && !integers.isEmpty()) {
            try (PrintWriter printWriter = new PrintWriter(new java.io.FileWriter(integersFileName, isNeedToAppendOutputFiles))) {
                NumbersStatistic<BigInteger> statistic = new NumbersStatistic<>();

                for (String string : integers) {
                    printWriter.println(string);

                    statistic.addData(new BigInteger(string));
                }

                System.out.println("File " + integersFileName + ": " + System.lineSeparator() + "lines count = " + strings.size());
                System.out.println(statistic.getStatistic());
            } catch (IOException e) {
                System.out.println("An exception occurred while trying to write the file " + integersFileName + ": " + e.getMessage());
            }
        }

        if (strings != null && !strings.isEmpty()) {
            try (PrintWriter printWriter = new PrintWriter(new java.io.FileWriter(stringsFileName, isNeedToAppendOutputFiles))) {
                StringsStatistic statistic = new StringsStatistic();

                for (String string : strings) {
                    printWriter.println(string);

                    statistic.addData(string);
                }

                System.out.println("File " + stringsFileName + ": " + System.lineSeparator() + "lines count = " + strings.size());
                System.out.println(statistic.getStatistic());
            } catch (IOException e) {
                System.out.println("An exception occurred while trying to write the file " + stringsFileName + ": " + e.getMessage());
            }
        }
    }

    private static DataType getDataType(String string) {
        char labeledDecimalSymbol = '.';

        if (NumberUtils.isCreatable(string)) {
            if (string.indexOf(labeledDecimalSymbol) < 0) {
                return DataType.INTEGER;
            } else return DataType.FLOAT;
        }

        return DataType.STRING;
    }
}
