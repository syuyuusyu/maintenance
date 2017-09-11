package com.bzh.cloud.maintenance.entity;

import java.io.Serializable;
import java.util.List;

public interface TreeEntity extends Serializable {

    public Integer getId();

    public Integer getParentId();

    public String getText();

    public String getLeaf();

    public <T> List<T> getChild( Class<T> clzz);


}
