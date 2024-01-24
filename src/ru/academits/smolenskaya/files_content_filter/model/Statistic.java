package ru.academits.smolenskaya.files_content_filter.model;

public interface Statistic {
    void addData(String data);

    String getStatistic();
}