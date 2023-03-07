package com.test.hibernate6bug.model.lazybug;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.Set;

@Entity
public class EntityGroup extends EntityNode{

    /** The privilege application children for hierarchical privilege application (groups and privileges). */
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent_id")
    private Set<EntityNode> children;

    @Column(name = "groupName")
    private String groupName;

    public Set<EntityNode> getChildren() {
        return children;
    }

    public void setChildren(Set<EntityNode> children) {
        this.children = children;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
