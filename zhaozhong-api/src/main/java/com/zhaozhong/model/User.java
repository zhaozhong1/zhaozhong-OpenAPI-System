package com.zhaozhong.model;

import lombok.Data;

@Data
public class User {

    String username;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }
}
