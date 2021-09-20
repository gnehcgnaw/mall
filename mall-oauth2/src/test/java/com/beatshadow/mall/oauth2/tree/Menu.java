package com.beatshadow.mall.oauth2.tree;

import java.util.List;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/3/23 10:25
 */
public class Menu implements TreeEntity<Menu>{
    public String id;
    public String name;
    public String parentId;
    public List<Menu> childList;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<Menu> getChildList() {
        return childList;
    }

    @Override
    public void setChildList(List<Menu> childList) {
        this.childList = childList;
    }
    //省略set、get方法...
}