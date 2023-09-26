package com.foogui.generator.common.utils;

import com.foogui.generator.common.constant.CommonConstant;
import com.foogui.generator.common.domain.DataBase;
import com.foogui.generator.common.domain.MetaColumn;
import com.foogui.generator.common.domain.MetaTable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * JDBC工具类
 *
 * @author wangxin
 * @date 2023/09/26
 */
@Slf4j
public class JDBCUtils {

    @SneakyThrows
    public static MetaTable getMetaTable(String tableName, DataBase dataBase) {
        MetaTable metaTable = new MetaTable();
        List<MetaColumn> metaColumns = new ArrayList<>();
        Class.forName(CommonConstant.MYSQL_DRIVER);
        Connection conn = null;
        ResultSet rsTable = null;
        ResultSet rsColumn = null;
        String tableSchema = null;
        String primaryKey = null;
        try {
            // 创建MySQL连接
            String url = String.format("jdbc:mysql://%s:%s/%s", dataBase.getHost(), dataBase.getPort(), dataBase.getDbname());
            String user = dataBase.getUsername();
            String password = dataBase.getPassword();
            conn = DriverManager.getConnection(url, user, password);

            // 获取数据库的元数据信息
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            tableSchema = conn.getCatalog();

            // 获取表的元数据
            metaTable.setTableName(tableName);
            rsTable = databaseMetaData.getTables(conn.getCatalog(), null, null, new String[]{"TABLE", "VIEW", ""});
            if (rsTable.next()) {
                String sql = "SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '" + tableSchema + "' AND TABLE_NAME = '" + tableName + "'";
                ResultSet tableRs = conn.createStatement().executeQuery(sql);
                String tableComment = null;
                while (tableRs.next()) {
                    // 获取表注释
                    tableComment = tableRs.getString("TABLE_COMMENT");
                }
                metaTable.setTableComment(tableComment);
            }
            // 获取主键名
            String pkSql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + tableSchema + "' AND TABLE_NAME = '" + tableName + "' AND COLUMN_KEY = 'PRI'";
            ResultSet rsPk = conn.createStatement().executeQuery(pkSql);
            while (rsPk.next()) {
                primaryKey = rsPk.getString(1);
            }

            // 获取字段的元数据
            rsColumn = databaseMetaData.getColumns(tableSchema, null, tableName, null);
            while (rsColumn.next()) {
                MetaColumn metaColumn = new MetaColumn();
                String columnName = rsColumn.getString("COLUMN_NAME");
                metaColumn.setColumnName(columnName);
                metaColumn.setDataType(rsColumn.getString("TYPE_NAME"));
                metaColumn.setColumnSize(rsColumn.getInt("COLUMN_SIZE"));
                // 必须要从INFORMATION_SCHEMA才能查到字段注释，REMARKS拿不到
                String sql = "SELECT COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + tableSchema + "' AND TABLE_NAME = '" + tableName + "' AND COLUMN_NAME = '" + columnName + "'";
                ResultSet rs = conn.createStatement().executeQuery(sql);
                // 获取字段注释
                while (rs.next()) {
                    metaColumn.setColumnComment(rs.getString("COLUMN_COMMENT"));
                }
                if (columnName.equals(primaryKey)) {
                    metaColumn.setIsPk(CommonConstant.ONE);
                }


                metaColumns.add(metaColumn);
            }
            metaTable.setColumns(metaColumns);
        } catch (SQLException e) {
            log.error("There was an error connecting to the database using jdbc {}", e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (rsTable != null) {
                rsTable.close();
            }
            if (rsColumn != null) {
                rsColumn.close();
            }
        }
        return metaTable;
    }


}
