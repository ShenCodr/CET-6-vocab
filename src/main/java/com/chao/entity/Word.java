package com.chao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tb_word")
public class Word {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String spelling;
    private String meaning;
    private String exampleSentence;
    private Integer status;
    private Integer userId;
    private LocalDateTime createTime;
}

