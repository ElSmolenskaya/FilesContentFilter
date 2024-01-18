package ru.academits.smolenskaya.files_contents_filter.model;

import java.text.DecimalFormat;

public class NumbersStatistic<T extends Number & Comparable<T>> implements Statistic<T> {
    private T minimalNumber;
    private T maximalNumber;
    private double numbersSum;
    private int numbersCount;

    @Override
    public String getStatistic() {
        DecimalFormat decimalFormat = new DecimalFormat("0.#");

        return String.format("minimal number = " + minimalNumber + System.lineSeparator() +
                "maximal number = " + maximalNumber + System.lineSeparator() +
                "all numbers sum = " + decimalFormat.format(numbersSum) + System.lineSeparator() +
                "average number = " + getAverageNumber());
    }

    private double getAverageNumber() {
        return numbersSum / numbersCount;
    }

    @Override
    public void addData(T data) {
        if (minimalNumber == null){
            minimalNumber = data;
        }

        if (maximalNumber == null){
            maximalNumber = data;
        }

        if (data.compareTo(minimalNumber) < 0) {
            minimalNumber = data;
        } else if (data.compareTo(maximalNumber) > 0) {
            maximalNumber = data;
        }

        numbersSum += data.doubleValue();
        numbersCount++;
    }
}