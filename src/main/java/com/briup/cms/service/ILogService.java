package com.briup.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.Log;
import com.briup.cms.bean.vo.LogParam;

public interface ILogService {
    // 添加日志信息
    void save(Log log);

    // 分页+条件查询日志信息
    IPage<Log> query(LogParam param);
}
