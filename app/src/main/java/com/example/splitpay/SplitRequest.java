package com.example.splitpay;

public class SplitRequest {
    private String title;
    private int description;

    public SplitRequest(String title, int description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }
}

