package com.kob.backend.service.record;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

public interface GetRecordListService {
    JSONObject getRecordList(Integer page);//获取第几页信息
}
