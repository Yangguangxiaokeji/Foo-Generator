package com.foogui.foo.generator.service.impl;


import com.foogui.foo.generator.service.GenService;
import com.foogui.generator.common.constant.CommonConstant;
import com.foogui.generator.common.domain.*;
import com.foogui.generator.common.enums.TypeEnum;
import com.foogui.generator.common.model.request.GenRequest;
import com.foogui.generator.common.utils.JDBCUtils;
import com.foogui.generator.common.utils.SqlParseUtils;
import com.foogui.generator.common.utils.StringUtils;
import com.foogui.generator.common.utils.VelocityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
@Slf4j
public class GenServiceImpl implements GenService {

    @Override
    public byte[] doCreateCodeByDDL(GenRequest dto) {
        String frameType = dto.getFrameType();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        doGeneratorCode(prepareTableByDDL(dto), zip,frameType);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    @Override
    public byte[] doCreateCodeBatch(GenRequest dto) {
        String frameType = dto.getFrameType();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : dto.getTableNames()) {
            Table table = new Table();
            prepareTableByMetaInfo(table,tableName, dto);
            doGeneratorCode(table, zip,frameType);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }


    /**
     * 根据表的元数据准备Table
     *
     * @param tableName
     * @param dto
     * @return {@link Table}
     */
    private Table prepareTableByMetaInfo(Table table,String tableName, GenRequest dto) {

        MetaTable metaTable = JDBCUtils.getMetaTable(tableName, dto.getDataBase());

        String className = StringUtils.removePrefixAndToCamel(tableName, dto.getPrefix());
        className = StringUtils.capitalizeFirstChar(className);
        String moduleName = StringUtils.removePrefixAndUnderline(tableName, dto.getPrefix());

        HashMap<String, List<Column>> map = getColumnsFromMetaTable(metaTable);
        table.setPk(map.get(CommonConstant.PK_COLUMN).get(0));
        table.setColumns(map.get(CommonConstant.NORMAL_COLUMN));
        table.setTableName(tableName);
        table.setProjectName(dto.getProjectName());
        table.setClassName(className);
        table.setModuleName(moduleName);
        String tableComment = metaTable.getTableComment();
        table.setFunctionName(StringUtils.isNoneBlank(tableComment) ? tableComment : dto.getBizName());
        table.setAuthor(dto.getAuthor());
        table.setPackageName(dto.getPackageName());
        table.setDataBase(DataBase.builder()
                .host(dto.getHost())
                .port(dto.getPort())
                .dbname(dto.getDbname())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build()
        );
        return table;
    }


    /**
     * 根据Table信息生成byte[]并注入到zip中
     *
     * @param table 表
     * @param zip   邮政编码
     */
    private void doGeneratorCode(Table table, ZipOutputStream zip, String frameType) {

        VelocityUtils.initEngine();

        VelocityContext context = VelocityUtils.prepareContext(table);
        // 选择Mybatis或者MybatisPlus的模板
        List<String> templates = VelocityUtils.getTemplateList(frameType);

        for (String template : templates) {
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            StringWriter stringWriter = new StringWriter();
            tpl.merge(context, stringWriter);
            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(stringWriter.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(stringWriter);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                log.error("渲染模板失败，表名：" + table.getTableName(), e);
            }
        }

    }

    /**
     * 准备表上下文
     *
     * @param dto
     */
    private Table prepareTableByDDL(GenRequest dto) {
        String ddl = dto.getDdl();

        Table table = new Table();
        // 去除sql中的飘号
        ddl = StringUtils.removeFly(ddl);

        String tableName = SqlParseUtils.getTableName(ddl);

        String projectName = dto.getProjectName();

        String className = StringUtils.removePrefixAndToCamel(tableName, dto.getPrefix());
        className = StringUtils.capitalizeFirstChar(className);
        String moduleName = StringUtils.removePrefixAndUnderline(tableName, dto.getPrefix());

        String primaryKey = SqlParseUtils.getPrimaryKey(ddl);

        // 处理Column信息
        Map<String, List<Column>> map = getColumns(ddl, primaryKey);
        table.setPk(map.get(CommonConstant.PK_COLUMN).get(0));
        table.setColumns(map.get(CommonConstant.NORMAL_COLUMN));

        table.setTableName(tableName);
        table.setProjectName(projectName);
        table.setClassName(className);
        table.setModuleName(moduleName);
        table.setFunctionName(dto.getBizName());
        table.setAuthor(dto.getAuthor());
        table.setPackageName(dto.getPackageName());
        table.setDataBase(DataBase.builder()
                .host(dto.getHost())
                .port(dto.getPort())
                .dbname(dto.getDbname())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build()
        );
        return table;
    }

    /**
     * 获得列信息进行处理
     *
     * @param ddl        ddl
     * @param primaryKey 主键
     * @return {@link ArrayList}<{@link Column}>
     * @jsqlparser例外
     */
    private HashMap<String, List<Column>> getColumns(String ddl, String primaryKey) {
        ArrayList<Column> columnList = new ArrayList<>();
        ArrayList<Column> pkColumnList = new ArrayList<>();
        List<Column> columns = SqlParseUtils.getColumns(ddl);
        for (Column column : columns) {
            if (column.getColumnName().equals(primaryKey)) {
                pkColumnList.add(column);
            } else {
                columnList.add(column);
            }
        }
        HashMap<String, List<Column>> map = new HashMap<>();
        map.put(CommonConstant.PK_COLUMN, pkColumnList);
        map.put(CommonConstant.NORMAL_COLUMN, columnList);
        return map;
    }

    private HashMap<String, List<Column>> getColumnsFromMetaTable(MetaTable metaTable) {
        ArrayList<Column> columnList = new ArrayList<>();
        ArrayList<Column> pkColumnList = new ArrayList<>();
        List<MetaColumn> columns = metaTable.getColumns();
        for (MetaColumn metaColumn : columns) {
            // Column转化
            Column column = new Column();
            String columnType = metaColumn.getDataType();
            column.setColumnName(metaColumn.getColumnName());
            column.setColumnComment(metaColumn.getColumnComment());
            column.setColumnType(columnType);
            columnType = StringUtils.lowerCase(columnType);
            column.setJavaType(TypeEnum.getJavaType(columnType));
            column.setJavaField(StringUtils.underlineToCamel(metaColumn.getColumnName()));
            column.setJdbcType(TypeEnum.getJdbcType(columnType));

            if (metaColumn.getIsPk() == 1) {
                pkColumnList.add(column);
            } else {
                columnList.add(column);
            }
        }
        HashMap<String, List<Column>> map = new HashMap<>();
        map.put(CommonConstant.PK_COLUMN, pkColumnList);
        map.put(CommonConstant.NORMAL_COLUMN, columnList);
        return map;
    }


}
