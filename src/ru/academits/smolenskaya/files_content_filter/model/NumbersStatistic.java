package ru.academits.smolenskaya.files_content_filter.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class NumbersStatistic implements Statistic {
    private static final int ROUNDING_PRECISION = 20;
    private BigDecimal minimalNumber;
    private BigDecimal maximalNumber;
    private BigDecimal numbersSum;
    private int numbersCount;

    public NumbersStatistic() {
        numbersSum = new BigDecimal(BigInteger.ZERO);
    }

    @Override
    public String getStatistic() {
        return String.format("Minimal number = " + minimalNumber.toPlainString() + System.lineSeparator() +
                "Maximal number = " + maximalNumber.toPlainString() + System.lineSeparator() +
                "All numbers sum = " + numbersSum.toPlainString() + System.lineSeparator() +
                "Average number = " + getAverageNumber().stripTrailingZeros().toPlainString());
    }

    private BigDecimal getAverageNumber() {
        return numbersSum.divide(new BigDecimal(numbersCount), ROUNDING_PRECISION, RoundingMode.HALF_UP);
    }

    @Override
    public void addData(String data) {
        BigDecimal number = new BigDecimal(data);

        if (minimalNumber == null) {
            minimalNumber = number;
        }

        if (maximalNumber == null) {
            maximalNumber = number;
        }

        if (number.compareTo(minimalNumber) < 0) {
            minimalNumber = number;
        } else if (number.compareTo(maximalNumber) > 0) {
            maximalNumber = number;
        }

        numbersSum = numbersSum.add(number);

        numbersCount++;
    }
}