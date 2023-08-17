package com.briup.cms.bean.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/22 15:25
 **/
@Data
public class CommentQueryParam {
    // 分页参数
    private Integer pageNum;
    private Integer pageSize;

    private Long userId;
    private Long articleId;
    // 关键字
    private String keyword;
    // 发表时间范围
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
