package com.foogui.foo.generator.controller;

import com.foogui.foo.generator.service.GenService;
import com.foogui.generator.common.model.request.GenRequest;
import com.foogui.generator.common.utils.FileUtils;
import com.foogui.generator.common.validation.ValidGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@RequestMapping("/gen")
@RestController
public class GenController {
    @Autowired
    private GenService genGenService;

    /**
     * 通过ddl的sql生成代码
     *
     * @param response 响应
     * @param file     文件
     * @throws IOException         ioexception
     */
    @PostMapping("/ddl")
    public void genCodeThroughDDLFile(HttpServletResponse response, @RequestParam("file") MultipartFile file, GenRequest dto) throws IOException {
        String ddl = new String(file.getBytes(), StandardCharsets.UTF_8);
        dto.setDdl(ddl);
        byte[] data = genGenService.doCreateCodeByDDL(dto);
        FileUtils.doCreateZip(response, data, dto.getProjectName());
    }


    /**
     * 通过表名批量生成代码
     *
     * @param response 响应
     * @param dto      dto
     * @throws IOException ioexception
     */
    @PostMapping("/batch")
    public void genCodeByTableNames(HttpServletResponse response, @Validated(ValidGroup.Database.class) @RequestBody GenRequest dto) throws IOException {
        byte[] data = genGenService.doCreateCodeBatch(dto);
        FileUtils.doCreateZip(response, data, dto.getProjectName());
    }
}
