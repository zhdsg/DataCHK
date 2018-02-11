package com.hg.chk.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2018/2/6/006.
 */
public class JSONUtils {
    public static String getString(String json,String key){
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString(key);
    }

    public static Integer getInteger(String json,String key){
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getInteger(key);
    }

}
