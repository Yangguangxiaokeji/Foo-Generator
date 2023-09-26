package com.foogui.generator.common.domain;

import lombok.Data;


/**
 * 列字段模型
 *
 * @author wangxin
 * @date 2023/09/25
 */
@Data
public class Column {

    /**
     * 字段名称（db）
     */
    private String columnName;

    /**
     * 字段描述（db）
     */
    private String columnComment;

    /**
     * 字段类型（db）
     */
    private String columnType;

    /**
     * java类型
     */
    private String javaType;

    /**
     * java字段
     */
    private String javaField;

    /**
     * jdbc类型
     */
    private String jdbcType;

}