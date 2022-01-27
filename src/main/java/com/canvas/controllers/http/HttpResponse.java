package com.canvas.controllers.http;

import java.util.*;
import java.net.*;

public class HttpResponse {
    public LinkedHashMap<String, URL> link = new LinkedHashMap<String, URL>();
    public String body;

    public HttpResponse(String link, String body) {
        this.link = parseLinkInHeaders(link);
        this.body = body;
    }

    private static LinkedHashMap<String, URL> parseLinkInHeaders(String linkInHeaders) {
        if(Objects.isNull(linkInHeaders))
            return null;

        linkInHeaders = linkInHeaders.replaceAll("[<>]", "");
        LinkedHashMap<String, URL> map = new LinkedHashMap<String, URL>();
        List<String> list = new ArrayList<String>(Arrays.asList(linkInHeaders.split(",")));
        for (String str : list) {
            String[] tmp = str.split(";");
            // rel="****"
            String key = tmp[1].split("=")[1].replace("\"", "");
            try {
                URL url = new URL(tmp[0]);
                map.put(key, url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public LinkedHashMap<String, URL> getLink() {
        return this.link;
    }

    public String getBody() {
        return this.body;
    }
}
