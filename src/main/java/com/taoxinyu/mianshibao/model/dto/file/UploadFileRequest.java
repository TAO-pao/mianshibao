package com.taoxinyu.mianshibao.model.dto.file;

import java.io.Serializable;
import lombok.Data;

/**
 * 文件上传请求
 *
 * @author <a href="https://github.com/liTAO-pao">北理陶鑫宇</a>
 * @from <a href="https://TAO-pao.icu">北理工自动化</a>
 */
@Data
public class UploadFileRequest implements Serializable {

    /**
     * 业务
     */
    private String biz;

    private static final long serialVersionUID = 1L;
}