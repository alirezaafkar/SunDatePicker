package com.alirezaafkar.sundatepicker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.alirezaafkar.sundatepicker.components.DateItem;
import com.alirezaafkar.sundatepicker.components.JDF;
import com.alirezaafkar.sundatepicker.fragments.MonthFragment;
import com.alirezaafkar.sundatepicker.fragments.YearFragment;
import com.alirezaafkar.sundatepicker.interfaces.DateInterface;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Alireza Afkar on 2/5/16 AD.
 */
@SuppressWarnings("NewInstance")
public class DatePicker extends DialogFragment
    implements OnClickListener, DateInterface {
    private TextView mDate;
    private TextView mYear;

    private Builder mBuilder;
    private String[] mMonths;
    private DateItem mDateItem;
    private String[] mWeekDays;
    private DateSetListener mCallBack;

    public DatePicker() {
    }

    public static class Builder {
        @StyleRes
        private int theme;

        private int id;
        private DateItem dateItem;

        public Builder() {
            dateItem = new DateItem();
            theme = R.style.DialogTheme;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder minYear(int minYear) {
            dateItem.setMinYear(minYear);
            return this;
        }

        public Builder maxYear(int maxYear) {
            dateItem.setMaxYear(maxYear);
            return this;
        }

        public Builder maxMonth(int maxMonth) {
            dateItem.setMaxMonth(maxMonth);
            return this;
        }

        public Builder date(JDF jdf) {
            this.dateItem.setDate(jdf);
            return this;
        }

        /**
         * @param day   Iranian day
         * @param month Iranian month
         * @param year  Iranian year
         */
        public Builder date(int day, int month, int year) {
            this.dateItem.setDate(day, month, year);
            return this;
        }

        public Builder date(GregorianCalendar calendar) {
            this.dateItem.setDate(new JDF(calendar));
            return this;
        }

        public Builder date(Calendar calendar) {
            this.dateItem.setDate(new JDF(calendar));
            return this;
        }

        /**
         * @param future false means max date is today
         */
        public Builder future(boolean future) {
            dateItem.setFutureDisabled(!future);
            return this;
        }

        /**
         * @param shouldShowYearFirst true means show year fragment first
         */
        public Builder showYearFirst(boolean shouldShowYearFirst) {
            dateItem.setShowYearFirst(shouldShowYearFirst);
            return this;
        }

        /**
         * @param shouldCloseYearAutomatically true means show month fragment automatically after choosing a year
         */
        public Builder closeYearAutomatically(boolean shouldCloseYearAutomatically) {
            dateItem.setCloseYearAutomatically(shouldCloseYearAutomatically);
            return this;
        }

        public Builder theme(@StyleRes int theme) {
            this.theme = theme;
            return this;
        }

        public DatePicker build(DateSetListener callback) {
            DatePicker datePicker = new DatePicker();
            datePicker.mCallBack = callback;
            datePicker.mDateItem = dateItem;
            datePicker.mBuilder = this;
            return datePicker;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NO_TITLE, mBuilder.theme);
    }

    @Override
    public void onStart() {
        super.onStart();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
        params.height = getResources().getDimensionPixelSize(R.dimen.dialog_height);
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkFuture();
        if(mDateItem.shouldShowYearFirst())
            mYear.performClick();
        else
            mDate.performClick();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater,
        ViewGroup container, Bundle savedInstanceState) {

        View view = layoutInflater.inflate(R.layout.dialog_main, container, false);

        mYear = (TextView) view.findViewById(R.id.year);
        mDate = (TextView) view.findViewById(R.id.date);

        view.findViewById(R.id.done).setOnClickListener(this);
        view.findViewById(R.id.year).setOnClickListener(this);
        view.findViewById(R.id.date).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);

        return view;
    }

    private void checkFuture() {
        if (!mDateItem.isFutureDisabled()) {
            mDateItem.setMaxMonth(0);
            return;
        }

        JDF jdf = new JDF();
        mDateItem.setMaxMonth(jdf.getIranianMonth());
        mDateItem.setMaxYear(jdf.getIranianYear());

        if (mDateItem.getMinYear() > mDateItem.getMaxYear())
            mDateItem.setMaxYear(mDateItem.getMaxYear() - 1);

        if (mDateItem.getMonth() > jdf.getIranianMonth())
            mDateItem.setMonth(jdf.getIranianMonth());
        if (mDateItem.getDay() > jdf.getIranianDay())
            mDateItem.setDay(jdf.getIranianDay());
        if (mDateItem.getYear() > jdf.getIranianYear())
            mDateItem.setYear(jdf.getIranianYear());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.year) {
            showYears();
        } else if (v.getId() == R.id.date) {
            showMonths();
        } else if (v.getId() == R.id.done) {
            if (mCallBack != null) {
                onDone();
            }
            dismiss();
        } else if (v.getId() == R.id.cancel) {
            dismiss();
        }
    }

    private void showMonths() {
        mDate.setSelected(true);
        mYear.setSelected(false);
        switchFragment(MonthFragment.newInstance(DatePicker.this,
            mDateItem.getMaxMonth()));
    }

    private void showYears() {
        mYear.setSelected(true);
        mDate.setSelected(false);
        switchFragment(YearFragment.newInstance(DatePicker.this,
            mDateItem.getMinYear(), mDateItem.getMaxYear()));
    }

    private void onDone() {
        mCallBack.onDateSet(mBuilder.id, mDateItem.getCalendar(),
            mDateItem.getDay(), mDateItem.getMonth(), mDateItem.getYear());
    }

    void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in,
            android.R.anim.fade_out);
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        updateDisplay();
    }

    public void updateDisplay() {
        mYear.setText(String.valueOf(mDateItem.getYear()));
        mDate.setText(String.format(Locale.US, "%s ، %d %s",
            getDayName(), mDateItem.getDay(), getMonthName()));
    }

    /**
     * @return Persian month name
     */
    public String getMonthName() {
        return getMonths()[mDateItem.getMonth() - 1];
    }

    public String getDayName() {
        int day = mDateItem.getIranianDay();
        return getWeekDays()[day];
    }

    /**
     * @param day Iranian day
     */
    @Override
    public void setDay(int day) {
        mDateItem.setDay(day);
        updateDisplay();
    }

    /**
     * @param day   Iranian day
     * @param month Iranian month
     * @param year   Iranian year
     */
    @Override
    public void setDay(int day, int month , int year) {
        mDateItem.setDay(day);
        mDateItem.setMonth(month);
        mDateItem.setYear(year);
        updateDisplay();
    }

    /**
     * @param month Iranian month
     */
    @Override
    public void setMonth(int month) {
        mDateItem.setMonth(month);
        updateDisplay();
    }

    /**
     * @param year Iranian year
     */
    @Override
    public void setYear(int year) {
        mDateItem.setYear(year);
        // check if user chosen day is 30 esfand and whether the new chosen year is Kabise or not
        if(!JDF.isLeapYear(year)&&mDateItem.getMonth()==12&&mDateItem.getDay()==30){
            mDateItem.setDay(29);
        }
        updateDisplay();
        if(mDateItem.shouldCloseYearAutomatically())
            showMonths();
    }

    /**
     * @param day   Iranian day
     * @param month Iranian month
     * @param year  Iranian year
     */
    @Override
    public void setDate(int day, int month, int year) {
        mDateItem.setDate(day, month, year);
        updateDisplay();
    }

    /**
     * @return returns Iranian day
     */
    @Override
    public int getDay() {
        return mDateItem.getDay();
    }

    /**
     * @return returns Iranian month
     */
    @Override
    public int getMonth() {
        return mDateItem.getMonth();
    }

    /**
     * @return returns Iranian year
     */
    @Override
    public int getYear() {
        return mDateItem.getYear();
    }

    /**
     * @return returns Persian week days
     */

    @Override
    public String[] getWeekDays() {
        if (mWeekDays == null)
            mWeekDays = getResources().getStringArray(R.array.persian_week_days);
        return mWeekDays;
    }

    /**
     * @return returns Persian months
     */

    @Override
    public String[] getMonths() {
        if (mMonths == null)
            mMonths = getResources().getStringArray(R.array.persian_months);
        return mMonths;
    }
}
