package com.hitales.goldlable.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Created by wangxi on 18/9/10.
 */
@Data
public class StructStatusResult {

    @Id
    private String id;

    private String recordId;

    private String service;

    private String sn;

    private String type;

    private String boxId;

    private String result;

    private long time;

    private int sendTime;
}
