package com.myroom.service;

import com.myroom.database.dao.Utility;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;

import java.util.List;

public interface IUtilityService {
    long readUtilityKey(String utilityId) throws ValidationException, OperationException;
    String readUtilityId(long utilityKey) throws ValidationException, OperationException;
    List<Utility> readAllAvailableUtility();
}
