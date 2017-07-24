package com.phengtola.entities;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by tolapheng on 7/21/17.
 */
public class Role implements GrantedAuthority{

    private int id;
    private String name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String getAuthority() {
        return name;
    }
}

