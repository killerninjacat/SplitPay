package com.example.splitpay;

public class TransactionResponse {
    private String title;
    private int description;
    private int id;
    private int owner_id;

    public TransactionResponse(String title, int description, int id, int owner_id) {
        this.title = title;
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
