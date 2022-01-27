package com.canvas.controllers;

import com.canvas.utils.structs.Folder;
import com.canvas.controllers.http.HttpResponse;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.*;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.*;

public class FolderController extends Controller {
    // 一部のコースではフォルダを取得する権利がない
    public List<Folder> getFolders(String canvasUrl, String token, int courseId) {
        List<Folder> list = new ArrayList<>();
        HttpResponse httpResponse;
        Type folderList = new TypeToken<List<Folder>>() {}.getType();
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        URL url;
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        Integer perPage = 100;
        params.put("per_page", perPage.toString());

        try {
            url = new URL(canvasUrl + "/api/v1/courses/" + courseId + "/folders");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        do {
            try {
                httpResponse = run(Method.GET, url, token, params);
                url = httpResponse.getLink().get("next");
                list.addAll(gson.fromJson(httpResponse.getBody(), folderList));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } while (Objects.nonNull(url));

        return list;
    }

    public List<Folder> getFoldersInFolder(URL foldersUrl, String token) {
        List<Folder> list = new ArrayList<>();
        HttpResponse httpResponse;
        Type folderList = new TypeToken<List<Folder>>() {}.getType();
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        URL url = foldersUrl;
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        Integer perPage = 100;
        params.put("per_page", perPage.toString());

        do {
            try {
                httpResponse = run(Method.GET, url, token, params);
                url = httpResponse.getLink().get("next");
                list.addAll(gson.fromJson(httpResponse.getBody(), folderList));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } while (Objects.nonNull(url));

        return list;
    }
}
