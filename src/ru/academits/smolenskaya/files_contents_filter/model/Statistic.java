package ru.academits.smolenskaya.files_contents_filter.model;

public interface Statistic<T> {
    void addData(T data);

    String getStatistic();
}