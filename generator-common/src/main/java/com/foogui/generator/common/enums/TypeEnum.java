package com.foogui.generator.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段类型，javaType和jdbcType的约束
 * @author Foogui
 * @date 2023/09/01
 */
@AllArgsConstructor
@Getter
public enum TypeEnum {

    BIGINT("bigint","Long","BIGINT"),

    BIGINT_UNSIGNED("bigint unsigned","Long","BIGINT"),

    BINARY("binary","byte[]","BINARY"),

    BIT("bit","Boolean","BIT"),

    BLOB("blob","byte[]","LONGVARBINARY"),

    CHAR("char","String","VARCHAR"),

    DATE("date","Date","DATE"),

    DATETIME("datetime","Date","TIMESTAMP"),

    DECIMAL("decimal","BigDecimal","DECIMAL"),

    DECIMAL_UNSIGNED("decimal unsigned","BigDecimal","DECIMAL"),

    DOUBLE("double","Double","DOUBLE"),

    DOUBLE_UNSIGNED("double unsigned","Double","DOUBLE"),

    FLOAT("float","Float","REAL"),

    FLOAT_UNSIGNED("float unsigned","Float","REAL"),

    INT("int","Integer","INTEGER"),

    INT_UNSIGNED("int unsigned","Integer","INTEGER"),

    SMALLINT("smallint","Short","SMALLINT"),

    TEXT("text","String","LONGVARCHAR"),

    TIME("time","Date","TIME"),

    TIMESTAMP("timestamp","Date","TIMESTAMP"),

    TINYINT("tinyint","Integer","TINYINT"),

    TINYINT_UNSIGNED("tinyint unsigned","Integer","TINYINT"),

    TINYTEXT("tinytext","String","VARCHAR"),

    VARCHAR("varchar","String","VARCHAR");

    private final String columnType;

    private final String javaType;

    private final String jdbcType;

    public static String getJavaType(String columnType) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.columnType.equals(columnType)) {
                return typeEnum.javaType;
            }
        }
        return null;
    }

    public static String getJdbcType(String columnType) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.columnType.equals(columnType)) {
                return typeEnum.jdbcType;
            }
        }
        return null;
    }

}
