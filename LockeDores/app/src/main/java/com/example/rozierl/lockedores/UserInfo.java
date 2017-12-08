package com.example.rozierl.lockedores;

/**
 * Created by rozierl on 12/7/17.
 */

public class UserInfo {
    private String name;
    private String email;
    private String password;

    private UserInfo(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
