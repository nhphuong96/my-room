package com.myroom.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.application.BaseApplication;
import com.myroom.database.dao.Currency;
import com.myroom.database.dao.RoomUtility;
import com.myroom.database.repository.RoomUtilityRepository;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.ICurrencyService;
import com.myroom.service.OnDataPasser;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;

public class CreateBillFragment extends Fragment {

    private Context parentContext;
    private LinearLayout linearLayout;
    private View electricityView;
    private View waterView;
    private View cabView;
    private View internetView;
    private AppCompatButton button;
    private Currency selectedCurrency;

    private OnDataPasser dataPasser;

    @Inject
    public RoomUtilityRepository roomUtilityRepository;
    @Inject
    public ICurrencyService currencyService;

    public static Fragment newInstance(Long roomId) {
        CreateBillFragment createBillFragment = new CreateBillFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("roomId", roomId);
        createBillFragment.setArguments(bundle);
        return createBillFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPasser) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentContext = container.getContext();
        View view = inflater.inflate(R.layout.fragment_create_bill, container, false);
        linearLayout = view.findViewById(R.id.linear_layout);
        button = view.findViewById(R.id.create_bill_submit);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BaseApplication.getRepositoryComponent(parentContext).inject(this);
        BaseApplication.getServiceComponent(parentContext).inject(this);
        loadSelectedCurrency();
        render();
        submitCreateBill();

    }

    private void loadSelectedCurrency() {
        try {
            selectedCurrency = currencyService.readSelectedCurrency().getCurrency();
        } catch (OperationException e) {
            Toast.makeText(parentContext, "Lỗi xảy ra: Không tìm được tiền tệ.", Toast.LENGTH_SHORT).show();
        }
    }

    public void submitCreateBill() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Bundle bundle = new Bundle();
                    bundle.putLong("roomId", getArguments().getLong("roomId"));
                    putElectricityProperties(bundle);
                    putWaterProperties(bundle);
                    putCabProperties(bundle);
                    putInternetProperties(bundle);
                    new AlertDialog.Builder(parentContext)
                            .setTitle("Tạo tin nhắn").setMessage("Bạn có chắc các thông tin đã đủ và tạo tin nhắn?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dataPasser.onDataPasser(bundle);
                                }
                            })
                            .setNegativeButton("Không", null)
                            .setIcon(R.drawable.ic_alert)
                            .show();
                } catch (ValidationException e) {
                    Toast.makeText(parentContext, "Không thể tạo hóa đơn.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void putInternetProperties(Bundle bundle) throws ValidationException {
        if (internetView != null) {
            EditText counter = internetView.findViewById(R.id.internet_counter);
            TextView fee = internetView.findViewById(R.id.unit_fee);
            if (StringUtils.isBlank(counter.getText())) {
                counter.setError("Bắt buộc.");
                throw new ValidationException();
            }
            bundle.putString("internetCounter", counter.getText().toString());
            bundle.putString("internetFee", fee.getText().toString());
        }
    }

    private void putCabProperties(Bundle bundle) throws ValidationException
    {
        if (cabView != null) {
            EditText counter = cabView.findViewById(R.id.cab_counter);
            TextView fee = cabView.findViewById(R.id.unit_fee);
            if (StringUtils.isBlank(counter.getText())) {
                counter.setError("Bắt buộc.");
                throw new ValidationException();
            }
            bundle.putString("cabCounter", counter.getText().toString());
            bundle.putString("cabFee", fee.getText().toString());
        }
    }

    private void putWaterProperties(Bundle bundle) throws ValidationException
    {
        if (waterView != null) {
            EditText counter = waterView.findViewById(R.id.water_counter);
            TextView fee = waterView.findViewById(R.id.unit_fee);
            if (StringUtils.isBlank(counter.getText())) {
                counter.setError("Bắt buộc.");
                throw new ValidationException();
            }
            bundle.putString("waterCounter", counter.getText().toString());
            bundle.putString("waterFee", fee.getText().toString());
        }
    }

    private void putElectricityProperties(Bundle bundle) throws ValidationException {
        if (electricityView != null) {
            EditText currentIndex = electricityView.findViewById(R.id.electricity_current_index);
            EditText lastIndex = electricityView.findViewById(R.id.electricity_last_index);
            TextView fee = electricityView.findViewById(R.id.unit_fee);
            if (StringUtils.isBlank(lastIndex.getText())) {
                lastIndex.setError("Bắt buộc.");
                throw new ValidationException();
            }
            if (StringUtils.isBlank(currentIndex.getText())) {
                currentIndex.setError("Bắt buộc.");
                throw new ValidationException();
            }
            bundle.putString("electricityLastIndex", lastIndex.getText().toString());
            bundle.putString("electricityCurrentIndex", currentIndex.getText().toString());
            bundle.putString("electricityFee", fee.getText().toString());
        }
    }

    private void render() {
        Bundle bundle = this.getArguments();
        Long roomId = bundle.getLong("roomId");
        List<RoomUtility> roomUtilityList = roomUtilityRepository.findRoomUtilityByRoomId(roomId);
        if (CollectionUtils.isNotEmpty(roomUtilityList)) {
            for (RoomUtility roomUtility : roomUtilityList) {
                long id = roomUtility.getUtilityId();
                if (id == 1L) {
                    electricityCalculationLayout(linearLayout, roomUtility);
                }
                else if (id == 2L) {
                    addLineSeparator(linearLayout);
                    waterCalculationLayout(linearLayout, roomUtility);
                }
                else if (id == 3L) {
                    addLineSeparator(linearLayout);
                    cabCalculationLayout(linearLayout, roomUtility);
                }
                else if (id == 4L) {
                    addLineSeparator(linearLayout);
                    internetCalculationLayout(linearLayout, roomUtility);
                }
            }
        }
    }

    private void internetCalculationLayout(LinearLayout root, RoomUtility internetUtility) {
        internetView = getLayoutInflater().inflate(R.layout.view_internet_calculator, null);
        TextView tvUnitFee = internetView.findViewById(R.id.unit_fee);
        tvUnitFee.setText(String.valueOf(internetUtility.getUtilityFee()));
        TextView tvCurrency= internetView.findViewById(R.id.currency);
        tvCurrency.setText(selectedCurrency.getCurrencyCd());
        root.addView(internetView);
    }

    private void cabCalculationLayout(LinearLayout root, RoomUtility cabUtility) {
        cabView = getLayoutInflater().inflate(R.layout.view_cab_calculator, null);
        TextView tvUnitFee = cabView.findViewById(R.id.unit_fee);
        tvUnitFee.setText(String.valueOf(cabUtility.getUtilityFee()));
        TextView tvCurrency= cabView.findViewById(R.id.currency);
        tvCurrency.setText(selectedCurrency.getCurrencyCd());
        root.addView(cabView);
    }

    private void electricityCalculationLayout(LinearLayout root, RoomUtility electricityUtility) {
        electricityView = getLayoutInflater().inflate(R.layout.view_electricity_calculator, null);
        TextView tvUnitFee = electricityView.findViewById(R.id.unit_fee);
        tvUnitFee.setText(String.valueOf(electricityUtility.getUtilityFee()));
        TextView tvCurrency= electricityView.findViewById(R.id.currency);
        tvCurrency.setText(selectedCurrency.getCurrencyCd());
        root.addView(electricityView);
    }

    private void waterCalculationLayout(LinearLayout root, RoomUtility waterUtility) {
        waterView = getLayoutInflater().inflate(R.layout.view_water_calculator, null);
        TextView tvUnitFee = waterView.findViewById(R.id.unit_fee);
        tvUnitFee.setText(String.valueOf(waterUtility.getUtilityFee()));
        TextView tvCurrency= waterView.findViewById(R.id.currency);
        tvCurrency.setText(selectedCurrency.getCurrencyCd());
        root.addView(waterView);
    }

    private void addLineSeparator(LinearLayout linearLayout) {
        LinearLayout lineLayout = new LinearLayout(parentContext);
        lineLayout.setBackgroundColor(Color.GRAY);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2);
        params.setMargins(0, 0, 0, 0);
        lineLayout.setLayoutParams(params);
        linearLayout.addView(lineLayout);
    }
}
