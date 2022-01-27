package com.canvas.controllers;

import okhttp3.*;
import com.canvas.controllers.http.HttpResponse;
import com.google.gson.Gson;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Base class for all controllers in this library.
 */
public class Controller {
    protected static HttpResponse run(Method method, URL endpoint, String token, LinkedHashMap<String, String> params) throws IOException {
        OkHttpClient client = new OkHttpClient(); // Client
        MediaType JSON = MediaType.get("application/json; charset=utf-8"); // JSON param type
        String json = new Gson().toJson(params); // Convert params to JSON
        Request request; // Initialize request
        HttpUrl.Builder url = HttpUrl.parse(endpoint.toString()).newBuilder(); // URL Builder
        Request.Builder builder = new Request.Builder().addHeader("Authorization", "Bearer " + token); // Request
                                                                                                       // Builder
        RequestBody body = RequestBody.create(json, JSON);

        // Build url depending on HTTP method
        if (method.getName() == "PUT" || method.getName() == "POST")
            builder.url(endpoint);
        else {
            if (params != null)
                params.forEach((k, v) -> url.addQueryParameter(k, v));
            builder.url(url.build());
        }

        switch (method.getName()) {
            case "GET":
                builder.get();
                break;
            case "POST":
                builder.post(body);
                break;
            case "PUT":
                builder.put(body);
                break;
            case "DELETE":
                builder.delete();
                break;
        }

        request = builder.build();

        // Call endpoint and return response's body
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful())
                return new HttpResponse(response.header("link"), response.body().string());
            else
                throw new IOException("API call failed with code: " + response.code() + " and body: " + response.body().string());
        }
    }
}

/**
 * Contains all HTTP methods used in the Canvas API
 */
enum Method {
    GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");

    private final String name;

    Method(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
