package com.taoxinyu.mianshibao.esdao;

import com.taoxinyu.mianshibao.model.dto.post.PostEsDTO;

import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 帖子 ES 操作
 *
 * @author <a href="https://github.com/liTAO-pao">北理陶鑫宇</a>
 * @from <a href="https://TAO-pao.icu">北理工自动化</a>
 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {

    List<PostEsDTO> findByUserId(Long userId);
}