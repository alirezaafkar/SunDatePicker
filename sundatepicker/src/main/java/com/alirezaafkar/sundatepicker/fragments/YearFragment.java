package com.alirezaafkar.sundatepicker.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alirezaafkar.sundatepicker.interfaces.DateInterface;
import com.alirezaafkar.sundatepicker.R;
import com.alirezaafkar.sundatepicker.adapters.YearAdapter;

/**
 * Created by Alireza Afkar on 2/5/16 AD.
 */
public class YearFragment extends Fragment {
    private int mMinYear;
    private int mMaxYear;
    private DateInterface mCallback;

    public static YearFragment newInstance(DateInterface callback, int minYear, int maxYear) {
        YearFragment fragment = new YearFragment();
        fragment.mCallback = callback;
        fragment.mMinYear = minYear;
        fragment.mMaxYear = maxYear;
        return fragment;
    }

    public YearFragment() {
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return LayoutInflater.from(container.getContext())
                .inflate(R.layout.layout_recycler_view, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        YearAdapter adapter = new YearAdapter(mCallback, getYears());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(adapter.getSelectedYear());
    }

    private int[] getYears() {
        int[] years = new int[(mMaxYear - mMinYear) + 1];
        int counter = 0;
        for (int i = mMinYear; i <= mMaxYear; i++) {
            years[counter++] = i;
        }
        return years;
    }

}
