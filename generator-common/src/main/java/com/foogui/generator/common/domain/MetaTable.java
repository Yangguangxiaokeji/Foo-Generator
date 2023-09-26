package com.foogui.generator.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaTable {
    private String tableName;
    private String tableComment;
    private List<MetaColumn> columns;
}