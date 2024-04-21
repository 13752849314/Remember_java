package com.happygh0st.remember.common;

import lombok.Getter;

@Getter
public enum Role {
    USER("user"), ADMIN("admin"), ADMINS("admins");

    private final String value;

    Role(String admins) {
        this.value = admins;
    }

    public boolean ge(String value) {
        if (value.equals(ADMINS.getValue())) return true;
        if (this.value.equals(value)) return true;
        return value.equals(ADMIN.getValue()) && this.value.equals(USER.getValue());
    }

    public static boolean ge(String controller, String controlled) {
        if (controller.equals(ADMINS.getValue())) return true;
        return controller.equals(ADMIN.getValue()) && controlled.equals(USER.getValue());
    }

}
