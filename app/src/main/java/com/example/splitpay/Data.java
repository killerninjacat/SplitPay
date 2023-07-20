package com.example.splitpay;

import java.util.List;

public class Data {
    List<TransactionResponse> transactions;

    public List<TransactionResponse> getTransactions() {
        return transactions;
    }

    public Data(List<TransactionResponse> transactions) {
        this.transactions = transactions;
    }

    public void setTransactions(List<TransactionResponse> transactions) {
        this.transactions = transactions;
    }
}
