package com.example.splitpay;

public class TransactionRequest {
    private String title;
    private int description;
    private boolean settled;

    public boolean isSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
    }

    public TransactionRequest(String title, int description, boolean settled) {
        this.title = title;
        this.description = description;
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
}

