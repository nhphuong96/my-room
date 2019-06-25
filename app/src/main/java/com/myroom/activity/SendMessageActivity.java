package com.myroom.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.application.BaseApplication;
import com.myroom.core.NumberFormatter;
import com.myroom.database.dao.Guest;
import com.myroom.database.repository.GuestRepository;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class SendMessageActivity extends AppCompatActivity {

    private AppCompatSpinner recipientSelector;

    private Long roomId;

    @Inject
    public GuestRepository guestRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        BaseApplication.getRepositoryComponent(this).inject(this);
        initializeToolbar();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            loadRecipients(bundle);
            String messageContent = createBillMessage(bundle);
            sendMessage(messageContent);
        }

    }

    private void sendMessage(final String messageContent) {
        final Context context = this;
        AppCompatButton btnSendMessage = findViewById(R.id.send_message);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Send message")
                        .setMessage("Are you sure you want to send bill to the recipient?")
                        .setNegativeButton("NO", null)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selectedItem = (String) recipientSelector.getSelectedItem();
                                String phoneNo = selectedItem.split(" - ")[1];
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(phoneNo, null, messageContent, null, null);
                                Toast.makeText(context, String.format("Message sent to %s successfully.", phoneNo), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
    }

    private void loadRecipients(Bundle bundle) {
        roomId = bundle.getLong("roomId");
        List<Guest> guestList = guestRepository.findGuestByRoomId(roomId);
        List<String> recipientList = convert(guestList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_recipient, recipientList);
        recipientSelector = findViewById(R.id.recipient_selector);
        recipientSelector.setAdapter(adapter);

    }

    private List<String> convert(List<Guest> guestList) {
        List<String> result = new ArrayList<>();
        for (Guest guest : guestList) {
            result.add(guest.getGuestName() + " - " + StringUtils.defaultIfEmpty(guest.getPhoneNumber(), "N/a"));
        }
        return result;
    }

    private String createBillMessage(Bundle bundle) {
        StringBuilder builder = new StringBuilder();
        builder.append("Tien nha " + new SimpleDateFormat("dd/MM/yyyy").format(new Date())).append("\n");
        Integer electricityFee = appendElectricityProperties(builder, bundle);
        Integer waterFee = appendWaterProperties(builder, bundle);
        Integer cabFee = appendCabProperties(builder, bundle);
        Integer internetFee = appendInternetProperties(builder, bundle);
        builder.append("TONG CONG: " + NumberFormatter.formatThousandNumberSeparator(electricityFee + waterFee + cabFee + internetFee) + " VND");
        String messageContent = builder.toString();

        TextView tvMessageContent = findViewById(R.id.message_content);
        tvMessageContent.setText(messageContent);

        return messageContent;
    }

    private Integer appendInternetProperties(StringBuilder builder, Bundle bundle) {
        String internetCounter = bundle.getString("internetCounter");
        String internetFee = bundle.getString("internetFee");
        if (StringUtils.isBlank(internetCounter) || StringUtils.isBlank(internetFee)) {
            return 0;
        }
        Integer counter = Integer.valueOf(internetCounter);
        Integer fee = Integer.valueOf(internetFee);
        builder.append("Internet: ");
        builder.append(counter);
        builder.append(" x ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(fee));
        builder.append(" = ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(counter * fee));
        builder.append("\n");
        return counter * fee;
    }

    private Integer appendCabProperties(StringBuilder builder, Bundle bundle) {
        String cabCounter = bundle.getString("cabCounter");
        String cabFee = bundle.getString("cabFee");
        if (StringUtils.isBlank(cabCounter) || StringUtils.isBlank(cabFee)) {
            return 0;
        }
        Integer counter = Integer.valueOf(cabCounter);
        Integer fee = Integer.valueOf(cabFee);
        builder.append("Cab: ");
        builder.append(counter);
        builder.append(" x ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(fee));
        builder.append(" = ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(counter * fee));
        builder.append("\n");
        return counter * fee;
    }

    private Integer appendWaterProperties(StringBuilder builder, Bundle bundle) {
        String waterCounter = bundle.getString("waterCounter");
        String waterFee = bundle.getString("waterFee");
        if (StringUtils.isBlank(waterCounter) || StringUtils.isBlank(waterFee)) {
            return 0;
        }
        Integer counter = Integer.valueOf(waterCounter);
        Integer fee = Integer.valueOf(waterFee);
        builder.append("Nuoc: ");
        builder.append(counter);
        builder.append(" x ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(fee));
        builder.append(" = ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(counter * fee));
        builder.append("\n");
        return counter * fee;
    }

    private Integer appendElectricityProperties(StringBuilder builder, Bundle bundle) {
        String electricityLastIndex = bundle.getString("electricityLastIndex");
        String electricityCurrentIndex = bundle.getString("electricityCurrentIndex");
        String electricityFee = bundle.getString("electricityFee");
        if (StringUtils.isBlank(electricityLastIndex) || StringUtils.isBlank(electricityCurrentIndex) || StringUtils.isBlank(electricityFee)) {
            return 0;
        }
        Integer lastIndex = Integer.valueOf(electricityLastIndex);
        Integer currentIndex = Integer.valueOf(electricityCurrentIndex);
        Integer fee = Integer.valueOf(electricityFee);
        builder.append("Dien: ");
        builder.append(currentIndex);
        builder.append(" - ");
        builder.append(lastIndex);
        builder.append(" = ");
        builder.append(currentIndex - lastIndex);
        builder.append(" x ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(fee));
        builder.append(" = ");
        builder.append(NumberFormatter.formatThousandNumberSeparator((currentIndex - lastIndex) * fee));
        builder.append("\n");
        return (currentIndex - lastIndex) * fee;
    }

    private void initializeToolbar() {
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Message review");
    }
}
