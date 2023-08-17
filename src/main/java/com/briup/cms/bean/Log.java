package com.briup.cms.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/22 17:21
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("cms_log")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 访问用户账号
     */
    private String username;

    /**
     * 请求的方式，get post delete put
     */
    private String requestMethod;

    /**
     * 请求的地址
     */
    private String requestUri;

    /**
     * 请求的时间
     */
    private LocalDateTime logTime;
}
