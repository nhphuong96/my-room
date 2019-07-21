package com.myroom.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.application.BaseApplication;
import com.myroom.core.CurrencyId;
import com.myroom.core.NumberFormatter;
import com.myroom.core.UtilityId;
import com.myroom.database.dao.Currency;
import com.myroom.database.dao.Payment;
import com.myroom.database.dao.UtilityIndex;
import com.myroom.database.repository.UtilityIndexRepository;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.ICurrencyService;
import com.myroom.service.IUtilityService;
import com.myroom.service.sdo.IndexPair;
import com.myroom.service.sdo.ReadSelectedCurrencyOut;
import com.myroom.utils.DateUtils;
import com.myroom.utils.FormattedTextUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.PaymentHistoryViewHolder> {

    private Context context;
    private List<Payment> paymentList;
    private Currency selectedCurrency;

    @Inject
    public ICurrencyService currencyService;
    @Inject
    public UtilityIndexRepository utilityIndexRepository;
    @Inject
    public IUtilityService utilityService;

    public PaymentHistoryAdapter(Context context, List<Payment> paymentList) {
        this.context = context;
        this.paymentList = paymentList;
        BaseApplication.getServiceComponent(context).inject(this);
        BaseApplication.getRepositoryComponent(context).inject(this);
        loadSelectedCurrency();
    }

    private void loadSelectedCurrency() {
        try {
            ReadSelectedCurrencyOut readSelectedCurrencyOut = currencyService.readSelectedCurrency();
            selectedCurrency = readSelectedCurrencyOut.getCurrency();
        } catch (OperationException e) {
            Toast.makeText(context, "Lỗi xảy ra: không tìm thấy đơn vị tiền tệ.", Toast.LENGTH_SHORT).show();
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
        setupPaymentHistoryData(paymentHistoryViewHolder, payment);
        paymentHistoryViewHolder.tvTotalFee.setText(calculateTotalFee(payment));
        paymentHistoryViewHolder.tvCreationDate.setText(DateUtils.convertDateToStringAsDDMMYYYYHHMMSS(payment.getCreationDate()));
        if (payment.getPaymentDate() != null) {
            paymentHistoryViewHolder.tvPaymentDate.setText(DateUtils.convertDateToStringAsDDMMYYYYHHMMSS(payment.getPaymentDate()));
        }
        else {
            paymentHistoryViewHolder.tvPaymentDate.setText("Chưa thanh toán");
        }

    }

    private void setupPaymentHistoryData(PaymentHistoryViewHolder paymentHistoryViewHolder, Payment payment) {
        try {
            List<UtilityIndex> utilityIndexList = utilityIndexRepository.findUtilityIndexByPaymentKey(payment.getPaymentKey());
            UtilityIndex electricityIndexPair = getElectricityIndexPair(utilityIndexList);
            paymentHistoryViewHolder.electricityFee = payment.getElectricityFee();
            paymentHistoryViewHolder.waterFee = payment.getWaterFee();
            paymentHistoryViewHolder.cabFee = payment.getCabFee();
            paymentHistoryViewHolder.internetFee = payment.getInternetFee();
            paymentHistoryViewHolder.roomFee = payment.getRoomFee();
            if (electricityIndexPair != null) {
                IndexPair indexPair = new IndexPair();
                indexPair.setLastIndex(electricityIndexPair.getLastIndex());
                indexPair.setCurrentIndex(electricityIndexPair.getCurrentIndex());
                paymentHistoryViewHolder.electricityPairIndex = indexPair;
            }
        }
        catch (ValidationException | OperationException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private UtilityIndex getElectricityIndexPair(List<UtilityIndex> utilityIndexList) throws ValidationException, OperationException {
        if (CollectionUtils.isNotEmpty(utilityIndexList)) {
            for (UtilityIndex utilityIndex : utilityIndexList) {
                String utilityId = utilityService.readUtilityId(utilityIndex.getUtilityKey());
                if (UtilityId.ELECTRICITY.name().equals(utilityId)) {
                    return utilityIndex;
                }
            }
        }
        return null;
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

    public class PaymentHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvTotalFee;
        private TextView tvPaymentDate;
        private TextView tvCreationDate;
        private String electricityFee;
        private IndexPair electricityPairIndex;
        private String waterFee;
        private String cabFee;
        private String internetFee;
        private String roomFee;

        public PaymentHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTotalFee = itemView.findViewById(R.id.item_payment_history_payment_fee);
            tvPaymentDate = itemView.findViewById(R.id.item_payment_history_payment_date);
            tvCreationDate = itemView.findViewById(R.id.item_payment_history_creation_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            viewPaymentHistoryDetail();
        }

        private void viewPaymentHistoryDetail() {
            final Dialog dialog = new Dialog(context);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_payment_history_detail);

            TextView tvElectricityFee = dialog.findViewById(R.id.payment_history_detail_electricity_fee);
            TextView tvElectricityLastIndex = dialog.findViewById(R.id.payment_history_detail_electricity_last_index);
            TextView tvElectricityCurrentIndex = dialog.findViewById(R.id.payment_history_detail_electricity_current_index);
            TextView tvWaterFee = dialog.findViewById(R.id.payment_history_detail_water_fee);
            TextView tvCabFee = dialog.findViewById(R.id.payment_history_detail_cab_fee);
            TextView tvInternetFee = dialog.findViewById(R.id.payment_history_detail_internet_fee);
            TextView tvRoomFee = dialog.findViewById(R.id.payment_history_detail_room_fee);

            tvElectricityFee.setText(String.format(FormattedTextUtils.getPriceWithCurrencyTextFormat(context, selectedCurrency), NumberFormatter.formatThousandNumberSeparator(electricityFee)));
            tvElectricityLastIndex.setText(String.format(FormattedTextUtils.getLastIndexFormattedText(context), electricityPairIndex.getLastIndex()));
            tvElectricityCurrentIndex.setText(String.format(FormattedTextUtils.getCurrentIndexFormattedText(context), electricityPairIndex.getCurrentIndex()));
            tvWaterFee.setText(String.format(FormattedTextUtils.getPriceWithCurrencyTextFormat(context, selectedCurrency), NumberFormatter.formatThousandNumberSeparator(waterFee)));
            tvCabFee.setText(String.format(FormattedTextUtils.getPriceWithCurrencyTextFormat(context, selectedCurrency), NumberFormatter.formatThousandNumberSeparator(cabFee)));
            tvInternetFee.setText(String.format(FormattedTextUtils.getPriceWithCurrencyTextFormat(context, selectedCurrency), NumberFormatter.formatThousandNumberSeparator(internetFee)));
            tvRoomFee.setText(String.format(FormattedTextUtils.getPriceWithCurrencyTextFormat(context, selectedCurrency), NumberFormatter.formatThousandNumberSeparator(roomFee)));

            AppCompatButton btnPay = dialog.findViewById(R.id.payment_history_detail_pay);

            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.show();
        }

    }
}
