package com.canvas.utils.structs;

public class Course {
    private int id;
    private String name;
    private boolean iswhitelisted = false;

    public void formatName() {
        // 全角括弧内を消す
        this.name = this.name.replaceAll("\\（.*?\\）", "").replaceAll("\\[", "(").replaceAll("\\]", ")");
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean getIswhitelisted() {
        return this.iswhitelisted;
    }

    public void setIswhitelisted(boolean iswhitelisted) {
        this.iswhitelisted = iswhitelisted;
    }
}
