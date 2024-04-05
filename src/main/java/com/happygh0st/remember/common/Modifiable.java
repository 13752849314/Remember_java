package com.happygh0st.remember.common;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Modifiable {
    boolean value() default true;

    String pattern() default "";

    Class<?> type() default String.class;
}
