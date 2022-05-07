package com.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author wanghaoquan
 * @date 2021/11/29
 */
public class TestUtils {
    public static JSONObject getTestArg(String path) {
        String s = new JSONReader(new BufferedReader(new InputStreamReader(TestUtils.class.getResourceAsStream(path)))).readString();
        return JSONObject.parseObject(s);
    }

    public static JSONArray getTestArray(String path) {
        String s = new JSONReader(new BufferedReader(new InputStreamReader(TestUtils.class.getResourceAsStream(path)))).readString();
        return JSONArray.parseArray(s);
    }
}