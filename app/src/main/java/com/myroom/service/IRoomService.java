package com.myroom.service;

import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.sdo.CreateRoomIn;
import com.myroom.service.sdo.CreateRoomOut;
import com.myroom.service.sdo.DeleteRoomIn;
import com.myroom.service.sdo.DeleteRoomOut;
import com.myroom.service.sdo.ReadAvailableUtilityIn;
import com.myroom.service.sdo.ReadAvailableUtilityOut;
import com.myroom.service.sdo.ReadRoomInformationIn;
import com.myroom.service.sdo.ReadRoomInformationOut;

public interface IRoomService {
    CreateRoomOut createRoom(CreateRoomIn createRoomIn) throws ValidationException, OperationException;
    ReadAvailableUtilityOut readAvailableUtility(ReadAvailableUtilityIn readAvailableUtilityIn) throws ValidationException, OperationException;
    DeleteRoomOut deleteRoom(DeleteRoomIn deleteRoomIn) throws ValidationException, OperationException;
    ReadRoomInformationOut readRoomInformation(ReadRoomInformationIn readRoomInformationIn) throws ValidationException, OperationException;
}
