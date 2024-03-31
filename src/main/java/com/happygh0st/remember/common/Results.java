package com.happygh0st.remember.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Results {
    private String code;
    private Map<String, Object> data;

    public Results() {
        this.data = new HashMap<String, Object>();
    }

    public static Results StatusOk() {
        Results results = new Results();
        results.setCode("200");
        return results;
    }

    public static Results StatusErr() {
        Results results = new Results();
        results.setCode("400");
        return results;
    }

    public static Results setCodes(String code) {
        Results results = new Results();
        results.setCode(code);
        return results;
    }

    public Results addData(String key, Object value) {
        data.put(key, value);
        return this;
    }
}
