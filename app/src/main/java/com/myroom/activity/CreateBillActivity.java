package com.myroom.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myroom.R;
import com.myroom.application.BaseApplication;
import com.myroom.database.dao.RoomUtility;
import com.myroom.database.repository.RoomUtilityRepository;
import com.myroom.exception.ValidationException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;

public class CreateBillActivity extends AppCompatActivity {

    @Inject
    public RoomUtilityRepository roomUtilityRepository;

    private View electricityView;
    private View waterView;
    private View cabView;
    private View internetView;

    private Long roomId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);
        BaseApplication.getRepositoryComponent(this).inject(this);
        initializeToolbar();
        getExtraData();
        render();
        createBill();
    }

    private void createBill() {
        final Context context = this;
        AppCompatButton btnCreateBill = findViewById(R.id.create_bill_submit);
        btnCreateBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context, SendMessageActivity.class);
                    intent.putExtra("roomId", roomId);
                    putElectricityProperties(intent);
                    putWaterProperties(intent);
                    putCabProperties(intent);
                    putInternetProperties(intent);
                    context.startActivity(intent);
                } catch (ValidationException e) {
                    //do nothing. let editTexts do their job
                }
            }
        });
    }

    private void putInternetProperties(Intent intent) throws ValidationException {
        if (internetView != null) {
            EditText counter = internetView.findViewById(R.id.internet_counter);
            TextView fee = internetView.findViewById(R.id.unit_fee);
            if (StringUtils.isBlank(counter.getText())) {
                counter.setError("This field is required.");
                throw new ValidationException();
            }
            intent.putExtra("internetCounter", counter.getText().toString());
            intent.putExtra("internetFee", fee.getText().toString());
        }
    }

    private void putCabProperties(Intent intent) throws ValidationException
    {
        if (cabView != null) {
            EditText counter = cabView.findViewById(R.id.cab_counter);
            TextView fee = cabView.findViewById(R.id.unit_fee);
            if (StringUtils.isBlank(counter.getText())) {
                counter.setError("This field is required.");
                throw new ValidationException();
            }
            intent.putExtra("cabCounter", counter.getText().toString());
            intent.putExtra("cabFee", fee.getText().toString());
        }
    }

    private void putWaterProperties(Intent intent) throws ValidationException
    {
        if (waterView != null) {
            EditText counter = waterView.findViewById(R.id.water_counter);
            TextView fee = waterView.findViewById(R.id.unit_fee);
            if (StringUtils.isBlank(counter.getText())) {
                counter.setError("This field is required.");
                throw new ValidationException();
            }
            intent.putExtra("waterCounter", counter.getText().toString());
            intent.putExtra("waterFee", fee.getText().toString());
        }
    }

    private void putElectricityProperties(Intent intent) throws ValidationException {
        if (electricityView != null) {
            EditText currentIndex = electricityView.findViewById(R.id.electricity_current_index);
            EditText lastIndex = electricityView.findViewById(R.id.electricity_last_index);
            TextView fee = electricityView.findViewById(R.id.unit_fee);
            if (StringUtils.isBlank(lastIndex.getText())) {
                lastIndex.setError("This field is required.");
                throw new ValidationException();
            }
            if (StringUtils.isBlank(currentIndex.getText())) {
                currentIndex.setError("This field is required.");
                throw new ValidationException();
            }
            intent.putExtra("electricityLastIndex", lastIndex.getText().toString());
            intent.putExtra("electricityCurrentIndex", currentIndex.getText().toString());
            intent.putExtra("electricityFee", fee.getText().toString());
        }
    }

    private void render() {
        LinearLayout linearLayout = findViewById(R.id.linear_layout);
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
        root.addView(internetView);
    }

    private void cabCalculationLayout(LinearLayout root, RoomUtility cabUtility) {
        cabView = getLayoutInflater().inflate(R.layout.view_cab_calculator, null);
        TextView tvUnitFee = cabView.findViewById(R.id.unit_fee);
        tvUnitFee.setText(String.valueOf(cabUtility.getUtilityFee()));
        root.addView(cabView);
    }

    private void electricityCalculationLayout(LinearLayout root, RoomUtility electricityUtility) {
        electricityView = getLayoutInflater().inflate(R.layout.view_electricity_calculator, null);
        TextView tvUnitFee = electricityView.findViewById(R.id.unit_fee);
        tvUnitFee.setText(String.valueOf(electricityUtility.getUtilityFee()));
        root.addView(electricityView);
    }

    private void waterCalculationLayout(LinearLayout root, RoomUtility waterUtility) {
        waterView = getLayoutInflater().inflate(R.layout.view_water_calculator, null);
        TextView tvUnitFee = waterView.findViewById(R.id.unit_fee);
        tvUnitFee.setText(String.valueOf(waterUtility.getUtilityFee()));
        root.addView(waterView);
    }

    private void addLineSeparator(LinearLayout linearLayout) {
        LinearLayout lineLayout = new LinearLayout(this);
        lineLayout.setBackgroundColor(Color.GRAY);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2);
        params.setMargins(0, 0, 0, 0);
        lineLayout.setLayoutParams(params);
        linearLayout.addView(lineLayout);
    }

    private void getExtraData() {
        roomId = getIntent().getExtras().getLong("roomId");
    }

    private void initializeToolbar() {
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Create bill");
    }
}
