package com.cht.procurementManagement.enums;

public enum RequestStatus {
    PENDING_ADMIN_APPROVAL,
    REJECTED_ADMIN_APPROVAL,
    PENDING_SUPPLIES_APPROVAL,
    REJECTED_SUPPLIES_APPROVAL,
    PENDING_PROCUREMENT,
    PROCUREMENT_CREATED,

//allowed to be changed by supplies user
    PROCUREMENT_POSTPONED,
    PROCUREMENT_CANCELLED
}
