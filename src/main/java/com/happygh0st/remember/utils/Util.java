package com.happygh0st.remember.utils;

import cn.hutool.crypto.digest.DigestUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static String PasswordEncrypt(String password) {
        return DigestUtil.sha256Hex(password);
    }

    public static List<Field> getFieldsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        List<Field> fieldsWithAnnotation = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                fieldsWithAnnotation.add(field);
            }
        }
        return fieldsWithAnnotation;
    }
}

