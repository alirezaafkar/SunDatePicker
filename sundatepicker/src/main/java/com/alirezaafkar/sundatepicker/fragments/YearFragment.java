package com.alirezaafkar.sundatepicker.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    private DateInterface mCallback;

    public static YearFragment newInstance(DateInterface callback) {
        YearFragment fragment = new YearFragment();
        fragment.mCallback = callback;
        return fragment;
    }

    public YearFragment() {
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return LayoutInflater.from(container.getContext())
                .inflate(R.layout.layout_recycler_view, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        YearAdapter adapter = new YearAdapter(mCallback, getYears());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(adapter.getSelectedYear());
    }

    private int[] getYears() {
        int maxYear = mCallback.getDateItem().getMaxDate().getIranianYear();
        int minYear = mCallback.getDateItem().getMinDate().getIranianYear();
        int[] years = new int[(maxYear - minYear) + 1];

        int counter = 0;
        for (int i = minYear; i <= maxYear; i++) {
            years[counter++] = i;
        }
        return years;
    }
}
