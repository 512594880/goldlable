package com.hitales.goldlable.Tools;


import com.hitales.goldlable.Entity.JSONResult;

/**
 * Created by wangxi on 18/8/7.
 * 统一处理实体工具类
 */
public class ResultUtil {

    public static JSONResult success(Object object){
        JSONResult result = new JSONResult();
        result.setCode(0);
        result.setMsg("Success");
        result.setData(object);
        return result;
    }

    public static JSONResult success(){
        return success(null);
    }

    public static  JSONResult error(Integer code,String msg){
        JSONResult result = new JSONResult();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
