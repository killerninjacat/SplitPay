package com.example.splitpay;

public class VerificationResponse {
    private boolean password_correct;

    public VerificationResponse(boolean password_correct) {
        this.password_correct = password_correct;
    }

    public boolean isPassword_correct() {
        return password_correct;
    }

    public void setPassword_correct(boolean password_correct) {
        this.password_correct = password_correct;
    }
}
