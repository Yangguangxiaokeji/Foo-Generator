package com.foogui.generator.common.utils;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FileUtils {
    /**
     * 将byte[]写入zip
     *
     * @param response 回答
     * @param data     数据
     * @param zipName  zip名称
     * @throws IOException IOException
     */
    public static void doCreateZip(HttpServletResponse response, byte[] data, String zipName) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName + ".zip");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
