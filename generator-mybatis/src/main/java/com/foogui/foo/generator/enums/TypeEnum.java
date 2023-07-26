package com.foogui.foo.generator.enums;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum TypeEnum {

    VARCHAR("varchar","String","VARCHAR"),
    DATE("date","Date","DATE"),
    DATETIME("datetime","Date","DATE"),
    TIMESTAMP("timestamp","Date","DATE"),
    TINYINT("tinyint","Integer","TINYINT"),
    TINYINT_UNSIGNED("tinyint unsigned","Integer","TINYINT"),
    INT("int","Integer","INTEGER"),
    INT_UNSIGNED("int unsigned","Integer","INTEGER"),
    DECIMAL("decimal","BigDecimal","DECIMAL"),
    DECIMAL_UNSIGNED("decimal unsigned","BigDecimal","DECIMAL"),
    DOUBLE("double","Double","DOUBLE"),
    DOUBLE_UNSIGNED("double unsigned","Double","DOUBLE"),
    FLOAT("float","Double","FLOAT"),
    FLOAT_UNSIGNED("float unsigned","Double","FLOAT"),
    CHAR("char","String","VARCHAR"),
    BIGINT("bigint","Long","BIGINT"),
    BIGINT_UNSIGNED("bigint unsigned","Long","BIGINT");

    private final static Table<String,String,String> TABLE= HashBasedTable.create();

    static {
        Stream.of(TypeEnum.values()).forEach(typeEnum -> {
            TABLE.put(typeEnum.getColumnType(),typeEnum.getJavaType(), typeEnum.getJdbcType());
        });
    }

    private String columnType;

    private String javaType;

    private String jdbcType;

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
