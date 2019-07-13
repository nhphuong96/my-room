package com.myroom.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myroom.R;
import com.myroom.adapter.PaymentHistoryAdapter;

public class PaymentHistoryFragment extends Fragment {

    private Context parentContext;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    public PaymentHistoryFragment newInstance(long roomId) {
        Bundle args = new Bundle();
        args.putLong("roomId", roomId);
        PaymentHistoryFragment fragment = new PaymentHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentContext = container.getContext();
        View view = inflater.inflate(R.layout.fragment_payment_history, container, false);
        recyclerView = view.findViewById(R.id.payment_history_list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadAllPaymentHistory();
    }

    private void loadAllPaymentHistory() {
        layoutManager = new LinearLayoutManager(parentContext);
        adapter = new PaymentHistoryAdapter(parentContext, getArguments().getLong("roomId"));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(parentContext, DividerItemDecoration.HORIZONTAL));
    }
}
