package com.canvas.utils.structs;

import java.net.URL;

public class Folder {
    private URL filesUrl;
    private String fullName;
    private int id;
    private String name;
    private int parentFolderId;

    public void replaceFullName(String courseName) {
        this.fullName = this.fullName.replace("course files", courseName);
    }

    public URL getFilesUrl() {
        return this.filesUrl;
    }

    public void setFilesUrl(URL filesUrl) {
        this.filesUrl = filesUrl;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentFolderId() {
        return this.parentFolderId;
    }

    public void setParentFolderId(int parentFolderId) {
        this.parentFolderId = parentFolderId;
    }
}
