package com.myroom.builder;

import com.myroom.core.Calculator;
import com.myroom.core.UtilityId;
import com.myroom.database.dao.Payment;
import com.myroom.service.sdo.ReadUtilityInRoomOut;
import com.myroom.service.sdo.UtilityInRoomItem;
import com.myroom.service.sdo.CreatePaymentIn;

import java.util.Date;

public class PaymentBuilder {
    private ReadUtilityInRoomOut readAvailableUtilityOut;
    private CreatePaymentIn createPaymentIn;

    public Payment build() {
        Payment payment = new Payment();
        payment.setCreationDate(new Date());
        payment.setRoomKey(createPaymentIn.getRoomKey());
        payment.setElectricityFee(calculateElectricityFee());
        payment.setWaterFee(calculateWaterFee());
        payment.setCabFee(calculateCabFee());
        payment.setInternetFee(calculateInternetFee());
        payment.setRoomFee(calculateRoomFee());
        return payment;
    }

    private String calculateRoomFee() {
        UtilityInRoomItem item = findUtility(UtilityId.ROOM);
        Double total = Calculator.calculate(createPaymentIn.getRoomIndex(), Double.valueOf(item.getUtilityFee()));
        return String.valueOf(total);
    }

    private String calculateInternetFee() {
        UtilityInRoomItem item = findUtility(UtilityId.INTERNET);
        Double total = Calculator.calculate(createPaymentIn.getInternetIndex(), Double.valueOf(item.getUtilityFee()));
        return String.valueOf(total);
    }

    private String calculateCabFee() {
        UtilityInRoomItem item = findUtility(UtilityId.CAB);
        Double total = Calculator.calculate(createPaymentIn.getCabIndex(), Double.valueOf(item.getUtilityFee()));
        return String.valueOf(total);
    }

    private String calculateWaterFee() {
        UtilityInRoomItem item = findUtility(UtilityId.WATER);
        Double total = Calculator.calculate(createPaymentIn.getWaterIndex(), Double.valueOf(item.getUtilityFee()));
        return String.valueOf(total);
    }

    private String calculateElectricityFee() {
        UtilityInRoomItem item = findUtility(UtilityId.ELECTRICITY);
        Double total = Calculator.calculate(Integer.valueOf(createPaymentIn.getElectricityIndices().getCurrentIndex()),
                                            Integer.valueOf(createPaymentIn.getElectricityIndices().getLastIndex()),
                                            Double.valueOf(item.getUtilityFee()));
        return total.toString();
    }

    private UtilityInRoomItem findUtility(UtilityId utilityId) throws IllegalArgumentException {
        for (UtilityInRoomItem item : readAvailableUtilityOut.getUtilityInRoomItemList()) {
            if (item.getUtilityId().equals(utilityId.name())) {
                return item;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy tiện ích " + utilityId.name());
    }

    public PaymentBuilder setReadAvailableUtilityOut(ReadUtilityInRoomOut readAvailableUtilityOut) {
        this.readAvailableUtilityOut = readAvailableUtilityOut;
        return this;
    }

    public PaymentBuilder setCreatePaymentIn(CreatePaymentIn createPaymentIn) {
        this.createPaymentIn = createPaymentIn;
        return this;
    }
}
