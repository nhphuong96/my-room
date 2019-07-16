package com.myroom.builder;

import com.myroom.core.NumberFormatter;
import com.myroom.core.UtilityId;
import com.myroom.database.dao.Currency;
import com.myroom.dto.UtilityInRoomItem;
import com.myroom.service.sdo.CreateMessageIn;
import com.myroom.service.sdo.ReadAvailableUtilityOut;
import com.myroom.utils.DateUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class MessageBuilder {
    private CreateMessageIn createMessageIn;
    private ReadAvailableUtilityOut readAvailableUtilityOut;
    private Currency selectedCurrency;
    private Double electricityFee;
    private Double waterFee;
    private Double cabFee;
    private Double internetFee;
    private Double roomFee;

    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("Tien nha " + DateUtils.convertDateToStringAsDDMMYYYY(new Date())).append("\n");
        builder.append(buildElectricityMessage());
        builder.append(buildWaterMessage());
        builder.append(buildCabMessage());
        builder.append(buildInternetMessage());
        builder.append(buildRoomMessage());
        builder.append("TONG CONG: " + NumberFormatter.formatThousandNumberSeparator(String.valueOf(electricityFee + waterFee + cabFee + internetFee + roomFee)) + " " + selectedCurrency.getCurrencyId());
        String messageContent = builder.toString();
        return messageContent;
    }

    private String buildRoomMessage() {
        if (createMessageIn.getRoomIndex() != null) {
            UtilityInRoomItem roomItem = findUtility(UtilityId.ROOM);
            Integer index = createMessageIn.getRoomIndex();
            Double total = index * Double.valueOf(roomItem.getUtilityFee());
            roomFee = total;
            StringBuilder builder = new StringBuilder(roomItem.getUtilityName()).append(": ");
            builder.append(index).append(" x ").append(roomItem.getUtilityFee());
            builder.append(" = ").append(total).append("\n");
            return builder.toString();
        }
        return StringUtils.EMPTY;
    }

    private String buildInternetMessage() {
        if (createMessageIn.getInternetIndex() != null) {
            UtilityInRoomItem internetItem = findUtility(UtilityId.INTERNET);
            Integer index = createMessageIn.getInternetIndex();
            Double total = index * Double.valueOf(internetItem.getUtilityFee());
            internetFee = total;
            StringBuilder builder = new StringBuilder(internetItem.getUtilityName()).append(": ");
            builder.append(index).append(" x ").append(internetItem.getUtilityFee());
            builder.append(" = ").append(total).append("\n");
            return builder.toString();
        }
        return StringUtils.EMPTY;
    }

    private String buildCabMessage() {
        if (createMessageIn.getCabIndex() != null) {
            UtilityInRoomItem cabItem = findUtility(UtilityId.CAB);
            Integer index = createMessageIn.getCabIndex();
            Double total = index * Double.valueOf(cabItem.getUtilityFee());
            cabFee = total;
            StringBuilder builder = new StringBuilder(cabItem.getUtilityName()).append(": ");
            builder.append(index).append(" x ").append(cabItem.getUtilityFee());
            builder.append(" = ").append(total).append("\n");
            return builder.toString();
        }
        return StringUtils.EMPTY;
    }

    private String buildWaterMessage() throws IllegalArgumentException {
        if (createMessageIn.getWaterIndex() != null) {
            UtilityInRoomItem waterItem = findUtility(UtilityId.WATER);
            Integer index = createMessageIn.getWaterIndex();
            Double total = index * Double.valueOf(waterItem.getUtilityFee());
            waterFee = total;
            StringBuilder builder = new StringBuilder(waterItem.getUtilityName()).append(": ");
            builder.append(index).append(" x ").append(waterItem.getUtilityFee());
            builder.append(" = ").append(total).append("\n");
            return builder.toString();
        }
        return StringUtils.EMPTY;
    }

    private String buildElectricityMessage() throws IllegalArgumentException {
        if (createMessageIn.getElectricityIndices() != null) {
            UtilityInRoomItem electricityItem = findUtility(UtilityId.ELECTRICITY);
            Integer lastIndex = createMessageIn.getElectricityIndices().getLastIndex();
            Integer currentIndex = createMessageIn.getElectricityIndices().getCurrentIndex();
            Integer consume = currentIndex - lastIndex;
            Double total  = consume * Double.valueOf(electricityItem.getUtilityFee());
            electricityFee  = total;
            StringBuilder builder = new StringBuilder(electricityItem.getUtilityName()).append(": ");
            builder.append(currentIndex).append(" - ").append(lastIndex);
            builder.append(" = ").append(consume);
            builder.append(" x ").append(electricityItem.getUtilityFee());
            builder.append(" = ").append(total).append("\n");
            return builder.toString();
        }
        return StringUtils.EMPTY;
    }

    private UtilityInRoomItem findUtility(UtilityId utilityId) throws IllegalArgumentException {
        for (UtilityInRoomItem item : readAvailableUtilityOut.getUtilityInRoomItemList()) {
            if (item.getUtilityId().equals(utilityId)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy tiện ích " + utilityId.name());
    }

    public MessageBuilder setCreateMessageIn(CreateMessageIn createMessageIn) {
        this.createMessageIn = createMessageIn;
        return this;
    }

    public MessageBuilder setReadAvailableUtilityOut(ReadAvailableUtilityOut readAvailableUtilityOut) {
        this.readAvailableUtilityOut = readAvailableUtilityOut;
        return this;
    }

    public MessageBuilder setSelectedCurrency(Currency selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
        return this;
    }
}
