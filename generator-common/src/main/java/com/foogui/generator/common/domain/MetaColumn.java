package com.foogui.generator.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaColumn {
    private String columnName;
    private String dataType;
    private int columnSize;
    private String columnComment;
    /**
     * 是否是主键（1：是）
     */
    private int isPk;
}
