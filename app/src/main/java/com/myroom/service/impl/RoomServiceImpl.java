package com.myroom.service.impl;

import android.content.Context;

import com.myroom.core.Assert;
import com.myroom.database.dao.GuestDAO;
import com.myroom.database.dao.RoomDAO;
import com.myroom.database.dao.RoomUtilityDAO;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.model.Guest;
import com.myroom.model.Room;
import com.myroom.service.IRoomService;
import com.myroom.service.sdo.CreateRoomIn;
import com.myroom.service.sdo.CreateRoomOut;
import com.myroom.service.sdo.DeleteRoomIn;
import com.myroom.service.sdo.DeleteRoomOut;

public class RoomServiceImpl implements IRoomService {

    private RoomDAO roomDAO;
    private GuestDAO guestDAO;
    private RoomUtilityDAO roomUtilityDAO;

    public RoomServiceImpl(Context context) {
        roomDAO = new RoomDAO(context);
        guestDAO = new GuestDAO(context);
        roomUtilityDAO = new RoomUtilityDAO(context);
    }

    @Override
    public CreateRoomOut createRoom(CreateRoomIn createRoomIn) throws ValidationException, OperationException {
        Assert.assertNotNull(createRoomIn, "CreateRoomIn must not be null.");
        Assert.assertNotNull(createRoomIn.getRoomName(), "CreateRoomIn.RoomName must not be null.");
        Assert.assertNotNull(createRoomIn.getGuestName(), "CreateRoomIn.GuestName must not be null.");
        Assert.assertNotNull(createRoomIn.getUtilityIdList(), "CreateRoomIn.UtilityIdList must not be null.");

        long roomId = roomDAO.add(new Room(createRoomIn.getRoomName()));
        if (roomId <= 0) {
            throw new OperationException("Could not create room.");
        }

        long guestId = guestDAO.add(buildGuestObject(createRoomIn, roomId));
        if (guestId <= 0) {
            throw new OperationException("Could not create guest.");
        }

        for (Long utilityId : createRoomIn.getUtilityIdList()) {
            long roomUtilityId = roomUtilityDAO.addRoomUtility(roomId, utilityId, 0D);
            if (roomUtilityId <= 0) {
                throw new OperationException("Could not create utility in room");
            }
        }

        CreateRoomOut createRoomOut = new CreateRoomOut();
        createRoomOut.setRoomId(roomId);
        return createRoomOut;
    }

    @Override
    public DeleteRoomOut deleteRoom(DeleteRoomIn deleteRoomIn) throws ValidationException, OperationException {
        Assert.assertNotNull(deleteRoomIn, "DeleteRoomIn must not be null.");
        Assert.assertNotNull(deleteRoomIn.getRoomIdList(), "DeleteRoomIn.RoomIdList must not be null.");

        DeleteRoomOut deleteRoomOut = new DeleteRoomOut();
        for (Long roomId : deleteRoomIn.getRoomIdList()) {
            boolean deletedUtilitySuccess = roomUtilityDAO.deleteRoomUtilityByRoomId(roomId);
            if (deletedUtilitySuccess) {
                throw new OperationException("Error occurred while deleting Utility in Room");
            }
            boolean deletedRoomSuccess = roomDAO.delete(roomId);
            if (deletedRoomSuccess) {
                throw new OperationException("Error occurred while delete Room.");
            }
            deleteRoomOut.getAfftectedRoomId().add(roomId);
        }
        return deleteRoomOut;
    }

    private Guest buildGuestObject(CreateRoomIn createRoomIn, long roomId) {
        Guest guest = new Guest();
        guest.setGuestName(createRoomIn.getGuestName());
        guest.setBirthDate(createRoomIn.getGuestBirthDate());
        guest.setIdCard(createRoomIn.getGuestIdCard());
        guest.setPhoneNumber(createRoomIn.getGuestPhoneNumber());
        guest.setRoomId(roomId);
        return guest;
    }

}
