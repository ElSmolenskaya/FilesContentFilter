package ru.academits.smolenskaya.files_content_filter.main;

import ru.academits.smolenskaya.files_content_filter.model.FilesContentFilter;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String outputFilesPrefix = "";
        String outputFilesPath = "";
        boolean isNeedToAppendOutputFiles = false;
        boolean isStatisticFull = false;

        ArrayList<String> inputFilesNames = new ArrayList<>();

        int i = 0;

        while (i < args.length) {
            switch (args[i].toLowerCase()) {
                case "-a" -> isNeedToAppendOutputFiles = true;
                case "-s" -> isStatisticFull = false;
                case "-f" -> isStatisticFull = true;
                case "-p" -> {
                    if (i < args.length - 1) {
                        outputFilesPrefix = args[i + 1];

                        i++;
                    }
                }

                case "-o" -> {
                    if (i < args.length - 1) {
                        outputFilesPath = args[i + 1];

                        i++;
                    }
                }

                default -> inputFilesNames.add(args[i]);
            }

            i++;
        }

        FilesContentFilter filesContentFilter = new FilesContentFilter(outputFilesPrefix, outputFilesPath, isNeedToAppendOutputFiles, isStatisticFull);
        filesContentFilter.filterFiles(inputFilesNames);
    }
}