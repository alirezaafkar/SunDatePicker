package com.alirezaafkar.sundatepicker.interfaces;

import android.support.annotation.Nullable;

import java.util.Calendar;

/**
 * Created by Alireza Afkar on 2/5/16 AD.
 */
public interface DateSetListener {
    void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year);
}
