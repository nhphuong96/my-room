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
import com.myroom.utils.DateUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.PaymentHistoryViewHolder> {

    private Context context;
    private List<Payment> paymentList;

    public PaymentHistoryAdapter(Context context, List<Payment> paymentList) {
        this.context = context;
        this.paymentList = paymentList;
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
        paymentHistoryViewHolder.tvPaymentDate.setText(DateUtils.convertDateToStringAsDDMMYYYYHHMMSS(payment.getCreationDate()));
    }

    private String calculateTotalFee(Payment payment) {
        Double total = 0D;
        if (StringUtils.isNotEmpty(payment.getElectricityFee())) {
            total += Double.valueOf(payment.getElectricityFee());
        }
        if (StringUtils.isNotEmpty(payment.getWaterFee())) {
            total += Double.valueOf(payment.getWaterFee());
        }
        if (StringUtils.isNotEmpty(payment.getCabFee())) {
            total += Double.valueOf(payment.getCabFee());
        }
        if (StringUtils.isNotEmpty(payment.getInternetFee())) {
            total += Double.valueOf(payment.getInternetFee());
        }
        if (StringUtils.isNotEmpty(payment.getRoomFee())) {
            total += Double.valueOf(payment.getRoomFee());
        }
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
