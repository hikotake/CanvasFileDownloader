package com.canvas.jsonManager.structs;

public class DownloadLocation {
    private String downloadLocation;

    public DownloadLocation(String path) {
        this.downloadLocation = path;
    }

    public String getDownloadLocation() {
        return this.downloadLocation;
    }

    public void setDownloadLocation(String DownloadLocation) {
        this.downloadLocation = DownloadLocation;
    }
}
