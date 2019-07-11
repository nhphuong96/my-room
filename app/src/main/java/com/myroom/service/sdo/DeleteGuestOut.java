package com.myroom.service.sdo;

import java.util.List;

public class DeleteGuestOut {
    private List<Long> deletedIdList;

    public List<Long> getDeletedIdList() {
        return deletedIdList;
    }

    public void setDeletedIdList(List<Long> deletedIdList) {
        this.deletedIdList = deletedIdList;
    }
}
