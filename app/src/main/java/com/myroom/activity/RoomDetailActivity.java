package com.myroom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.adapter.RoomDetailPagerAdapter;
import com.myroom.core.Constant;

public class RoomDetailActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private long roomId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        initializeToolbar();
        getExtraInputs();
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.pager);

        RoomDetailPagerAdapter adapter = new RoomDetailPagerAdapter(getSupportFragmentManager(), roomId);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabIndicatorFullWidth(true);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initializeToolbar() {
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Phòng đang thuê");
    }

    private void getExtraInputs() {
        Intent intent = getIntent();
        Long roomKey = intent.getLongExtra(Constant.ROOM_KEY_NAME, -1L);
        if (roomId != -1) {
            this.roomId = Long.valueOf(roomKey);
        }
        else {
            Toast.makeText(this, "Không tìm thấy roomId.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
