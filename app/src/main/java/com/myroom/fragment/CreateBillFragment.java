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
import com.myroom.core.Constant;
import com.myroom.core.UtilityId;
import com.myroom.database.dao.Currency;
import com.myroom.database.dao.RoomUtility;
import com.myroom.database.repository.RoomUtilityRepository;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.ICurrencyService;
import com.myroom.service.IUtilityService;
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
    private View roomView;
    private AppCompatButton button;
    private Currency selectedCurrency;

    private OnDataPasser dataPasser;

    @Inject
    public RoomUtilityRepository roomUtilityRepository;
    @Inject
    public IUtilityService utilityService;
    @Inject
    public ICurrencyService currencyService;

    public static Fragment newInstance(Long roomId) {
        CreateBillFragment createBillFragment = new CreateBillFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.ROOM_KEY_NAME, roomId);
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
        try {
            loadSelectedCurrency();
            render();
            setUpCreateBill();
        } catch (ValidationException | OperationException e) {
            Toast.makeText(parentContext, "Lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadSelectedCurrency() throws OperationException {
        selectedCurrency = currencyService.readSelectedCurrency().getCurrency();
    }

    public void setUpCreateBill() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Bundle bundle = new Bundle();
                    bundle.putLong(Constant.ROOM_KEY_NAME, getArguments().getLong(Constant.ROOM_KEY_NAME));
                    putElectricityProperties(bundle);
                    putWaterProperties(bundle);
                    putCabProperties(bundle);
                    putInternetProperties(bundle);
                    putRoomProperties(bundle);
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

    private void putRoomProperties(Bundle bundle) throws ValidationException {
        if (internetView != null) {
            EditText counter = roomView.findViewById(R.id.counter);
            if (StringUtils.isBlank(counter.getText())) {
                counter.setError("Bắt buộc.");
                throw new ValidationException();
            }
            bundle.putInt(Constant.ROOM_INDEX, Integer.valueOf(counter.getText().toString()));
        }
    }

    private void putInternetProperties(Bundle bundle) throws ValidationException {
        if (internetView != null) {
            EditText counter = internetView.findViewById(R.id.counter);
            if (StringUtils.isBlank(counter.getText())) {
                counter.setError("Bắt buộc.");
                throw new ValidationException();
            }
            bundle.putInt(Constant.INTERNET_INDEX, Integer.valueOf(counter.getText().toString()));
        }
    }

    private void putCabProperties(Bundle bundle) throws ValidationException
    {
        if (cabView != null) {
            EditText counter = cabView.findViewById(R.id.counter);
            if (StringUtils.isBlank(counter.getText())) {
                counter.setError("Bắt buộc.");
                throw new ValidationException();
            }
            bundle.putInt(Constant.CAB_INDEX, Integer.valueOf(counter.getText().toString()));
        }
    }

    private void putWaterProperties(Bundle bundle) throws ValidationException
    {
        if (waterView != null) {
            EditText counter = waterView.findViewById(R.id.counter);
            if (StringUtils.isBlank(counter.getText())) {
                counter.setError("Bắt buộc.");
                throw new ValidationException();
            }
            bundle.putInt(Constant.WATER_INDEX, Integer.valueOf(counter.getText().toString()));
        }
    }

    private void putElectricityProperties(Bundle bundle) throws ValidationException {
        if (electricityView != null) {
            EditText currentIndex = electricityView.findViewById(R.id.electricity_current_index);
            EditText lastIndex = electricityView.findViewById(R.id.electricity_last_index);
            if (StringUtils.isBlank(lastIndex.getText())) {
                lastIndex.setError("Bắt buộc.");
                throw new ValidationException();
            }
            if (StringUtils.isBlank(currentIndex.getText())) {
                currentIndex.setError("Bắt buộc.");
                throw new ValidationException();
            }
            bundle.putInt(Constant.ELECTRICITY_LAST_INDEX_NAME, Integer.valueOf(lastIndex.getText().toString()));
            bundle.putInt(Constant.ELECTRICITY_CURRENT_INDEX_NAME, Integer.valueOf(currentIndex.getText().toString()));
        }
    }

    private void render() throws ValidationException, OperationException {
        Bundle bundle = this.getArguments();
        Long roomId = bundle.getLong(Constant.ROOM_KEY_NAME);
        List<RoomUtility> roomUtilityList = roomUtilityRepository.findRoomUtilityByRoomKey(roomId);
        if (CollectionUtils.isNotEmpty(roomUtilityList)) {
            for (RoomUtility roomUtility : roomUtilityList) {
                String utilityId = utilityService.readUtilityId(roomUtility.getUtilityKey());
                switch (UtilityId.valueOf(utilityId)) {
                    case ELECTRICITY:
                        electricityCalculationLayout(linearLayout, roomUtility);
                        break;
                    case WATER:
                        addLineSeparator(linearLayout);
                        waterCalculationLayout(linearLayout, roomUtility);
                        break;
                    case CAB:
                        addLineSeparator(linearLayout);
                        cabCalculationLayout(linearLayout, roomUtility);
                        break;
                    case INTERNET:
                        addLineSeparator(linearLayout);
                        internetCalculationLayout(linearLayout, roomUtility);
                        break;
                    case ROOM:
                        addLineSeparator(linearLayout);
                        roomCalculatorLayout(linearLayout, roomUtility);
                        break;
                    default:
                }
            }
        }
    }

    private void roomCalculatorLayout(LinearLayout root, RoomUtility roomUtility) {
        roomView = getLayoutInflater().inflate(R.layout.view_calculator, null);
        TextView tvUnitFee = roomView.findViewById(R.id.unit_fee);
        tvUnitFee.setText(String.valueOf(roomUtility.getUtilityFee()));
        TextView tvCurrency= roomView.findViewById(R.id.currency);
        tvCurrency.setText(selectedCurrency.getCurrencyId());
        EditText etCounter = roomView.findViewById(R.id.counter);
        etCounter.setText(String.valueOf(1));
        etCounter.setEnabled(false);
        TextView tvUtilityName = roomView.findViewById(R.id.view_calculator_utility_name);
        tvUtilityName.setText("Phí phòng");
        root.addView(roomView);
    }

    private void internetCalculationLayout(LinearLayout root, RoomUtility internetUtility) {
        internetView = getLayoutInflater().inflate(R.layout.view_calculator, null);
        TextView tvUnitFee = internetView.findViewById(R.id.unit_fee);
        tvUnitFee.setText(String.valueOf(internetUtility.getUtilityFee()));
        TextView tvCurrency= internetView.findViewById(R.id.currency);
        tvCurrency.setText(selectedCurrency.getCurrencyId());
        TextView tvUtilityName = internetView.findViewById(R.id.view_calculator_utility_name);
        tvUtilityName.setText("Internet");
        root.addView(internetView);
    }

    private void cabCalculationLayout(LinearLayout root, RoomUtility cabUtility) {
        cabView = getLayoutInflater().inflate(R.layout.view_calculator, null);
        TextView tvUnitFee = cabView.findViewById(R.id.unit_fee);
        tvUnitFee.setText(String.valueOf(cabUtility.getUtilityFee()));
        TextView tvCurrency= cabView.findViewById(R.id.currency);
        tvCurrency.setText(selectedCurrency.getCurrencyId());
        TextView tvUtilityName = cabView.findViewById(R.id.view_calculator_utility_name);
        tvUtilityName.setText("Cab");
        root.addView(cabView);
    }

    private void electricityCalculationLayout(LinearLayout root, RoomUtility electricityUtility) {
        electricityView = getLayoutInflater().inflate(R.layout.view_electricity_calculator, null);
        TextView tvUnitFee = electricityView.findViewById(R.id.unit_fee);
        tvUnitFee.setText(String.valueOf(electricityUtility.getUtilityFee()));
        TextView tvCurrency= electricityView.findViewById(R.id.currency);
        tvCurrency.setText(selectedCurrency.getCurrencyId());
        root.addView(electricityView);
    }

    private void waterCalculationLayout(LinearLayout root, RoomUtility waterUtility) {
        waterView = getLayoutInflater().inflate(R.layout.view_calculator, null);
        TextView tvUnitFee = waterView.findViewById(R.id.unit_fee);
        tvUnitFee.setText(String.valueOf(waterUtility.getUtilityFee()));
        TextView tvCurrency= waterView.findViewById(R.id.currency);
        tvCurrency.setText(selectedCurrency.getCurrencyId());
        TextView tvUtilityName = waterView.findViewById(R.id.view_calculator_utility_name);
        tvUtilityName.setText("Nước");
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
