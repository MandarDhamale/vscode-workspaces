package com.mandar.SpringRestApi.payload.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {

    private String email;
    private String password;

}
