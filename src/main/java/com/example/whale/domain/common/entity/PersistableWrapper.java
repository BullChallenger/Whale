package com.example.whale.domain.common.entity;

import org.springframework.data.domain.Persistable;

public abstract class PersistableWrapper extends BaseEntity implements Persistable<String> {
    
    @Override
    public boolean isNew() {
        return super.getCreatedAt() == null;
    }

}
