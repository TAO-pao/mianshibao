package com.taoxinyu.mianshibao.model.dto.postfavour;

import com.taoxinyu.mianshibao.model.dto.post.PostQueryRequest;
import com.taoxinyu.mianshibao.common.PageRequest;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子收藏查询请求
 *
 * @author <a href="https://github.com/litaoxinyu">北理陶鑫宇</a>
 * @from <a href="https://taoxinyu.icu">北理工自动化</a>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostFavourQueryRequest extends PageRequest implements Serializable {

    /**
     * 帖子查询请求
     */
    private PostQueryRequest postQueryRequest;

    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}