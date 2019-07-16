package com.myroom.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.myroom.R;
import com.myroom.core.Constant;
import com.myroom.database.repository.RoomUtilityRepository;
import com.myroom.fragment.CreateBillFragment;
import com.myroom.fragment.SendMessageFragment;
import com.myroom.service.OnDataPasser;

import javax.inject.Inject;

public class CreateBillActivity extends AppCompatActivity implements OnDataPasser {

    @Inject
    public RoomUtilityRepository roomUtilityRepository;

    private View electricityView;
    private View waterView;
    private View cabView;
    private View internetView;
    private ViewPager viewPager;

    private Long roomId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);
        initializeToolbar("Tạo hóa đơn");
        getExtraData();
        render();
    }

    private void render() {
        Fragment createBillFragment = CreateBillFragment.newInstance(roomId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_create_bill_fragment_container, createBillFragment, "create-bill-fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getExtraData() {
        roomId = getIntent().getExtras().getLong(Constant.ROOM_KEY_NAME);
    }

    private void initializeToolbar(String title) {
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
    }

    @Override
    public void onDataPasser(Bundle bundle) {
        Fragment sendMessageFragment = SendMessageFragment.newInstance(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_create_bill_fragment_container, sendMessageFragment, "send-message-fragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        initializeToolbar("Gửi tin nhắn");
    }
}
