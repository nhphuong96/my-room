package com.myroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.application.BaseApplication;
import com.myroom.core.NumberFormatter;
import com.myroom.database.dao.Payment;
import com.myroom.database.repository.PaymentRepository;
import com.myroom.exception.OperationException;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.PaymentHistoryViewHolder> {

    private Context context;
    private long roomId;
    private List<Payment> paymentList;

    @Inject
    public PaymentRepository paymentRepository;

    public PaymentHistoryAdapter(Context context, long roomId) {
        this.context = context;
        this.roomId = roomId;
        BaseApplication.getRepositoryComponent(context).inject(this);
        loadAllPaymentHistory();
    }

    private void loadAllPaymentHistory() {
        try {
            paymentList = paymentRepository.findPaymentByRoomId(roomId);
        } catch (OperationException e) {
            Toast.makeText(context, "Lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public PaymentHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_payment_history, viewGroup, false);
        PaymentHistoryViewHolder paymentHistoryViewHolder = new PaymentHistoryViewHolder(view);
        return paymentHistoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHistoryViewHolder paymentHistoryViewHolder, int i) {
        Payment payment = paymentList.get(i);
        paymentHistoryViewHolder.tvTotalFee.setText(calculateTotalFee(payment));
        paymentHistoryViewHolder.tvPaymentDate.setText(new SimpleDateFormat("dd/MM/YYYY hh:mm:ss").format(payment.getPaymentDate()));
    }

    private String calculateTotalFee(Payment payment) {
        Double total = 0D;
        total += Double.valueOf(payment.getElectricityFee());
        total += Double.valueOf(payment.getWaterFee());
        total += Double.valueOf(payment.getCabFee());
        total += Double.valueOf(payment.getInternetFee());
        total += Double.valueOf(payment.getRoomFee());
        return NumberFormatter.formatThousandNumberSeparator(String.valueOf(total));
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public class PaymentHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTotalFee;
        private TextView tvPaymentDate;

        public PaymentHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTotalFee = itemView.findViewById(R.id.item_payment_history_payment_fee);
            tvPaymentDate = itemView.findViewById(R.id.item_payment_history_payment_date);
        }
    }
}
