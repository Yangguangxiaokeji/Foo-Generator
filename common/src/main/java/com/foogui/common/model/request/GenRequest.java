package com.foogui.common.model.request;

import com.foogui.common.domain.DataBase;
import com.foogui.common.validation.ValidGroup;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class GenRequest {

    /**
     * 项目名称（可选）
     */
    private String projectName = "gen-code";

    /**
     * 表前缀（可选）
     */
    private String prefix = "t_";

    /**
     * 业务名称（可选）
     */
    private String bizName = "XX";

    /**
     * 包路径（必填）
     */
    @NotNull(message = "包名不能为空", groups = {ValidGroup.Database.class})
    private String packageName;

    /**
     * 作者（可选）
     */
    private String author = "FooGui";

    /**
     * 数据库host（必填）
     */
    @NotNull(message = "host不能为空", groups = {ValidGroup.Database.class})
    private String host;

    /**
     * 端口（必填）
     */
    @NotNull(message = "port不能为空", groups = {ValidGroup.Database.class})
    private String port;

    /**
     * 数据库名（必填）
     */
    @NotNull(message = "dbname不能为空", groups = {ValidGroup.Database.class})
    private String dbname;

    /**
     * 用户名（必填）
     */
    @NotNull(message = "username不能为空", groups = {ValidGroup.Database.class})
    private String username;

    /**
     * 密码（必填）
     */
    @NotNull(message = "password不能为空", groups = {ValidGroup.Database.class})
    private String password;

    /**
     * 表名集合（必填）
     */
    @NotEmpty(message = "表名必填", groups = {ValidGroup.Database.class})
    private List<String> tableNames;


    private String ddl;

    public DataBase getDataBase() {
        return DataBase.builder()
                .host(host)
                .port(port)
                .dbname(dbname)
                .username(username)
                .password(password)
                .build();
    }


}
