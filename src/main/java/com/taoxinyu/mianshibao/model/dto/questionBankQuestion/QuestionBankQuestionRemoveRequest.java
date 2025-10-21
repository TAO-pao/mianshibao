package com.taoxinyu.mianshibao.model.dto.questionBankQuestion;

import lombok.Data;

import java.io.Serializable;
@Data
/**
 * 移除题目题库关联请求
 */
public class QuestionBankQuestionRemoveRequest implements Serializable {
    /**
     * 题库id
     */
    private Long questionBankId;

    /**
     * 题目id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;

}
