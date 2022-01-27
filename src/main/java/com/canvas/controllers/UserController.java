package com.canvas.controllers;

import com.canvas.controllers.http.HttpResponse;
import com.canvas.utils.structs.User;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URL;

public class UserController extends Controller {
    public static User getCurrentUser(String canvasUrl, String token) {
        HttpResponse httpResponse;
        String json;
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        try {
            URL url = new URL(canvasUrl + "/api/v1/users/self");
            httpResponse = run(Method.GET, url, token, null);
            json = httpResponse.getBody();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return gson.fromJson(json, User.class);
    }
}
