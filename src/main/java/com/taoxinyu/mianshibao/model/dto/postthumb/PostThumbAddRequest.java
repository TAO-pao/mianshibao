package com.taoxinyu.mianshibao.model.dto.postthumb;

import java.io.Serializable;
import lombok.Data;

/**
 * 帖子点赞请求
 *
 * @author <a href="https://github.com/litaoxinyu">北理陶鑫宇</a>
 * @from <a href="https://taoxinyu.icu">北理工自动化</a>
 */
@Data
public class PostThumbAddRequest implements Serializable {

    /**
     * 帖子 id
     */
    private Long postId;

    private static final long serialVersionUID = 1L;
}