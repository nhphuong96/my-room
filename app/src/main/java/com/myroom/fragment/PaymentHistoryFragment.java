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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.adapter.PaymentHistoryAdapter;
import com.myroom.application.BaseApplication;
import com.myroom.core.Constant;
import com.myroom.database.dao.Payment;
import com.myroom.database.repository.PaymentRepository;
import com.myroom.exception.OperationException;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PaymentHistoryFragment extends Fragment {

    private Context parentContext;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private LinearLayout emptyView;

    @Inject
    public PaymentRepository paymentRepository;

    public static PaymentHistoryFragment newInstance(long roomId) {
        Bundle args = new Bundle();
        args.putLong(Constant.ROOM_KEY_NAME, roomId);
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
        emptyView = view.findViewById(R.id.payment_history_empty_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BaseApplication.getRepositoryComponent(parentContext).inject(this);
        List<Payment> paymentList = loadAllPaymentHistory();
        if (CollectionUtils.isEmpty(paymentList)) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            setupPaymentList(paymentList);
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

    }

    private void setupPaymentList(List<Payment> paymentList) {
        layoutManager = new LinearLayoutManager(parentContext);
        adapter = new PaymentHistoryAdapter(parentContext, paymentList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(parentContext, DividerItemDecoration.HORIZONTAL));
    }

    private List<Payment> loadAllPaymentHistory() {
        try {
            return paymentRepository.findPaymentByRoomId(getArguments().getLong(Constant.ROOM_KEY_NAME));
        } catch (OperationException e) {
            Toast.makeText(parentContext, "Lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return new ArrayList<>();
    }
}
