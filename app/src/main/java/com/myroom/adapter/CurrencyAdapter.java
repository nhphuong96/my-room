package com.myroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.application.BaseApplication;
import com.myroom.core.Constant;
import com.myroom.database.dao.Currency;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.ICurrencyService;
import com.myroom.service.sdo.ReadAvailableCurrencyOut;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    private Context context;
    private List<Currency> availableCurrencyList = new ArrayList<>();

    @Inject
    public ICurrencyService currencyService;

    public CurrencyAdapter(Context context) {
        this.context = context;
        BaseApplication.getServiceComponent(context).inject(this);
        try {
            ReadAvailableCurrencyOut readAvailableCurrencyOut = currencyService.readAvailableCurrency();
            availableCurrencyList.addAll(readAvailableCurrencyOut.getCurrencyList());
        }
        catch (OperationException e) {
            Toast.makeText(context, "Lỗi xảy ra khi đọc danh sách tiền tệ hiện có.", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_setting_currency, viewGroup, false);
        CurrencyViewHolder currencyViewHolder = new CurrencyViewHolder(view);
        return currencyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder currencyViewHolder, int i) {
        Currency currency = availableCurrencyList.get(i);
        currencyViewHolder.currencyKey = currency.getCurrencyKey();
        currencyViewHolder.currencyIcon.setImageResource(context.getResources().getIdentifier(currency.getCurrencyIcon(), "drawable", context.getPackageName()));
        currencyViewHolder.currencyId.setText(currency.getCurrencyId());
        currencyViewHolder.selected.setVisibility(currency.getIsSelected() == Constant.TRUE ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return availableCurrencyList.size();
    }

    public class CurrencyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView currencyIcon;
        private TextView currencyId;
        private ImageView selected;
        private long currencyKey;

        public CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyIcon = itemView.findViewById(R.id.item_setting_currency_icon);
            currencyId = itemView.findViewById(R.id.item_setting_currency_cd);
            selected = itemView.findViewById(R.id.item_setting_currency_selected);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try
            {
                currencyService.updateSelectedCurrency(currencyKey);
                ReadAvailableCurrencyOut readAvailableCurrencyOut = currencyService.readAvailableCurrency();
                availableCurrencyList = readAvailableCurrencyOut.getCurrencyList();
                notifyDataSetChanged();
            }
            catch (ValidationException | OperationException e) {
                Toast.makeText(context, "Lỗi xảy ra khi cập nhật tiện tệ mới.", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
