package ru.academits.smolenskaya.files_contents_filter.model;

public class StringsStatistic implements Statistic<String> {
    private int minimalStringLength;
    private int maximalStringLength;

    @Override
    public void addData(String data) {
        int stringLength = data.length();

        if (minimalStringLength == 0) {
            minimalStringLength = stringLength;
        }

        if (stringLength < minimalStringLength) {
            minimalStringLength = stringLength;
        } else if (stringLength > maximalStringLength) {
            maximalStringLength = stringLength;
        }
    }

    @Override
    public String getStatistic() {
        return "minimal string length = " + minimalStringLength + System.lineSeparator() +
                "maximal string length = " + maximalStringLength;
    }
}