package com.test.hibernate6bug.model.lazybug;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class EntitySimple extends EntityNode{

    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
