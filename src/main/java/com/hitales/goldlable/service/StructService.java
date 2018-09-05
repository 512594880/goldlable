package com.hitales.goldlable.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wangxi on 18/9/5.
 */

@FeignClient(url = "${structUrl}",name="struct-client")
public interface StructService {
    @PostMapping(value = "/show/elmaterial/one")
    JSONObject doStruct(@RequestParam("patientId") String patientId, @RequestParam("recordId") String recordId, @RequestParam("text")String text);
}
