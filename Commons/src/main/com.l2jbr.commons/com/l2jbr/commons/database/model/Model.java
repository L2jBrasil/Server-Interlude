package com.l2jbr.commons.database.model;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

public abstract class Model<ID> implements Persistable<ID> {

    @Transient
    protected boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void onSave() {
        isNew = false;
    }

    public void onLoad() {
        isNew = false;
    }

    public boolean isPersisted() {
        return !isNew;
    }

}
