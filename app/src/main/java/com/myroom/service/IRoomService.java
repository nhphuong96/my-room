package com.myroom.service;

import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.sdo.CreateRoomIn;
import com.myroom.service.sdo.CreateRoomOut;
import com.myroom.service.sdo.DeleteRoomIn;
import com.myroom.service.sdo.DeleteRoomOut;

public interface IRoomService {
    CreateRoomOut createRoom(CreateRoomIn createRoomIn) throws ValidationException, OperationException;
    DeleteRoomOut deleteRoom(DeleteRoomIn deleteRoomIn) throws ValidationException, OperationException;
}
