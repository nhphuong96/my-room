package com.myroom.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.myroom.R;
import com.myroom.application.BaseApplication;
import com.myroom.database.dao.RoomUtility;
import com.myroom.database.repository.RoomUtilityRepository;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import javax.inject.Inject;

public class CreateBillActivity extends AppCompatActivity {

    @Inject
    public RoomUtilityRepository roomUtilityRepository;

    private Long roomId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);
        BaseApplication.getRepositoryComponent(this).inject(this);
        initializeToolbar();
        getExtraData();
        render();
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
                    addLineSeperator(linearLayout);
                    waterCalculationLayout(linearLayout, roomUtility);
                }
                else if (id == 3L) {
                    addLineSeperator(linearLayout);
                    cabCalculationLayout(linearLayout, roomUtility);
                }
                else if (id == 4L) {
                    addLineSeperator(linearLayout);
                    internetCalculationLayout(linearLayout, roomUtility);
                }
            }
        }
    }

    private void internetCalculationLayout(LinearLayout root, RoomUtility internetUtility) {
        View view = getLayoutInflater().inflate(R.layout.view_internet_calculator, null);
        TextView tvUtitFee = view.findViewById(R.id.unit_fee);
        tvUtitFee.setText(String.valueOf(internetUtility.getUtilityFee()));
        root.addView(view);
    }

    private void cabCalculationLayout(LinearLayout root, RoomUtility cabUtility) {
        View view = getLayoutInflater().inflate(R.layout.view_cab_calculator, null);
        TextView tvUtitFee = view.findViewById(R.id.unit_fee);
        tvUtitFee.setText(String.valueOf(cabUtility.getUtilityFee()));
        root.addView(view);
    }

    private void electricityCalculationLayout(LinearLayout root, RoomUtility electricityUtility) {
        View view = getLayoutInflater().inflate(R.layout.view_electricity_calculator, null);
        TextView tvUtitFee = view.findViewById(R.id.unit_fee);
        tvUtitFee.setText(String.valueOf(electricityUtility.getUtilityFee()));
        root.addView(view);
    }

    private void waterCalculationLayout(LinearLayout root, RoomUtility waterUtility) {
        View view = getLayoutInflater().inflate(R.layout.view_water_calculator, null);
        TextView tvUnitFee = view.findViewById(R.id.unit_fee);
        tvUnitFee.setText(String.valueOf(waterUtility.getUtilityFee()));
        root.addView(view);
    }

    private void addLineSeperator(LinearLayout linearLayout) {
        LinearLayout lineLayout = new LinearLayout(this);
        lineLayout.setBackgroundColor(Color.GRAY);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2);
        params.setMargins(0, 0, 0, 0);
        lineLayout.setLayoutParams(params);
        linearLayout.addView(lineLayout);
    }

    private int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
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
