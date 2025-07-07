package com.mandar.SpringRestApi.util.constants;

public enum Authority {
    READ,
    WRITE,
    UPDATE,
    USER, // User can update delete self object and read all objects
    ADMIN // Can read update delete any object
}
