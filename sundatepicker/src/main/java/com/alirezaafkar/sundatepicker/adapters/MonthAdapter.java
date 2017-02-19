package com.alirezaafkar.sundatepicker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alirezaafkar.sundatepicker.R;
import com.alirezaafkar.sundatepicker.components.JDF;
import com.alirezaafkar.sundatepicker.components.SquareTextView;
import com.alirezaafkar.sundatepicker.interfaces.DateInterface;

import java.text.ParseException;

/**
 * Created by Alireza Afkar on 2/11/16 AD.
 */
public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.ViewHolder> {
    private static final int TYPE_DAY = 0;
    private static final int TYPE_TITLE = 1;
    private static final int TYPE_NONE = 2;

    private int mYear;
    private int mCurrentYear;
    private int mMonth;
    private JDF mToday;
    private int mStartDay;
    private int mMaxMonth;
    private DateInterface mCallback;
    private View.OnClickListener mOnClickListener;

    public MonthAdapter(DateInterface callback, View.OnClickListener onClickListener,
                        int currentMonth, int maxMonth,int chosenYear) {
        mMaxMonth = maxMonth;
        mCallback = callback;
        mYear=chosenYear;
        mMonth = currentMonth;
        mOnClickListener = onClickListener;

        mToday = new JDF();
        try {
            mStartDay = new JDF().getIranianDay(mYear, mMonth + 1, 1);
            mCurrentYear=mToday.getIranianYear();
        } catch (ParseException ignored) {
        }
    }

    private boolean isSelected(int day) {
        return mCallback.getMonth() == mMonth + 1 &&
                mCallback.getDay() == day + 1 &&
            mCallback.getYear()==mYear;
    }

    private boolean isToday(int day) {
        return (mMonth + 1 == mToday.getIranianMonth()
                && day + 1 == mToday.getIranianDay()
                && mYear == mToday.getIranianYear());
    }

    @Override
    public MonthAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MonthAdapter.ViewHolder holder, int position) {
        String text = null;
        boolean selected = false;
        boolean clickable = false;

        int viewType = getItemViewType(position);

        if (viewType == TYPE_TITLE) {
            clickable = false;
            selected = false;
            text = mCallback.getWeekDays()[position].substring(0, 1);
        } else if (viewType == TYPE_DAY) {
            clickable = true;
            position = getDayIndex(position);
            selected = isSelected(position);
            text = String.valueOf(position + 1);
        }

        holder.mTextView.setChecked(isToday(position));
        holder.mTextView.setClickable(clickable);
        holder.mTextView.setSelected(selected);
        holder.mTextView.setText(text);
    }

    private int getDayIndex(int position) {
        return position - mStartDay - 7;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && position < 7) {
            return TYPE_TITLE;
        } else if (position - 7 >= mStartDay) {
            return TYPE_DAY;
        } else {
            return TYPE_NONE;
        }
    }

    @Override
    public int getItemCount() {
        int days = 30;
        if (mMonth < 6)
            days = 31;
        if (mMonth == 11 && !JDF.isLeapYear(mYear))
            days = 29;

        if (mMaxMonth == mMonth + 1 &&mYear==mCurrentYear)
            days = mToday.getIranianDay();

        return days + 7 + mStartDay;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SquareTextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (SquareTextView) itemView;
            mTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getDayIndex(getLayoutPosition());
            if (mCallback != null && position >= 0) {
                int oldMonth = mCallback.getMonth();
                mCallback.setDay(position + 1, mMonth + 1,mYear);
                if (oldMonth != mMonth + 1) {
                    mOnClickListener.onClick(view);
                } else {
                    notifyDataSetChanged();
                }
            }
        }
    }
}
