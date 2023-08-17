package com.briup.cms.bean.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/20 1:02
 **/
@Data
public class ArticleParam {
    private Integer pageNum;
    private Integer pageSize;

    private Integer categoryId;
    private String title;
    private String status;
    private Long userId;
    private Integer charged;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
