package com.myroom.service;

import com.myroom.database.dao.Utility;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.sdo.CreateUtilityInRoomIn;
import com.myroom.service.sdo.DeleteUtilityInRoomIn;
import com.myroom.service.sdo.ReadUtilityInRoomIn;
import com.myroom.service.sdo.ReadUtilityInRoomOut;
import com.myroom.service.sdo.UpdateUtilityInRoomIn;

import java.util.List;

public interface IUtilityService {
    long readUtilityKey(String utilityId) throws ValidationException, OperationException;
    String readUtilityId(long utilityKey) throws ValidationException, OperationException;
    void deleteUtilityInRoom(DeleteUtilityInRoomIn deleteUtilityInRoomIn) throws ValidationException, OperationException;
    ReadUtilityInRoomOut readUtilityInRoom(ReadUtilityInRoomIn readUtilityInRoomIn) throws ValidationException, OperationException;
    void createUtilityInRoom(CreateUtilityInRoomIn createUtilityInRoomIn) throws ValidationException, OperationException;
    void updateUtilityInRoom(UpdateUtilityInRoomIn updateUtilityInRoomIn) throws ValidationException, OperationException;
    List<Utility> readAllAvailableUtility();
}
