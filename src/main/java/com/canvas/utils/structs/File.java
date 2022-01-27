package com.canvas.utils.structs;

import java.net.URL;
import java.nio.file.Path;
import com.google.gson.annotations.SerializedName;

public class File {
    private int id;
    private int folderId;
    private String displayName;
    @SerializedName("filename")
    private String fileName;
    private URL url;
    private int size;
    private Path downloadLocation;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFolderId() {
        return this.folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public URL getUrl() {
        return this.url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Path getDownloadLocation() {
        return this.downloadLocation;
    }

    public void setDownloadLocation(Path path) {
        this.downloadLocation = path;
    }
}
