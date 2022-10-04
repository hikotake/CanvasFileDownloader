// MIT License

// Copyright(c)2020 Will St.Onge,Trevor Sears,Will Sie,Trey Regruth

// Permission is hereby granted,free of charge,to any person obtaining a copy of this software and associated documentation files(the"Software"),to deal in the Software without restriction,including without limitation the rights to use,copy,modify,merge,publish,distribute,sublicense,and/or sell copies of the Software,and to permit persons to whom the Software is furnished to do so,subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED"AS IS",WITHOUT WARRANTY OF ANY KIND,EXPRESS OR IMPLIED,INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,DAMAGES OR OTHER LIABILITY,WHETHER IN AN ACTION OF CONTRACT,TORT OR OTHERWISE,ARISING FROM,OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

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
