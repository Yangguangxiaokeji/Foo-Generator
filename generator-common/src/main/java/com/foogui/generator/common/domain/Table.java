package com.foogui.generator.common.domain;

import lombok.Data;

import java.util.List;


/**
 * 表模型
 *
 * @author Foogui
 * @date 2023/05/11
 */
@Data
public class Table {

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 实体类名称(首字母大写)
     */
    private String className;

    /**
     * 生成的模块名
     */
    private String moduleName;

    /**
     * 作者名
     */
    private String author;

    /**
     * 业务功能
     */
    private String functionName;

    /**
     * 主键列字段
     */
    private Column pk;


    /**
     * 其他列字段
     */
    private List<Column> columns;

    /**
     * 项目名称
     */
    private String projectName="gen-code";

    /**
     * 包名
     */
    private String packageName;

    /**
     * 数据库信息
     */
    private DataBase dataBase;
}
