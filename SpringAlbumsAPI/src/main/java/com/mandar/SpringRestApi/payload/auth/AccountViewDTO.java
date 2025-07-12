package com.mandar.SpringRestApi.payload.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountViewDTO {

    private long id;
    private String email;
    private String authorities;
}
