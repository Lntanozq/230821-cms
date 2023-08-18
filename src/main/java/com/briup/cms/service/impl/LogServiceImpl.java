package com.briup.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.bean.Log;
import com.briup.cms.bean.vo.LogParam;
import com.briup.cms.dao.LogDao;
import com.briup.cms.exception.ServiceException;
import com.briup.cms.service.ILogService;
import com.briup.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/22 17:34
 **/
@Service
public class LogServiceImpl implements ILogService {

    @Autowired
    private LogDao logDao;

    @Override
    public void save(Log log) {
        if(log == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);

        // 设置日志写入时间
        log.setCreateTime(LocalDateTime.now());
        logDao.insert(log);
    }

    @Override
    public IPage<Log> query(LogParam param) {
        // 1.参数判断
        if(param == null || param.getPageNum() == null || param.getPageSize() == null)
            throw new ServiceException(ResultCode.PARAM_IS_BLANK);

        // 2.查询条件准备，按时间倒序
        IPage<Log> page = new Page<>(param.getPageNum(), param.getPageSize());
        String userName = param.getUserName();
        LocalDateTime startTime = param.getStartTime();
        LocalDateTime endTime = param.getEndTime();
        LambdaQueryWrapper<Log> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userName != null, Log::getUsername, userName)
               .le(endTime != null, Log::getCreateTime, endTime)
               .ge(startTime != null, Log::getCreateTime, startTime)
               .orderByDesc(Log::getCreateTime);

        // 3.执行分页查询
        logDao.selectPage(page, wrapper);

        return page;
    }
}
