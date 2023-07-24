package com.example.splitpay;

public class SplitRequest {
    private String title,names;
    private int description;

    public SplitRequest(String title,String names, int description) {
        this.title = title;
        this.description = description;
        this.names=names;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
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

