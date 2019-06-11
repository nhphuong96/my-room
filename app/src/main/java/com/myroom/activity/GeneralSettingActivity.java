package com.myroom.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.myroom.R;
import com.myroom.adapter.GeneralSettingAdapter;
import com.myroom.core.GeneralSettingId;
import com.myroom.dto.GeneralSettingItem;

import java.util.ArrayList;
import java.util.List;

public class GeneralSettingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_setting);
        initializeToolbar();
        loadAllGeneralSettingItem();
    }

    private void loadAllGeneralSettingItem() {
        layoutManager = new LinearLayoutManager(this);
        adapter = new GeneralSettingAdapter(this, initializeGeneralSettingItems());
        recyclerView = findViewById(R.id.general_setting_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private List<GeneralSettingItem> initializeGeneralSettingItems() {
        List<GeneralSettingItem> result = new ArrayList<>();
        GeneralSettingItem utilitySetting = new GeneralSettingItem(GeneralSettingId.UTILITY,"Utility", R.drawable.ic_utility);
        GeneralSettingItem otherSetting = new GeneralSettingItem(GeneralSettingId.OTHER,"Other", R.drawable.ic_dog);
        result.add(utilitySetting);
        result.add(otherSetting);
        return result;
    }

    private void initializeToolbar() {
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("General setting");
    }
}
