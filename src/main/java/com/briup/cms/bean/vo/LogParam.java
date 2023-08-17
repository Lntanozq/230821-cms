package com.briup.cms.bean.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/22 17:31
 **/
@Data
public class LogParam {
    private Integer pageNum;
    private Integer pageSize;

    private String userName;

    //日志时间
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
