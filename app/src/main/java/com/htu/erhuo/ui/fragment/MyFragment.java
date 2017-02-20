package com.htu.erhuo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.htu.erhuo.R;
import com.htu.erhuo.ui.adapter.RecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 * Created by yzw on 2017/2/19.
 */

public class MyFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, view);
        initUIAndData();
        return view;
    }

    private void initUIAndData() {
        RecyclerAdapter adapter = new RecyclerAdapter(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }
}
