package com.pragma.plazoleta.infrastructure.util.constants;

public class SecurityConstants {

    private SecurityConstants() {}

    public static final String HAS_ADMIN = "hasAuthority('ADMIN')";
    public static final String HAS_CLIENT = "hasAuthority('CLIENT')";
    public static final String HAS_EMPLOYEE = "hasAuthority('EMPLOYEE')";
    public static final String HAS_OWNER = "hasAuthority('OWNER')";
}
