package com.foogui.generator.common.utils;

import com.foogui.generator.common.constant.CommonConstant;
import com.foogui.generator.common.domain.DataBase;
import com.foogui.generator.common.domain.Table;
import com.foogui.generator.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 模板工具类
 *
 * @author ruoyi
 */
public class VelocityUtils {
    /**
     * 项目空间路径
     */
    private static final String PROJECT_PATH = "src/main/java";

    /**
     * mybatis空间路径
     */
    private static final String MYBATIS_PATH = "src/main/resources/mapper";

    /**
     * yaml路径
     */
    private static final String YAMl_PATH = "src/main/resources";


    /**
     * 设置模板变量信息
     *
     * @return 模板列表
     */
    public static VelocityContext prepareContext(Table table) {
        // 数据准备
        String moduleName = table.getModuleName();
        String packageName = table.getPackageName();
        String functionName = table.getFunctionName();
        DataBase dataBase = table.getDataBase();

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("tableName", table.getTableName());
        velocityContext.put("functionName", functionName);
        velocityContext.put("ClassName", table.getClassName());
        velocityContext.put("className", StringUtils.uncapitalize(table.getClassName()));
        velocityContext.put("moduleName", moduleName);
        velocityContext.put("projectName", table.getProjectName());
        velocityContext.put("packageName", packageName);
        velocityContext.put("author", table.getAuthor());
        velocityContext.put("datetime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        velocityContext.put("pkColumn", table.getPk());
        velocityContext.put("pkColumnName", table.getPk().getColumnName());
        velocityContext.put("columns", table.getColumns());
        velocityContext.put("table", table);

        // 设置数据库的相关信息，供yaml中使用
        velocityContext.put("url", dataBase.getHost());
        velocityContext.put("port", dataBase.getPort());
        velocityContext.put("dbname", dataBase.getDbname());
        velocityContext.put("username", dataBase.getUsername());
        velocityContext.put("password", dataBase.getPassword());

        // 设置BaseResult和PageResult路径
        velocityContext.put("baseResultPath", CommonConstant.BASE_RESULT_PATH);
        velocityContext.put("pageResultPath", CommonConstant.PAGE_RESULT_PATH);
        velocityContext.put("pageRequestPath", CommonConstant.PAGE_REQUEST_PATH);
        return velocityContext;
    }


    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    public static List<String> getTemplateList(String frameType) {
        List<String> templates = new ArrayList<>();
        // 通用模板
        templates.add("vm/java/controller.java.vm");
        templates.add("vm/java/dto.java.vm");
        templates.add("vm/java/vo.java.vm");
        templates.add("vm/xml/mapper.xml.vm");
        // 按类型选择
        if (CommonConstant.MYBATIS.equals(frameType)){
            templates.add("vm/java/mybatis/domain.java.vm");
            templates.add("vm/java/mybatis/mapper.java.vm");
            templates.add("vm/java/mybatis/service.java.vm");
            templates.add("vm/java/mybatis/serviceImpl.java.vm");
        } else if (CommonConstant.MYBATIS_PLUS.equals(frameType)) {
            templates.add("vm/java/mybatisplus/domain.java.vm");
            templates.add("vm/java/mybatisplus/mapper.java.vm");
            templates.add("vm/java/mybatisplus/service.java.vm");
            templates.add("vm/java/mybatisplus/serviceImpl.java.vm");
        }else {
            throw new BizException("frameType只能传递Mybatis或MybatisPlus");
        }
        // pom
        templates.add("vm/pom/pom.xml.vm");
        // common
        templates.add("vm/common/BaseResult.java.vm");
        templates.add("vm/common/PageRequest.java.vm");
        templates.add("vm/common/PageResult.java.vm");
        // boot
        templates.add("vm/boot/Application.java.vm");
        templates.add("vm/boot/application.yaml.vm");
        return templates;
    }


    /**
     * 根据模板生成的文件
     *
     * @param template 模板
     * @param table    表
     * @return {@link String}
     */
    public static String getFileName(String template, Table table) {
        // 文件名称
        String fileName = "";
        // 包路径
        String packageName = table.getPackageName();
        // 模块名
        String moduleName = table.getModuleName();
        // 大写类名
        String className = table.getClassName();

        String projectName = table.getProjectName();
        String javaPath = projectName + "/" + PROJECT_PATH + "/" + StringUtils.replace(packageName, ".", "/") + "/" + moduleName;
        String applicationPath = projectName + "/" + PROJECT_PATH + "/" + StringUtils.replace(packageName, ".", "/");
        String mapperXmlPath = projectName + "/" + MYBATIS_PATH + "/" + moduleName;
        String yamlPath = projectName + "/" + YAMl_PATH;

        if (template.contains("domain.java.vm")) {
            fileName = javaPath + "/domain/" + className + "PO.java";
        } else if (template.contains("mapper.java.vm")) {
            fileName = javaPath + "/mapper/" + className + "Mapper.java";
        } else if (template.contains("service.java.vm")) {
            fileName = javaPath + "/service/" + className + "Service.java";
        } else if (template.contains("serviceImpl.java.vm")) {
            fileName = javaPath + "/service/impl/" + className + "ServiceImpl.java";
        } else if (template.contains("controller.java.vm")) {
            fileName = javaPath + "/controller/" + className + "Controller.java";
        } else if (template.contains("mapper.xml.vm")) {
            fileName = mapperXmlPath + "/" + className + "Mapper.xml";
        } else if (template.contains("dto.java.vm")) {
            fileName = javaPath + "/dto/" + className + "DTO.java";
        } else if (template.contains("vo.java.vm")) {
            fileName = javaPath + "/vo/" + className + "VO.java";
        } else if (template.contains("pom.xml.vm")) {
            fileName = projectName + "/" + "pom.xml";
        }
        // common
        else if (template.contains("BaseResult.java.vm")) {
            String BaseResultPath = projectName + "/" + PROJECT_PATH + "/" + StringUtils.replace(CommonConstant.BASE_RESULT_PATH, ".", "/");
            fileName = BaseResultPath + "/" + "BaseResult.java";
        } else if (template.contains("PageResult.java.vm")) {
            String pageResultPath = projectName + "/" + PROJECT_PATH + "/" + StringUtils.replace(CommonConstant.PAGE_RESULT_PATH, ".", "/");
            fileName = pageResultPath + "/" + "PageResult.java";
        } else if (template.contains("PageRequest.java.vm")) {
            String pageResultPath = projectName + "/" + PROJECT_PATH + "/" + StringUtils.replace(CommonConstant.PAGE_REQUEST_PATH, ".", "/");
            fileName = pageResultPath + "/" + "PageRequest.java";
        }
        // boot
        else if (template.contains("Application.java.vm")) {
            fileName = applicationPath + "/" + "Application.java";
        } else if (template.contains("application.yaml.vm")) {
            fileName = yamlPath + "/" + "application.yaml";
        }
        return fileName;
    }

    /**
     * 初始化模板引擎
     */
    public static void initEngine() {
        Properties p = new Properties();
        try {
            // 加载classpath目录下的vm文件
            p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            p.setProperty("resource.loader", "class");
            // 定义字符集
            p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
            // 初始化Velocity引擎，指定配置Properties
            Velocity.init(p);
        } catch (Exception e) {
            throw new BizException(e.getMessage(),e);
        }
    }


}
