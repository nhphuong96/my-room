package com.myroom.service.impl;

import com.myroom.application.BaseApplication;
import com.myroom.builder.PaymentBuilder;
import com.myroom.core.Assert;
import com.myroom.database.dao.Payment;
import com.myroom.database.repository.PaymentRepository;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.IPaymentService;
import com.myroom.service.IRoomService;
import com.myroom.service.IUtilityService;
import com.myroom.service.sdo.CreatePaymentIn;
import com.myroom.service.sdo.DeletePaymentIn;
import com.myroom.service.sdo.ReadAvailableUtilityIn;
import com.myroom.service.sdo.ReadAvailableUtilityOut;
import com.myroom.service.sdo.ReadUtilityInRoomIn;
import com.myroom.service.sdo.ReadUtilityInRoomOut;

import javax.inject.Inject;

public class PaymentServiceImpl implements IPaymentService {

    @Inject
    public PaymentRepository paymentRepository;
    @Inject
    public IUtilityService utilityService;

    @Inject
    public PaymentServiceImpl() {
        BaseApplication.getRepositoryComponent(BaseApplication.getContextComponent().getContext()).inject(this);
    }

    @Override
    public void createPayment(CreatePaymentIn createPaymentIn) throws ValidationException, OperationException {
        Assert.assertNotNull(createPaymentIn, "createPaymentIn must not be null.");
        Assert.assertNotNull(createPaymentIn.getRoomKey(), "createPaymentIn.roomKey must not be null.");
        ReadUtilityInRoomOut readUtilityInRoomOut = utilityService.readUtilityInRoom(convertReadUtilityInRoomIn(createPaymentIn));
        PaymentBuilder paymentBuilder = new PaymentBuilder();
        Payment payment = paymentBuilder.setCreatePaymentIn(createPaymentIn)
                .setReadAvailableUtilityOut(readUtilityInRoomOut)
                .build();
        long id = paymentRepository.add(payment);
        if (id <= 0) {
            throw new OperationException("Không thể thêm hóa đơn.");
        }
    }



    @Override
    public void deletePayment(DeletePaymentIn deletePaymentIn) throws ValidationException, OperationException {
        Assert.assertNotNull(deletePaymentIn, "deletePaymentIn must not be null.");
        Assert.assertNotNull(deletePaymentIn.getRoomKey(), "deletePaymentIn must not be null.");

        boolean success = paymentRepository.deleteByRoom(deletePaymentIn.getRoomKey());
        if (!success) {
            throw new OperationException("Không thể xóa hóa đơn.");
        }
    }

    private ReadUtilityInRoomIn convertReadUtilityInRoomIn(CreatePaymentIn createPaymentIn) {
        ReadUtilityInRoomIn readUtilityInRoomIn = new ReadUtilityInRoomIn();
        readUtilityInRoomIn.setRoomKey(createPaymentIn.getRoomKey());
        return readUtilityInRoomIn;
    }
}
