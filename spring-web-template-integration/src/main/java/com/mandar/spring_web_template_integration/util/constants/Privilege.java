package com.mandar.spring_web_template_integration.util.constants;


public enum Privilege {
    RESET_ANY_USER_PASSWORD(1l, "RESET_ANY_USER_PASSWORD"),
    ACCESS_ADMIN_PANEL(2l, "ACCESS_ADMIN_PANEL");

    private Long Id;
    private String privilegeString;

    private Privilege(Long id, String privilegeString) {
        this.Id = id;
        this.privilegeString = privilegeString;
    }

    public Long getId() {
        return Id;
    }

    public String getPrivilegeString() {
        return privilegeString;
    }





}
