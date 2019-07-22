package com.myroom.service.impl;

import com.myroom.application.BaseApplication;
import com.myroom.core.Assert;
import com.myroom.core.RoomInfoKey;
import com.myroom.database.repository.RoomRepository;
import com.myroom.service.sdo.DeleteUtilityInRoomIn;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.database.dao.Room;
import com.myroom.service.IGuestService;
import com.myroom.service.IPaymentService;
import com.myroom.service.IRoomService;
import com.myroom.service.IUtilityService;
import com.myroom.service.sdo.CreateUtilityInRoomIn;
import com.myroom.service.sdo.CreateGuestIn;
import com.myroom.service.sdo.CreateRoomIn;
import com.myroom.service.sdo.DeletePaymentIn;
import com.myroom.service.sdo.DeleteRoomIn;
import com.myroom.service.sdo.DeleteRoomOut;
import com.myroom.service.sdo.ReadRoomInformationIn;
import com.myroom.service.sdo.ReadRoomInformationOut;
import com.myroom.service.sdo.RoomInfo;

import java.util.ArrayList;

import javax.inject.Inject;

public class RoomServiceImpl implements IRoomService {

    @Inject public RoomRepository roomRepository;
    @Inject public IGuestService guestService;
    @Inject public IUtilityService utilityService;
    @Inject public IPaymentService paymentService;

    @Inject
    public RoomServiceImpl() {
        BaseApplication.getRepositoryComponent(BaseApplication.getContextComponent().getContext()).inject(this);
        BaseApplication.getServiceComponent(BaseApplication.getContextComponent().getContext()).inject(this);
    }

    @Override
    public void createRoom(CreateRoomIn createRoomIn) throws ValidationException, OperationException {
        Assert.assertNotNull(createRoomIn, "CreateRoomIn must not be null.");
        Assert.assertNotNull(createRoomIn.getRoomName(), "CreateRoomIn.RoomName must not be null.");
        Assert.assertNotNull(createRoomIn.getGuestName(), "CreateRoomIn.GuestName must not be null.");
        Assert.assertNotNull(createRoomIn.getUtilityKeyList(), "CreateRoomIn.UtilityKeyList must not be null.");
        long roomKey = roomRepository.add(new Room(createRoomIn.getRoomName()));
        if (roomKey <= 0) {
            throw new OperationException("Không thể tạo phòng mới.");
        }
        guestService.createGuest(convertCreateGuestIn(createRoomIn, roomKey));
        addUtilityInRoom(createRoomIn, roomKey);
    }

    @Override
    public DeleteRoomOut deleteRoom(DeleteRoomIn deleteRoomIn) throws ValidationException, OperationException {
        Assert.assertNotNull(deleteRoomIn, "DeleteRoomIn must not be null.");
        Assert.assertNotNull(deleteRoomIn.getRoomKeyList(), "DeleteRoomIn.RoomKeyList must not be null.");

        DeleteRoomOut deleteRoomOut = new DeleteRoomOut();
        for (Long roomKey : deleteRoomIn.getRoomKeyList()) {
            utilityService.deleteUtilityInRoom(convertDeleteUtilityInRoom(roomKey));
            paymentService.deletePayment(convertDeletePaymentIn(roomKey));
            boolean deletedRoomSuccess = roomRepository.delete(roomKey);
            if (!deletedRoomSuccess) {
                throw new OperationException("Lỗi xảy ra khi xóa phòng.");
            }
            deleteRoomOut.getAfftectedRoomId().add(roomKey);
        }
        return deleteRoomOut;
    }

    @Override
    public ReadRoomInformationOut readRoomInformation(ReadRoomInformationIn readRoomInformationIn) throws ValidationException, OperationException {
        Assert.assertNotNull(readRoomInformationIn, "readRoomInformationIn must not be null.");
        Assert.assertNotNull(readRoomInformationIn.getRoomId(), "readRoomInformationIn.roomId must not be null.");

        ReadRoomInformationOut readRoomInformationOut = new ReadRoomInformationOut();
        readRoomInformationOut.setRoomInfoList(new ArrayList<RoomInfo>());
        readRoomInformationOut.getRoomInfoList().add(collectRoomName(readRoomInformationIn.getRoomId()));
        return readRoomInformationOut;
    }

    private CreateUtilityInRoomIn convertAddUtilityInRoomIn(long roomKey, long utilityKey, String fee) {
        CreateUtilityInRoomIn createUtilityInRoomIn = new CreateUtilityInRoomIn();
        createUtilityInRoomIn.setRoomKey(roomKey);
        createUtilityInRoomIn.setUtilityKey(utilityKey);
        createUtilityInRoomIn.setFee(fee);
        return createUtilityInRoomIn;
    }

    private RoomInfo collectRoomName(long roomId) {
        Room room = roomRepository.find(roomId);
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.setKey(RoomInfoKey.ROOM_NAME.getDenotation());
        roomInfo.setValue(room.getRoomName());
        return roomInfo;
    }


    private void addUtilityInRoom(CreateRoomIn createRoomIn, long roomKey) throws ValidationException, OperationException {
        for (Long utilityKey : createRoomIn.getUtilityKeyList()) {
            utilityService.createUtilityInRoom(convertAddUtilityInRoomIn(roomKey, utilityKey, "0"));
        }
    }

    private DeleteUtilityInRoomIn convertDeleteUtilityInRoom(Long roomKey) {
        DeleteUtilityInRoomIn deleteUtilityInRoomIn = new DeleteUtilityInRoomIn();
        deleteUtilityInRoomIn.setRoomKey(roomKey);
        return deleteUtilityInRoomIn;
    }

    private DeletePaymentIn convertDeletePaymentIn(Long roomKey) {
        DeletePaymentIn deletePaymentIn = new DeletePaymentIn();
        deletePaymentIn.setRoomKey(roomKey);
        return deletePaymentIn;
    }

    private CreateGuestIn convertCreateGuestIn(CreateRoomIn createRoomIn, long roomKey) {
        CreateGuestIn createGuestIn = new CreateGuestIn();
        createGuestIn.setGuestName(createRoomIn.getGuestName());
        createGuestIn.setGuestPhone(createRoomIn.getGuestPhoneNumber());
        createGuestIn.setGuestIdCard(createRoomIn.getGuestIdCard());
        createGuestIn.setGuestBirthday(createRoomIn.getGuestBirthDate());
        createGuestIn.setRoomKey(roomKey);
        createGuestIn.setGender(createRoomIn.getGender());
        return createGuestIn;
    }

}
