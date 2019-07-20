package com.myroom.service.impl;

import com.myroom.application.BaseApplication;
import com.myroom.core.Assert;
import com.myroom.database.dao.RoomUtility;
import com.myroom.database.dao.Utility;
import com.myroom.database.repository.RoomUtilityRepository;
import com.myroom.database.repository.UtilityRepository;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.IUtilityService;
import com.myroom.service.sdo.CreateUtilityInRoomIn;
import com.myroom.service.sdo.DeleteUtilityInRoomIn;
import com.myroom.service.sdo.ReadUtilityInRoomIn;
import com.myroom.service.sdo.ReadUtilityInRoomOut;
import com.myroom.service.sdo.UpdateUtilityInRoomIn;
import com.myroom.service.sdo.UtilityInRoomItem;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class UtilityServiceImpl implements IUtilityService {

    @Inject
    public UtilityRepository utilityRepository;
    @Inject
    public RoomUtilityRepository roomUtilityRepository;

    @Inject
    public UtilityServiceImpl() {
        BaseApplication.getRepositoryComponent(BaseApplication.getContextComponent().getContext()).inject(this);
    }

    @Override
    public long readUtilityKey(String utilityId) throws ValidationException, OperationException {
        Utility utility = utilityRepository.findById(utilityId);
        return utility.getUtilityKey();
    }

    @Override
    public String readUtilityId(long utilityKey) throws ValidationException, OperationException {
        Utility utility = utilityRepository.find(utilityKey);
        return utility.getUtilityId();
    }

    @Override
    public void deleteUtilityInRoom(DeleteUtilityInRoomIn deleteUtilityInRoomIn) throws ValidationException, OperationException {
        Assert.assertNotNull(deleteUtilityInRoomIn, "deleteUtilityInRoomIn must not be null.");
        Assert.assertNotNull(deleteUtilityInRoomIn.getRoomKey(), "deleteUtilityInRoom.roomKey must not be null.");

        boolean success = roomUtilityRepository.deleteRoomUtilityByRoomKey(deleteUtilityInRoomIn.getRoomKey());
        if (!success) {
            throw new OperationException("Không thể xóa tiện ích trong phòng.");
        }
    }

    @Override
    public ReadUtilityInRoomOut readUtilityInRoom(ReadUtilityInRoomIn readUtilityInRoomIn) throws ValidationException, OperationException {
        Assert.assertNotNull(readUtilityInRoomIn, "readUtilityInRoomIn must not be null.");
        Assert.assertNotNull(readUtilityInRoomIn, "readUtilityInRoomIn.roomKey must not be null.");
        List<RoomUtility> roomUtilityList = roomUtilityRepository.findRoomUtilityByRoomKey(readUtilityInRoomIn.getRoomKey());
        ReadUtilityInRoomOut readUtilityInRoomOut = new ReadUtilityInRoomOut();
        readUtilityInRoomOut.setUtilityInRoomItemList(new ArrayList<UtilityInRoomItem>());
        if (CollectionUtils.isNotEmpty(roomUtilityList)) {
            for (RoomUtility rUtility: roomUtilityList) {
                Utility utility = utilityRepository.find(rUtility.getUtilityKey());
                UtilityInRoomItem utilityInRoomItem = new UtilityInRoomItem();
                utilityInRoomItem.setUtilityKey(utility.getUtilityKey());
                utilityInRoomItem.setUtilityId(utility.getUtilityId());
                utilityInRoomItem.setUtilityName(utility.getName());
                utilityInRoomItem.setUtilityFee(rUtility.getUtilityFee());
                utilityInRoomItem.setUtilityIconName(utility.getIcon());
                readUtilityInRoomOut.getUtilityInRoomItemList().add(utilityInRoomItem);
            }
        }
        return readUtilityInRoomOut;
    }

    @Override
    public void createUtilityInRoom(CreateUtilityInRoomIn createUtilityInRoomIn) throws ValidationException, OperationException {
        Assert.assertNotNull(createUtilityInRoomIn, "createUtilityInRoomIn must not be null.");
        Assert.assertNotNull(createUtilityInRoomIn.getRoomKey(), "createUtilityInRoomIn.roomKey must not be null.");
        Assert.assertNotNull(createUtilityInRoomIn.getUtilityKey(), "createUtilityInRoomIn.utilityKey must not be null.");
        long id = roomUtilityRepository.addRoomUtility(createUtilityInRoomIn.getRoomKey(), createUtilityInRoomIn.getUtilityKey(), Double.valueOf(createUtilityInRoomIn.getFee()));
        if (id <= 0) {
            throw new OperationException("Không thể thêm tiện ích vào phòng.");
        }
    }

    @Override
    public void updateUtilityInRoom(UpdateUtilityInRoomIn updateUtilityInRoomIn) throws ValidationException, OperationException {
        Assert.assertNotNull(updateUtilityInRoomIn, "updateUtilityInRoomIn must not be null.");
        boolean success = roomUtilityRepository.updateRoomUtility(convertRoomUtility(updateUtilityInRoomIn));
        if (!success) {
            throw new OperationException("Không thể cập nhật phí của tiện ích.");
        }
    }

    private RoomUtility convertRoomUtility(UpdateUtilityInRoomIn updateUtilityInRoomIn) {
        RoomUtility roomUtility = new RoomUtility();
        roomUtility.setUtilityKey(updateUtilityInRoomIn.getUtilityKey());
        roomUtility.setRoomKey(updateUtilityInRoomIn.getRoomKey());
        roomUtility.setUtilityFee(updateUtilityInRoomIn.getUtilityFee());
        return roomUtility;
    }

    @Override
    public List<Utility> readAllAvailableUtility() {
        return utilityRepository.findAll();
    }
}
