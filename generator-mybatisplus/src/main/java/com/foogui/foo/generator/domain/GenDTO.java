package com.foogui.foo.generator.domain;

import com.foogui.foo.generator.util.ValidGroup;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class GenDTO {

    /**
     * 项目名称(不传默认为"gen-code")
     */
    private String projectName = "gen-code";

    /**
     * 表前缀（可选），按需添加,默认t_
     */
    private String prefix = "t_";

    /**
     * 业务名称(不传默认为"XX"),建议添加
     */
    private String bizName = "XX";

    /**
     * 包路径，建议添加
     */
    @NotNull(message = "包名不能为空",groups = {ValidGroup.Database.class})
    private String packageName = "com.foogui";

    /**
     * 作者
     */
    private String author = "FooGui";

    /**
     * 数据库host
     */
    @NotNull(message = "host不能为空",groups = {ValidGroup.Database.class})
    private String host;
    /**
     * 端口
     */
    @NotNull(message = "port不能为空",groups = {ValidGroup.Database.class})
    private String port;
    /**
     * 数据库名
     */
    @NotNull(message = "dbname不能为空",groups = {ValidGroup.Database.class})
    private String dbname;
    /**
     * 用户名
     */
    @NotNull(message = "username不能为空",groups = {ValidGroup.Database.class})
    private String username;
    /**
     * 密码
     */
    @NotNull(message = "password不能为空",groups = {ValidGroup.Database.class})
    private String password;

    /**
     * 表名集合
     */
    @NotEmpty(message = "表名必填",groups = {ValidGroup.Database.class})
    private List<String> tableNames;


    private String ddl;

    public DataBase getDataBase() {
        return DataBase.builder()
                .host(host)
                .port(port)
                .dbname(dbname)
                .username(username)
                .password(password).build();
    }


}
