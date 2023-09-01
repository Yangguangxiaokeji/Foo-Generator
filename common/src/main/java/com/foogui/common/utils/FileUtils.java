package com.foogui.common.utils;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FileUtils {
    public static void doCreateZip(HttpServletResponse response, byte[] data, String zipName) throws IOException {
        ServletUtils.setResponseToZip(response, data,zipName);
        IOUtils.write(data, response.getOutputStream());
    }
}
