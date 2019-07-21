package com.myroom.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.myroom.R;
import com.myroom.application.BaseApplication;
import com.myroom.core.Constant;
import com.myroom.core.NumberFormatter;
import com.myroom.database.dao.Guest;
import com.myroom.database.dao.Payment;
import com.myroom.database.repository.GuestRepository;
import com.myroom.database.repository.PaymentRepository;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.ICurrencyService;
import com.myroom.service.IMessageService;
import com.myroom.service.IPaymentService;
import com.myroom.service.sdo.CreateMessageIn;
import com.myroom.service.sdo.CreateMessageOut;
import com.myroom.service.sdo.CreatePaymentIn;
import com.myroom.service.sdo.IndexPair;
import com.myroom.service.sdo.SendMessageIn;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SendMessageFragment extends Fragment {

    private Context context;
    private TextView tvMessage;
    private AppCompatSpinner recipientSelector;
    private AppCompatButton btnSendMessage;
    private long roomKey;
    private String messageContent;

    @Inject
    public GuestRepository guestRepository;
    @Inject
    public ICurrencyService currencyService;
    @Inject
    public IMessageService messageService;
    @Inject
    public IPaymentService paymentService;

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
        getExtraData();
        createMessage();
        loadRecipients();
        setUpSendMessage();
    }

    private void createMessage() {
        try {
            CreateMessageOut createMessageOut = messageService.createMessage(collectCreateMessageIn());
            messageContent = createMessageOut.getMessageContent();
            tvMessage.setText(messageContent);
        } catch (ValidationException | OperationException e) {
            Toast.makeText(context, "không tạo được message", Toast.LENGTH_LONG).show();
        }
    }

    private void getExtraData() {
        roomKey = getArguments().getLong(Constant.ROOM_KEY_NAME);
    }

    private void setUpSendMessage() {
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
                                    sendMessage();
                                    createPayment();
                                    getActivity().finish();
                                } catch (ValidationException | OperationException e) {
                                    Toast.makeText(context, String.format("Hóa đơn gửi không thành công."), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void createPayment() throws ValidationException, OperationException {
        paymentService.createPayment(collectCreatePaymentIn());
    }

    private CreatePaymentIn collectCreatePaymentIn() {
        Bundle bundle = getArguments();
        CreatePaymentIn createPaymentIn = new CreatePaymentIn();
        createPaymentIn.setRoomKey(roomKey);
        createPaymentIn.setElectricityIndices(new IndexPair(bundle.getString(Constant.ELECTRICITY_LAST_INDEX_NAME), bundle.getString(Constant.ELECTRICITY_CURRENT_INDEX_NAME)));
        createPaymentIn.setWaterIndex(bundle.getInt(Constant.WATER_INDEX));
        createPaymentIn.setCabIndex(bundle.getInt(Constant.CAB_INDEX));
        createPaymentIn.setInternetIndex(bundle.getInt(Constant.INTERNET_INDEX));
        createPaymentIn.setRoomIndex(bundle.getInt(Constant.ROOM_INDEX));
        return createPaymentIn;
    }

    private void sendMessage() throws ValidationException, OperationException {
        String recipientPhoneNumber = getSelectedPhoneNumber();
        messageService.sendMessage(convertSendMessageIn(recipientPhoneNumber, messageContent));
        Toast.makeText(context, String.format("Hóa đơn gửi tới %s thành công.", recipientPhoneNumber), Toast.LENGTH_SHORT).show();
    }

    private String getSelectedPhoneNumber() {
        String selectedItem = (String) recipientSelector.getSelectedItem();
        return selectedItem.split(" - ")[1];
    }

    private CreateMessageIn collectCreateMessageIn() {
        Bundle bundle = getArguments();
        CreateMessageIn createMessageIn = new CreateMessageIn();
        createMessageIn.setRoomKey(roomKey);
        createMessageIn.setElectricityIndices(new IndexPair(bundle.getString(Constant.ELECTRICITY_LAST_INDEX_NAME), bundle.getString(Constant.ELECTRICITY_CURRENT_INDEX_NAME)));
        createMessageIn.setWaterIndex(bundle.getInt(Constant.WATER_INDEX));
        createMessageIn.setCabIndex(bundle.getInt(Constant.CAB_INDEX));
        createMessageIn.setInternetIndex(bundle.getInt(Constant.INTERNET_INDEX));
        createMessageIn.setRoomIndex(bundle.getInt(Constant.ROOM_INDEX));
        return createMessageIn;
    }

    private SendMessageIn convertSendMessageIn(String phoneNo, String messageContent) {
        SendMessageIn sendMessageIn = new SendMessageIn();
        sendMessageIn.setMessageContent(messageContent);
        sendMessageIn.setRecipient(phoneNo);
        return sendMessageIn;
    }

    private void loadRecipients() {
        List<Guest> guestList = guestRepository.findGuestByRoomId(roomKey);
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

}
