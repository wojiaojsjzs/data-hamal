package com.striveonger.study.api.leaf.core;

import java.io.Serializable;

public class ID implements Serializable {
    private long id;
    private Status status;

    public ID() {

    }
    public ID(long id, Status status) {
        this.id = id;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
