package com.taoxinyu.mianshibao.mapper;

import com.taoxinyu.mianshibao.model.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
* @author 31943
* @description 针对表【question(题目)】的数据库操作Mapper
* @createDate 2025-09-07 11:17:53
* @Entity com.taoxinyu.mianshibao.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 查询题目列表（包括已被删除的数据）
     */
    @Select("select * from question where updateTime >= #{minUpdateTime}")
    List<Question> listQuestionWithDelete(Date minUpdateTime);

}




