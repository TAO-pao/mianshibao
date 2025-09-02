package com.taoxinyu.mianshibao.common;

import java.io.Serializable;
import lombok.Data;

/**
 * 删除请求
 *
 * @author <a href="https://github.com/liTAO-pao">北理陶鑫宇</a>
 * @from <a href="https://TAO-pao.icu">北理工自动化</a>
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}