package com.striveonger.study.api.leaf.core;

public enum Status {
    SUCCESS,
    EXCEPTION;

    public static boolean success(ID id) {
        return id != null && Status.SUCCESS.equals(id.getStatus());
    }

    public static boolean exception(ID id) {
        return id == null || Status.EXCEPTION.equals(id.getStatus());
    }
}
