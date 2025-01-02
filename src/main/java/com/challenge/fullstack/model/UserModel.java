package com.challenge.fullstack.model;

import lombok.Data;

@Data
public class UserModel {
    Integer user_id;
    String name;
    String password;
    String phone;
}
