package com.foogui.foo.generator.domain;

import lombok.Data;

import java.util.List;


/**
 * 表
 *
 * @author Foogui
 * @date 2023/05/11
 */
@Data
public class Table {

    private static final long serialVersionUID = 1623794836078801057L;


    /**
     * 表名称
     */
    private String tableName;

    /**
     * 实体类名称(首字母大写)
     */
    private String className;


    /**
     * 生成模块名
     */
    private String moduleName;

    /**
     * 生成作者
     */
    private String author;


    /**
     * 主键信息
     */
    private Column pk;

    /**
     * 方法功能说明
     */
    private String functionName;


    /**
     * 表列信息
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
