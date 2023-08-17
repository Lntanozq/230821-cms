package com.briup.cms.bean.extend;

import com.briup.cms.bean.Subcomment;
import com.briup.cms.bean.User;
import lombok.Data;
import lombok.ToString;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/23 10:28
 **/
@Data
public class SubCommentExtend extends Subcomment {
    // 评论人
    private User author;

}
