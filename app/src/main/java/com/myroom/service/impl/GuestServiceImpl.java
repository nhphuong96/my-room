package com.myroom.service.impl;

import com.myroom.application.BaseApplication;
import com.myroom.core.Assert;
import com.myroom.database.dao.Guest;
import com.myroom.database.repository.GuestRepository;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.IGuestService;
import com.myroom.service.sdo.CreateGuestIn;
import com.myroom.service.sdo.DeleteGuestIn;
import com.myroom.service.sdo.DeleteGuestOut;
import com.myroom.service.sdo.ReadGuestInRoomIn;
import com.myroom.service.sdo.ReadGuestInRoomOut;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GuestServiceImpl implements IGuestService {

    @Inject
    public GuestRepository guestRepository;

    @Inject
    public GuestServiceImpl() {
        BaseApplication.getRepositoryComponent(BaseApplication.getContextComponent().getContext()).inject(this);
    }

    @Override
    public void createGuest(CreateGuestIn createGuestIn) throws ValidationException, OperationException {
        Assert.assertNotNull(createGuestIn, "createGuestIn must not be null.");
        Assert.assertNotNull(createGuestIn.getRoomId(), "createGuestIn.roomId must not be null.");
        Assert.assertNotNull(createGuestIn.getGuestName(), "createGuestIn.guestName must not be null.");
        Assert.assertNotNull(createGuestIn.getGuestPhone(), "createGuestIn.guestPhone must not be null.");

        long id = guestRepository.add(buildGuest(createGuestIn));
        if (id <= 0) {
            throw new OperationException("Không thể tạo khách phòng.");
        }
    }

    @Override
    public ReadGuestInRoomOut readGuestInRoom(ReadGuestInRoomIn readGuestInRoomIn) throws ValidationException, OperationException {
        Assert.assertNotNull(readGuestInRoomIn, "readGuestInRoomIn must not be null.");
        Assert.assertNotNull(readGuestInRoomIn.getRoomId(), "readGuestInRoomIn.roomId must not be null.");

        List<Guest> guestInRoomList = guestRepository.findGuestByRoomId(readGuestInRoomIn.getRoomId());
        ReadGuestInRoomOut readGuestInRoomOut = new ReadGuestInRoomOut();
        readGuestInRoomOut.setGuestList(guestInRoomList);
        return readGuestInRoomOut;
    }

    @Override
    public DeleteGuestOut deleteGuest(DeleteGuestIn deleteGuestIn) throws ValidationException, OperationException {
        Assert.assertNotNull(deleteGuestIn, "deleteGuestIn must not be null.");
        Assert.assertNotNull(deleteGuestIn.getGuestIdList(), "deleteGuestIn.guestIdList must not be null.");
        DeleteGuestOut deleteGuestOut = new DeleteGuestOut();
        deleteGuestOut.setDeletedIdList(new ArrayList<Long>());
        for (Long id : deleteGuestIn.getGuestIdList()) {
            boolean delete = guestRepository.delete(id);
            if (delete) {
                deleteGuestOut.getDeletedIdList().add(id);
            }
        }
        return deleteGuestOut;
    }

    private Guest buildGuest(CreateGuestIn createGuestIn) {
        Guest guest = new Guest();
        guest.setGuestName(createGuestIn.getGuestName());
        guest.setPhoneNumber(createGuestIn.getGuestPhone());
        guest.setGender(createGuestIn.getGender());
        guest.setIdCard(createGuestIn.getGuestIdCard());
        guest.setBirthDate(createGuestIn.getGuestBirthday());
        guest.setRoomId(createGuestIn.getRoomId());
        return guest;
    }
}
