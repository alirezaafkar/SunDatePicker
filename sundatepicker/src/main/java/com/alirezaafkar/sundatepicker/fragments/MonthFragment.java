package com.alirezaafkar.sundatepicker.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alirezaafkar.sundatepicker.R;
import com.alirezaafkar.sundatepicker.adapters.MonthAdapter;
import com.alirezaafkar.sundatepicker.components.JDF;
import com.alirezaafkar.sundatepicker.interfaces.DateInterface;

/**
 * Created by Alireza Afkar on 2/5/16 AD.
 */
public class MonthFragment extends Fragment implements View.OnClickListener {
    private int mMaxMonth;
    private TextView mTitle;
    private ViewPager mPager;
    private PagerAdapter mAdapter;
    private DateInterface mCallback;

    public static MonthFragment newInstance(DateInterface callback, int maxMonth) {
        MonthFragment fragment = new MonthFragment();
        fragment.mCallback = callback;
        fragment.mMaxMonth = maxMonth;
        return fragment;
    }

    public MonthFragment() {
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return LayoutInflater.from(container.getContext())
                .inflate(R.layout.fragment_month, container, false);
    }

    @Override
    public void onClick(View view) {
        int currentItem = mPager.getCurrentItem();
        if (view.getId() == R.id.next) {
            if (++currentItem < mAdapter.getCount()) {
                mPager.setCurrentItem(currentItem, true);
            }
        } else if (view.getId() == R.id.before) {
            if (--currentItem < mAdapter.getCount()) {
                mPager.setCurrentItem(currentItem, true);
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mTitle = (TextView) view.findViewById(R.id.title);

        mAdapter = new PagerAdapter();
        mPager.setAdapter(mAdapter);

        view.findViewById(R.id.next).setOnClickListener(this);
        view.findViewById(R.id.before).setOnClickListener(this);

        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int month) {
                super.onPageSelected(month);

                mTitle.setText(String.format("%s %d",
                        mAdapter.getPageTitle(month),
                        mCallback.getYear()));
            }
        });

        mPager.setCurrentItem(mCallback.getMonth() - 1);
    }


    private class PagerAdapter extends android.support.v4.view.PagerAdapter implements View.OnClickListener {
        private int mCurrentYear;

        public PagerAdapter() {
            mCurrentYear = new JDF().getIranianYear();
        }

        @Override
        public int getCount() {
            if (mMaxMonth > 0 && mCurrentYear == mCallback.getYear())
                return mMaxMonth;
            else
                return mCallback.getMonths().length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mCallback.getMonths()[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int month) {
            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.layout_recycler_view, container, false);
            RecyclerView recyclerView = (RecyclerView) view;
            MonthAdapter adapter = new MonthAdapter(mCallback, this, month, mMaxMonth);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void onClick(View view) {
            notifyDataSetChanged();
        }
    }

}
