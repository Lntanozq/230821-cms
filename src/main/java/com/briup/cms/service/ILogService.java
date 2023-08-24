package com.briup.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.Log;
import com.briup.cms.bean.vo.LogExportParam;
import com.briup.cms.bean.vo.LogParam;

import java.util.List;

public interface ILogService {

    // 分页+条件查询日志信息
    IPage<Log> query(LogParam param);

    //根据条件查询待导出的数据
    List<Log> queryForExport(LogExportParam logExportParam);
}
