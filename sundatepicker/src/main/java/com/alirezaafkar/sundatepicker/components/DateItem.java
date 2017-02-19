package com.alirezaafkar.sundatepicker.components;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by Alireza Afkar on 2/5/16 AD.
 */
public class DateItem {
    private int day;
    private int year;
    private int month;
    private int maxYear;
    private int minYear;
    private int maxMonth;
    private boolean futureDisabled;
    private boolean showYearFirst;
    private boolean closeYearAutomatically;

    public DateItem() {
        setDate(new JDF());
    }

    public DateItem(int day, int month, int year) {
        setDate(day, month, year);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMaxMonth() {
        return maxMonth;
    }

    public void setMaxMonth(int maxMonth) {
        this.maxMonth = maxMonth;
    }

    public int getMinYear() {
        return minYear;
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    public int getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    public boolean isFutureDisabled() {
        return futureDisabled;
    }

    public void setFutureDisabled(boolean futureDisabled) {
        this.futureDisabled = futureDisabled;
    }


    public boolean shouldShowYearFirst() {
        return showYearFirst;
    }

    public void setShowYearFirst(boolean showYearFirst) {
        this.showYearFirst = showYearFirst;
    }


    public boolean shouldCloseYearAutomatically() {
        return closeYearAutomatically;
    }

    public void setCloseYearAutomatically(boolean closeYearAutomatically) {
        this.closeYearAutomatically = closeYearAutomatically;
    }

    public void setDate(JDF jdf) {
        setDate(jdf.getIranianDay(), jdf.getIranianMonth(), jdf.getIranianYear());
    }

    public void setDate(int day, int month, int year) {
        this.day = day;
        this.year = year;
        this.month = month;

        if (minYear == 0) {
            minYear = new JDF().getIranianYear();
            maxYear = minYear + 10;
        }
    }

    public int getIranianDay() {
        try {
            return new JDF().getIranianDay(year, month, day);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Calendar getCalendar() {
        JDF jdf = new JDF();
        jdf.setIranianDate(year, month, day);
        Calendar calendar = Calendar.getInstance();
        calendar.set(jdf.getGregorianYear(),
                jdf.getGregorianMonth()-1,
                jdf.getGregorianDay());
        return calendar;
    }
}
