package org.example.ceylontraditionalmedicinecenter.entity;

public enum Role {
    ROLE_ADMIN,
    ROLE_USER;

    public static Role fromSimpleName(String name) {
        if (name == null) return ROLE_USER;
        String normalized = name.trim().toUpperCase();
        if (normalized.startsWith("ROLE_")) {
            return valueOf(normalized);
        }
        return valueOf("ROLE_" + normalized);
    }

    public String getSimpleName() {
        return name().substring("ROLE_".length());
    }
}
