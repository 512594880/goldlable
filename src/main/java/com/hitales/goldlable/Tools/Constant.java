package com.hitales.goldlable.Tools;

/**
 * Created by wangxi on 18/9/4.
 */
public class Constant {
    public static final String  TYPE_DRUG = "drug";
    public static final String  TYPE_DIAG = "diag";
    public static final String  TYPE_TEST = "test";
    public static final String  TYPE_SYMPTOM = "symptom";
    public static final String [] DrugShare = {"医院","科室/病种","患者（PID）","病例（RID）","锚点","上下文","备注"};

    public static final String [] DrugShareMethodName = {"setHospital","setDepartments","setPatientId","setRecordId","setAnchor","setContext","setRemark"};

}
