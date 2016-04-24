package com.example.exerciseapp.model;

import java.util.List;

/**
 * User: lyjq(1752095474)
 * Date: 2016-04-22
 */
public class OrganizeList extends ErrorMsg {
    List<OrganizeName> data;

    public List<OrganizeName> getData() {
        return data;
    }

    public void setData(List<OrganizeName> data) {
        this.data = data;
    }
}
