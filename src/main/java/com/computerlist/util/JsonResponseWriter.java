package com.computerlist.util;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonResponseWriter {

    // Для Map
    public static void write(HttpServletResponse response, Map<String, Object> data) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        JSONObject json = new JSONObject(data);
        response.getWriter().write(json.toString());
    }

    // Для списка объектов
    public static void writeJson(HttpServletResponse response, List<?> data) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        JSONArray jsonArray = new JSONArray();

        for (Object obj : data) {
            JSONObject jsonObject = new JSONObject(obj);
            jsonArray.put(jsonObject);
        }

        response.getWriter().write(jsonArray.toString());
    }
}
