package com.canvas.controllers;

import com.canvas.utils.structs.*;
import com.canvas.controllers.http.HttpResponse;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.*;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class FileController extends Controller {
    public List<File> getFiles(String canvasUrl, String token, int courseId) throws IOException {
        List<File> list = new ArrayList<>();
        HttpResponse httpResponse;
        Type fileList = new TypeToken<List<File>>() {}.getType();
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        URL url;
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        Integer perPage = 100;
        params.put("per_page", perPage.toString());

        try {
            url = new URL(canvasUrl + "/api/v1/courses/" + courseId + "/files");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        do {
            try {
                httpResponse = run(Method.GET, url, token, params);
                url = httpResponse.getLink().get("next");
                list.addAll(gson.fromJson(httpResponse.getBody(), fileList));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } while (Objects.nonNull(url));

        return list;
    }

    public List<File> getFilesInFolder(URL filesUrl, String token) {
        List<File> list = new ArrayList<>();
        HttpResponse httpResponse;
        Type fileList = new TypeToken<List<File>>() {}.getType();
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        URL url = filesUrl;
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        Integer perPage = 100;
        params.put("per_page", perPage.toString());

        do {
            try {
                httpResponse = run(Method.GET, url, token, params);
                url = httpResponse.getLink().get("next");
                list.addAll(gson.fromJson(httpResponse.getBody(), fileList));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } while (Objects.nonNull(url));

        return list;
    }

    // https://www.baeldung.com/java-download-file 参考
    public void downloadFileAsync(File file) {
        try (BufferedInputStream in = new BufferedInputStream(file.getUrl().openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(file.getDownloadLocation().toString())) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            System.out.println("Download Complete: " + file.getDownloadLocation());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
