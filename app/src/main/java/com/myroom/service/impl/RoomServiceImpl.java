package com.myroom.service.impl;

import com.myroom.application.BaseApplication;
import com.myroom.core.Assert;
import com.myroom.core.RoomInfoKey;
import com.myroom.database.dao.RoomUtility;
import com.myroom.database.dao.Utility;
import com.myroom.database.repository.RoomUtilityRepository;
import com.myroom.database.repository.GuestRepository;
import com.myroom.database.repository.RoomRepository;
import com.myroom.database.repository.UtilityRepository;
import com.myroom.dto.UtilityInRoomItem;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.database.dao.Guest;
import com.myroom.database.dao.Room;
import com.myroom.service.IRoomService;
import com.myroom.service.IUtilityService;
import com.myroom.service.sdo.CreateRoomIn;
import com.myroom.service.sdo.CreateRoomOut;
import com.myroom.service.sdo.DeleteRoomIn;
import com.myroom.service.sdo.DeleteRoomOut;
import com.myroom.service.sdo.ReadAvailableUtilityIn;
import com.myroom.service.sdo.ReadAvailableUtilityOut;
import com.myroom.service.sdo.ReadRoomInformationIn;
import com.myroom.service.sdo.ReadRoomInformationOut;
import com.myroom.service.sdo.RoomInfo;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RoomServiceImpl implements IRoomService {

    @Inject public RoomRepository roomRepository;
    @Inject public GuestRepository guestRepository;
    @Inject public UtilityRepository utilityRepository;
    @Inject public RoomUtilityRepository roomUtilityRepository;
    @Inject public IUtilityService utilityService;

    @Inject
    public RoomServiceImpl() {
        BaseApplication.getRepositoryComponent(BaseApplication.getContextComponent().getContext()).inject(this);
        BaseApplication.getServiceComponent(BaseApplication.getContextComponent().getContext()).inject(this);
    }

    @Override
    public CreateRoomOut createRoom(CreateRoomIn createRoomIn) throws ValidationException, OperationException {
        Assert.assertNotNull(createRoomIn, "CreateRoomIn must not be null.");
        Assert.assertNotNull(createRoomIn.getRoomName(), "CreateRoomIn.RoomName must not be null.");
        Assert.assertNotNull(createRoomIn.getGuestName(), "CreateRoomIn.GuestName must not be null.");
        Assert.assertNotNull(createRoomIn.getUtilityIdList(), "CreateRoomIn.UtilityIdList must not be null.");

        long roomId = roomRepository.add(new Room(createRoomIn.getRoomName()));
        if (roomId <= 0) {
            throw new OperationException("Could not create room.");
        }

        long guestId = guestRepository.add(buildGuestObject(createRoomIn, roomId));
        if (guestId <= 0) {
            throw new OperationException("Could not create guest.");
        }

        for (Long utilityId : createRoomIn.getUtilityIdList()) {
            long roomUtilityId = roomUtilityRepository.addRoomUtility(roomId, utilityId, 0);
            if (roomUtilityId <= 0) {
                throw new OperationException("Could not create utility in room");
            }
        }

        CreateRoomOut createRoomOut = new CreateRoomOut();
        createRoomOut.setRoomId(roomId);
        return createRoomOut;
    }

    @Override
    public ReadAvailableUtilityOut readAvailableUtility(ReadAvailableUtilityIn readAvailableUtilityIn) throws ValidationException, OperationException {
        Assert.assertNotNull(readAvailableUtilityIn, "ReadAvailableUtilityIn must not be null.");
        Assert.assertNotNull(readAvailableUtilityIn.getRoomKey(), "ReadAvailableUtilityIn.roomId must not be null.");

        ReadAvailableUtilityOut readAvailableUtilityOut = new ReadAvailableUtilityOut();
        readAvailableUtilityOut.setUtilityInRoomItemList(new ArrayList<UtilityInRoomItem>());

        //Read all utilities in room
        List<RoomUtility> roomUtilityList = roomUtilityRepository.findRoomUtilityByRoomId(readAvailableUtilityIn.getRoomKey());
        if (CollectionUtils.isNotEmpty(roomUtilityList)) {
            for (RoomUtility rUtility: roomUtilityList) {
                Utility utility = utilityRepository.find(rUtility.getUtilityKey());
                UtilityInRoomItem utilityInRoomItem = new UtilityInRoomItem();
                utilityInRoomItem.setUtilityKey(utility.getUtilityKey());
                utilityInRoomItem.setUtilityId(utility.getUtilityId());
                utilityInRoomItem.setUtilityName(utility.getName());
                utilityInRoomItem.setUtilityFee(rUtility.getUtilityFee());
                utilityInRoomItem.setUtilityIconName(utility.getIcon());
                readAvailableUtilityOut.getUtilityInRoomItemList().add(utilityInRoomItem);
            }
        }
        return readAvailableUtilityOut;
    }

    @Override
    public DeleteRoomOut deleteRoom(DeleteRoomIn deleteRoomIn) throws ValidationException, OperationException {
        Assert.assertNotNull(deleteRoomIn, "DeleteRoomIn must not be null.");
        Assert.assertNotNull(deleteRoomIn.getRoomKeyList(), "DeleteRoomIn.RoomKeyList must not be null.");

        DeleteRoomOut deleteRoomOut = new DeleteRoomOut();
        for (Long roomId : deleteRoomIn.getRoomKeyList()) {
            boolean deletedUtilitySuccess = roomUtilityRepository.deleteRoomUtilityByRoomId(roomId);
            if (!deletedUtilitySuccess) {
                throw new OperationException("Error occurred while deleting Utility in Room");
            }
            boolean deletedRoomSuccess = roomRepository.delete(roomId);
            if (!deletedRoomSuccess) {
                throw new OperationException("Error occurred while delete Room.");
            }
            deleteRoomOut.getAfftectedRoomId().add(roomId);
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

    private RoomInfo collectRoomName(long roomId) {
        Room room = roomRepository.find(roomId);
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.setKey(RoomInfoKey.ROOM_NAME.getDenotation());
        roomInfo.setValue(room.getRoomName());
        return roomInfo;
    }

    private Guest buildGuestObject(CreateRoomIn createRoomIn, long roomId) {
        Guest guest = new Guest();
        guest.setGuestName(createRoomIn.getGuestName());
        guest.setBirthDate(createRoomIn.getGuestBirthDate());
        guest.setIdCard(createRoomIn.getGuestIdCard());
        guest.setPhoneNumber(createRoomIn.getGuestPhoneNumber());
        guest.setRoomId(roomId);
        guest.setGender(createRoomIn.getGender());
        return guest;
    }

}
