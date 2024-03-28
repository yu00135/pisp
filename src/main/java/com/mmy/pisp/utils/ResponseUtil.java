package com.mmy.pisp.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmy.pisp.result.ResultVO;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class ResponseUtil {
    public static void out(HttpServletResponse response, ResultVO result) {
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter writer = null;
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json; charset=UTF-8");
        try {
            writer = response.getWriter();
            mapper.writeValue(writer, result);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}

