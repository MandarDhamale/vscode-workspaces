package com.mandar.spring_web_template_integration.util.constants;

public enum Roles {

    ADMIN("ROLE_ADMIN"), EDITOR("ROLE_EDITOR"), USER("ROLE_USER");

    private final String role;

    private Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

}
