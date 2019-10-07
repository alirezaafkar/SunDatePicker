package com.alirezaafkar.sundatepicker;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
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

/**
 * Created by Alireza Afkar on 2/5/16 AD.
 */
@SuppressWarnings("NewInstance")
public class DatePicker extends DialogFragment
        implements OnClickListener, DateInterface {
    private TextView mDate;
    private TextView mYear;
    private TextView mToday;

    private Builder mBuilder;
    private String[] mMonths;
    private DateItem mDateItem;
    private String[] mWeekDays;
    private DateSetListener mCallBack;
    private JDF mTodayDate = new JDF();

    public DatePicker() {
    }

    public static class Builder {
        @StyleRes
        private int theme;

        private int id;
        private DateItem dateItem;
        private boolean retainInstance;

        public Builder() {
            dateItem = new DateItem();
            theme = R.style.DialogTheme;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder minDate(int year, int month, int day) {
            dateItem.setMinDate(new JDF(year, month, day));
            return this;
        }

        public Builder maxDate(int year, int month, int day) {
            dateItem.setMaxDate(new JDF(year, month, day));
            return this;
        }

        public Builder minDate(Calendar calendar) {
            dateItem.setMinDate(new JDF(calendar));
            return this;
        }

        public Builder maxDate(Calendar calendar) {
            dateItem.setMaxDate(new JDF(calendar));
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

        public Builder setRetainInstance(boolean retainInstance) {
            this.retainInstance = retainInstance;
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
        setRetainInstance(mBuilder.retainInstance);
        setStyle(DialogFragment.STYLE_NO_TITLE, mBuilder.theme);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
            params.height = getResources().getDimensionPixelSize(R.dimen.dialog_height);
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mDateItem.shouldShowYearFirst()) {
            mYear.performClick();
        } else {
            mDate.performClick();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = layoutInflater.inflate(R.layout.dialog_main, container, false);

        mYear = view.findViewById(R.id.year);
        mDate = view.findViewById(R.id.date);
        mToday = view.findViewById(R.id.today);

        mYear.setOnClickListener(this);
        mDate.setOnClickListener(this);
        mToday.setOnClickListener(this);
        view.findViewById(R.id.done).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.year) {
            showYears();
        } else if (v.getId() == R.id.date) {
            showMonths();
        } else if (v.getId() == R.id.today) {
            mDateItem.setDate(new JDF());
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
        switchFragment(MonthFragment.newInstance(DatePicker.this));
    }

    private void showYears() {
        mYear.setSelected(true);
        mDate.setSelected(false);
        switchFragment(YearFragment.newInstance(DatePicker.this));
    }

    private void onDone() {
        mCallBack.onDateSet(mBuilder.id, mDateItem.getCalendar(),
                mDateItem.getDay(), mDateItem.getMonth(), mDateItem.getYear());
    }

    void switchFragment(Fragment fragment) {
        fragment.setRetainInstance(this.getRetainInstance());
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
        updateDisplay();
    }

    public void updateDisplay() {
        mToday.setVisibility(isToday() ? View.GONE : View.VISIBLE);
        mYear.setText(String.valueOf(mDateItem.getYear()));
        mDate.setText(getString(R.string.date_placeholder,
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
     * @param year  Iranian year
     */
    @Override
    public void setDay(int day, int month, int year) {
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
        if (!JDF.isLeapYear(year) && mDateItem.getMonth() == 12 && mDateItem.getDay() == 30) {
            mDateItem.setDay(29);
        }
        updateDisplay();
        if (mDateItem.shouldCloseYearAutomatically())
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
     * @return returns current year according to user device time
     */
    @Override
    public int getCurrentYear() {
        return mDateItem.getCurrentYear();
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

    private Boolean isToday() {
        return mDateItem.getYear() == mTodayDate.getIranianYear() &&
                mDateItem.getMonth() == mTodayDate.getIranianMonth() &&
                mDateItem.getDay() == mTodayDate.getIranianDay();
    }

    @Override
    public DateItem getDateItem() {
        return mDateItem;
    }
}
