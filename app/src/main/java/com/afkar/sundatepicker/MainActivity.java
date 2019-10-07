package com.afkar.sundatepicker;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.DateItem;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;

import java.util.Calendar;
import java.util.Locale;

/*
 * Created by Alireza Afkar on 2/11/16 AD.
 */

public class MainActivity extends AppCompatActivity implements
        OnClickListener, DateSetListener {
    private Button mEnd;
    private Button mStart;
    private CheckBox mFuture;
    private CheckBox mPast;

    private Date mEndDate;
    private Date mStartDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale("fa");

        setContentView(R.layout.activity_main);

        mEnd = findViewById(R.id.endDate);
        mStart = findViewById(R.id.startDate);
        mFuture = findViewById(R.id.future);
        mPast = findViewById(R.id.past);

        mEndDate = new Date();
        mStartDate = new Date();

        mEnd.setOnClickListener(this);
        mStart.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
            ShareCompat.IntentBuilder.from(this)
                    .setType("message/rfc822")
                    .addEmailTo("pesiran@gmail.com")
                    .setSubject("SunDatePicker")
                    .startChooser();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId() == R.id.startDate ? 1 : 2;
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        if (mFuture.isChecked()) {
            maxDate.set(Calendar.YEAR, maxDate.get(Calendar.YEAR) + 10);
        }
        if (mPast.isChecked()) {
            minDate.set(Calendar.YEAR, minDate.get(Calendar.YEAR) - 10);
        }

        DatePicker.Builder builder = new DatePicker.Builder()
                .id(id)
                .minDate(minDate)
                .maxDate(maxDate)
                .setRetainInstance(true);

        if (v.getId() == R.id.startDate)
            builder.date(mStartDate.getDay(), mStartDate.getMonth(), mStartDate.getYear());
        else
            builder.date(mEndDate.getCalendar());

        builder.build(MainActivity.this)
                .show(getSupportFragmentManager(), "");
    }

    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        if (id == 1) {
            mStartDate.setDate(day, month, year);
            mStart.setText(mStartDate.getDate());
        } else {
            mEndDate.setDate(day, month, year);
            mEnd.setText(mEndDate.getDate());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale("fa");
    }

    public void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    class Date extends DateItem {
        String getDate() {
            Calendar calendar = getCalendar();
            return String.format(Locale.US,
                    "%d/%d/%d (%d/%d/%d)",
                    getYear(), getMonth(), getDay(),
                    calendar.get(Calendar.YEAR),
                    +calendar.get(Calendar.MONTH) + 1,
                    +calendar.get(Calendar.DAY_OF_MONTH));
        }
    }
}
