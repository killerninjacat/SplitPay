package com.example.splitpay;

public class SplitResponse {
    private String title,names;
    private int description;
    private int id;
    private int owner_id;

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public SplitResponse(String title, String names, int description, int id, int owner_id) {
        this.title = title;
        this.names=names;
        this.description = description;
        this.id = id;
        this.owner_id = owner_id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }
}
