package com.example.splitpay;

import java.util.List;

public class UserResponse {
    private String email;
    private int id;
    private boolean is_active;
    private List<Object> transactions;

    public UserResponse(String email,String hashed_password, int id, boolean is_active, List<Object> transactions) {
        this.email = email;
        this.id = id;
        this.is_active = is_active;
        this.transactions = transactions;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public List<Object> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Object> transactions) {
        this.transactions = transactions;
    }
}
