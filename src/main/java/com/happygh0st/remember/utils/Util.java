package com.happygh0st.remember.utils;

import cn.hutool.crypto.digest.DigestUtil;

public class Util {
    public static String PasswordEncrypt(String password) {
        return DigestUtil.sha256Hex(password);
    }
}

