package com.myroom.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.adapter.GeneralSettingAdapter;
import com.myroom.adapter.UtilityAdapter;
import com.myroom.database.dao.UtilityDAO;
import com.myroom.model.Utility;

import org.apache.commons.lang3.StringUtils;

public class GeneralSettingUtilityActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private UtilityDAO utilityDAO = new UtilityDAO(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);
        initializeToolbar();
        loadAllUtilities();
    }

    private void loadAllUtilities() {
        layoutManager = new LinearLayoutManager(this);
        adapter = new UtilityAdapter(this);
        recyclerView = findViewById(R.id.utility_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_utility_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_utility:
                createUtility();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllUtilities();
    }

    private void createUtility() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_utility);
        AppCompatButton btnCancel = dialog.findViewById(R.id.create_utility_cancel);
        AppCompatButton btnSubmit = dialog.findViewById(R.id.create_utility_submit);
        final EditText etUtilityName = dialog.findViewById(R.id.create_utility_name);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = etUtilityName.getText().toString();
                if (StringUtils.isEmpty(value)) {
                    etUtilityName.setError("This field is required.");
                    return;
                }

                Utility newUtility = new Utility();
                newUtility.setName(value);
                long utilityId = utilityDAO.addUtility(newUtility);
                if (utilityId > 0) {
                    Toast.makeText(v.getContext(), value + " is created.", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(v.getContext(), "Error occurred, please contact Administrator.", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                loadAllUtilities();
            }
        });
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void initializeToolbar() {
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Utility setting");
    }
}
