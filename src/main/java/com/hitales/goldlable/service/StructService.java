package com.hitales.goldlable.service;

import com.alibaba.fastjson.JSONObject;
import com.hitales.goldlable.Entity.OneStructEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by wangxi on 18/9/5.
 */
@Service
@FeignClient(url = "${structUrl}",name="struct-client")
public interface StructService {
    @PostMapping(value = "/show/elmaterial/one")
    JSONObject doStruct(@RequestBody OneStructEntity oneStructEntity);
}
