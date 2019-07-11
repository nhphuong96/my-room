package com.myroom.service;

import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.sdo.CreateGuestIn;
import com.myroom.service.sdo.DeleteGuestIn;
import com.myroom.service.sdo.DeleteGuestOut;
import com.myroom.service.sdo.ReadGuestInRoomIn;
import com.myroom.service.sdo.ReadGuestInRoomOut;

public interface IGuestService {
    void createGuest(CreateGuestIn createGuestIn) throws ValidationException, OperationException;
    ReadGuestInRoomOut readGuestInRoom(ReadGuestInRoomIn readGuestInRoomIn) throws ValidationException, OperationException;
    DeleteGuestOut deleteGuest(DeleteGuestIn deleteGuestIn) throws ValidationException, OperationException;
}
