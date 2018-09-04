package com.hitales.goldlable.Entity;

import lombok.Data;

/**
 * Created by wangxi on 18/8/7.
 * 同意错误处理实体类
 */
@Data
public class JSONResult<T> {

    private Integer code;

    private String msg;

    private T data;
}
