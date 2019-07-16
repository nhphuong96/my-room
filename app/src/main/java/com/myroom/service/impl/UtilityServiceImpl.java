package com.myroom.service.impl;

import com.myroom.application.BaseApplication;
import com.myroom.database.dao.Utility;
import com.myroom.database.repository.UtilityRepository;
import com.myroom.exception.OperationException;
import com.myroom.exception.ValidationException;
import com.myroom.service.IUtilityService;

import java.util.List;

import javax.inject.Inject;

public class UtilityServiceImpl implements IUtilityService {

    @Inject
    public UtilityRepository utilityRepository;

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
    public List<Utility> readAllAvailableUtility() {
        return utilityRepository.findAll();
    }
}
