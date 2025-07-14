package com.mandar.SpringRestApi.util.constants.account;

public enum Authority {
    READ,
    WRITE,
    UPDATE,
    USER, // User can update delete self object and read all objects
    ADMIN // Can read update delete any object
}
