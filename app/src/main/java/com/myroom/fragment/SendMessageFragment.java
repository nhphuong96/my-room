package com.myroom.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.activity.MainActivity;
import com.myroom.application.BaseApplication;
import com.myroom.core.NumberFormatter;
import com.myroom.database.dao.Currency;
import com.myroom.database.dao.Guest;
import com.myroom.database.repository.GuestRepository;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.ICurrencyService;
import com.myroom.service.IMessageService;
import com.myroom.service.impl.CurrencyServiceImpl;
import com.myroom.service.sdo.SendMessageIn;
import com.myroom.utils.DateUtils;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class SendMessageFragment extends Fragment {

    private Context context;
    private TextView tvMessage;
    private AppCompatSpinner recipientSelector;
    private AppCompatButton btnSendMessage;
    private long roomId;
    private Currency selectedCurrency;

    @Inject
    public GuestRepository guestRepository;
    @Inject
    public ICurrencyService currencyService;
    @Inject
    public IMessageService messageService;

    public static Fragment newInstance(Bundle bundle) {
        SendMessageFragment sendMessageFragment = new SendMessageFragment();
        sendMessageFragment.setArguments(bundle);
        return sendMessageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_send_message, container, false);
        tvMessage = view.findViewById(R.id.message_content);
        recipientSelector = view.findViewById(R.id.recipient_selector);
        btnSendMessage = view.findViewById(R.id.send_message);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BaseApplication.getRepositoryComponent(context).inject(this);
        BaseApplication.getServiceComponent(context).inject(this);
        loadSelectedCurrency();
        Bundle bundle = getArguments();
        if (bundle != null) {
            loadRecipients(bundle);
            String messageContent = createBillMessage(bundle);
            sendMessage(messageContent);
        }
    }

    private void loadSelectedCurrency() {
        try {
            selectedCurrency = currencyService.readSelectedCurrency().getCurrency();
        } catch (OperationException e) {
            Toast.makeText(context, "Lỗi xảy ra: không tìm thấy tiền tệ thích hợp.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessage(final String messageContent) {
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Gửi tin nhắn")
                        .setMessage("Bạn có chắc muốn gửi hóa đơn tới người này?")
                        .setNegativeButton("Không", null)
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    String selectedItem = (String) recipientSelector.getSelectedItem();
                                    String phoneNo = selectedItem.split(" - ")[1];
                                    messageService.sendMessage(convertSendMessageIn(phoneNo, messageContent));
                                    Toast.makeText(context, String.format("Hóa đơn gửi tới %s thành công.", phoneNo), Toast.LENGTH_SHORT).show();
                                } catch (ValidationException e) {
                                    Toast.makeText(context, String.format("Hóa đơn gửi không thành công."), Toast.LENGTH_SHORT).show();
                                } catch (OperationException e) {
                                    Toast.makeText(context, String.format("Hóa đơn gửi không thành công."), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private SendMessageIn convertSendMessageIn(String phoneNo, String messageContent) {
        SendMessageIn sendMessageIn = new SendMessageIn();
        sendMessageIn.setMessageContent(messageContent);
        sendMessageIn.setRecipient(phoneNo);
        return sendMessageIn;
    }

    private void loadRecipients(Bundle bundle) {
        roomId = getArguments().getLong("roomId");
        List<Guest> guestList = guestRepository.findGuestByRoomId(roomId);
        List<String> recipientList = convert(guestList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.item_recipient, recipientList);
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
        builder.append("Tien nha " + DateUtils.convertDateToStringAsDDMMYYYY(new Date())).append("\n");
        Double electricityFee = appendElectricityProperties(builder, bundle);
        Double waterFee = appendWaterProperties(builder, bundle);
        Double cabFee = appendCabProperties(builder, bundle);
        Double internetFee = appendInternetProperties(builder, bundle);
        Double roomFee = appendRoomProperties(builder, bundle);
        builder.append("TONG CONG: " + NumberFormatter.formatThousandNumberSeparator(String.valueOf(electricityFee + waterFee + cabFee + internetFee + roomFee)) + " " + selectedCurrency.getCurrencyCd());
        String messageContent = builder.toString();
        tvMessage.setText(messageContent);
        return messageContent;
    }

    private Double appendRoomProperties(StringBuilder builder, Bundle bundle) {
        String roomCounter = bundle.getString("roomCounter");
        String roomFee = bundle.getString("roomFee");
        if (StringUtils.isBlank(roomCounter) || StringUtils.isBlank(roomFee)) {
            return 0D;
        }
        Integer counter = Integer.valueOf(roomCounter);
        Double fee = Double.valueOf(roomFee);
        builder.append("Tiền nhà: ");
        builder.append(counter);
        builder.append(" x ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(String.valueOf(fee)));
        builder.append(" = ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(String.valueOf(counter * fee)));
        builder.append("\n");
        return counter * fee;
    }

    private Double appendInternetProperties(StringBuilder builder, Bundle bundle) {
        String internetCounter = bundle.getString("internetCounter");
        String internetFee = bundle.getString("internetFee");
        if (StringUtils.isBlank(internetCounter) || StringUtils.isBlank(internetFee)) {
            return 0D;
        }
        Integer counter = Integer.valueOf(internetCounter);
        Double fee = Double.valueOf(internetFee);
        builder.append("Internet: ");
        builder.append(counter);
        builder.append(" x ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(String.valueOf(fee)));
        builder.append(" = ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(String.valueOf(counter * fee)));
        builder.append("\n");
        return counter * fee;
    }

    private Double appendCabProperties(StringBuilder builder, Bundle bundle) {
        String cabCounter = bundle.getString("cabCounter");
        String cabFee = bundle.getString("cabFee");
        if (StringUtils.isBlank(cabCounter) || StringUtils.isBlank(cabFee)) {
            return 0D;
        }
        Integer counter = Integer.valueOf(cabCounter);
        Double fee = Double.valueOf(cabFee);
        builder.append("Cab: ");
        builder.append(counter);
        builder.append(" x ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(String.valueOf(fee)));
        builder.append(" = ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(String.valueOf(counter * fee)));
        builder.append("\n");
        return counter * fee;
    }

    private Double appendWaterProperties(StringBuilder builder, Bundle bundle) {
        String waterCounter = bundle.getString("waterCounter");
        String waterFee = bundle.getString("waterFee");
        if (StringUtils.isBlank(waterCounter) || StringUtils.isBlank(waterFee)) {
            return 0D;
        }
        Integer counter = Integer.valueOf(waterCounter);
        Double fee = Double.valueOf(waterFee);
        builder.append("Nuoc: ");
        builder.append(counter);
        builder.append(" x ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(String.valueOf(fee)));
        builder.append(" = ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(String.valueOf(counter * fee)));
        builder.append("\n");
        return counter * fee;
    }

    private Double appendElectricityProperties(StringBuilder builder, Bundle bundle) {
        String electricityLastIndex = bundle.getString("electricityLastIndex");
        String electricityCurrentIndex = bundle.getString("electricityCurrentIndex");
        String electricityFee = bundle.getString("electricityFee");
        if (StringUtils.isBlank(electricityLastIndex) || StringUtils.isBlank(electricityCurrentIndex) || StringUtils.isBlank(electricityFee)) {
            return 0D;
        }
        Integer lastIndex = Integer.valueOf(electricityLastIndex);
        Integer currentIndex = Integer.valueOf(electricityCurrentIndex);
        Double fee = Double.valueOf(electricityFee);
        builder.append("Dien: ");
        builder.append(currentIndex);
        builder.append(" - ");
        builder.append(lastIndex);
        builder.append(" = ");
        builder.append(currentIndex - lastIndex);
        builder.append(" x ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(String.valueOf(fee)));
        builder.append(" = ");
        builder.append(NumberFormatter.formatThousandNumberSeparator(String.valueOf((currentIndex - lastIndex) * fee)));
        builder.append("\n");
        return (currentIndex - lastIndex) * fee;
    }

}
