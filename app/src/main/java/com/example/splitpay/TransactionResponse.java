package com.example.splitpay;

public class TransactionResponse {
    private String title;
    private int description;
    private int id;
    private int owner_id;
    private boolean settled;

    public boolean isSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
    }

    public TransactionResponse(String title, int description, int id, int owner_id, boolean settled) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.owner_id = owner_id;
        this.settled=settled;
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
