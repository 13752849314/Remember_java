package com.happygh0st.remember.common;

import lombok.Getter;

@Getter
public enum Role {
    USER("user"), ADMIN("admin"), ADMINS("admins");

    private final String value;

    Role(String admins) {
        this.value = admins;
    }

}
