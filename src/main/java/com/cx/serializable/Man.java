package com.cx.serializable;

import java.io.Serializable;

/**
 * @author chenxiang
 * @create 2021-11-26 10:43
 */
public class Man implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;

    public Man(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Man() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
