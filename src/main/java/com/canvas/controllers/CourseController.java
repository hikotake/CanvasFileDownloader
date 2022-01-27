package com.canvas.controllers;

import com.canvas.controllers.http.HttpResponse;
import com.canvas.utils.structs.Course;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class CourseController extends Controller {
    public List<Course> getCourses(String canvasUrl, String token) {
        List<Course> list = new ArrayList<>();
        HttpResponse httpResponse;
        Type courseList = new TypeToken<List<Course>>() {}.getType();
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        URL url;
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        Integer perPage = 100;
        params.put("per_page", perPage.toString());

        try {
            url = new URL(canvasUrl + "/api/v1/courses/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        do {
            try {
                httpResponse = run(Method.GET, url, token, params);
                url = httpResponse.getLink().get("next");
                list.addAll(gson.fromJson(httpResponse.getBody(), courseList));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } while (Objects.nonNull(url));

        list.removeIf(course -> Objects.isNull(course.getName()));

        list.parallelStream().forEach(course -> {
            course.formatName();
        });

        return list;
    }
}
