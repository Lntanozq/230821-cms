package com.briup.cms.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.aop.Logging;
import com.briup.cms.bean.Log;
import com.briup.cms.bean.vo.LogParam;
import com.briup.cms.service.ILogService;
import com.briup.cms.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/22 18:00
 **/
@Api(tags = "日志模块")
@RestController
@RequestMapping("/auth/log")
public class LogController {

    @Autowired
    private ILogService logService;

    @ApiOperation(value = "分页+条件查询日志信息", notes = "用户名、时间范围可以为空")
    @Logging //思考：此处是否有必要加日志注解
    @PostMapping("/query")
    public Result query(@RequestBody LogParam param) {
        IPage<Log> page = logService.query(param);

        return Result.success(page);
    }

}
