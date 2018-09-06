package com.hitales.goldlable.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.HashMap;

/**
 * Created by wangxi on 18/9/4.
 */
@Data
public class GoldLabelEntity {

    @Id
    private String id;

    private String recordId;

    private String patientId;

    private String context;

    private String type;

    private String tag;

    private String hospital;

    private String departments;

    private String anchor;

    private String remark;
    /**
     * 各实体表头结果
     */
    private HashMap<String,String> list;



}
