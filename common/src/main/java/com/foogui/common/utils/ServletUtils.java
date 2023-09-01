package com.foogui.common.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletUtils {
    public static void setResponseToZip(HttpServletResponse response, byte[] data, String zipName) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName + ".zip");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
    }
}
