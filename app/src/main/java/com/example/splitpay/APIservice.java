package com.example.splitpay;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIservice {
    @POST("/new-user/")
    Call<UserResponse> createUser(@Body UserRequest userRequest);
    @GET("/users/")
    Call<List<UserResponse>> getUsers();
    @GET("/users/{user_id}/transactions/")
    Call<List<TransactionResponse>> getalltransactions(@Path("user_id") int userId);
    @GET("/users/{user_id}/splits/")
    Call<List<SplitResponse>> getallsplits(@Path("user_id") int userId);
    @POST("/users/{user_id}/new-split/")
    Call<SplitResponse> createSplit(@Path ("user_id") int userId, @Body SplitRequest splitRequest);
    @POST("/users/{user_id}/new-transaction/")
    Call<TransactionResponse> createTransaction(@Path ("user_id") int userId, @Body TransactionRequest transactionRequest);
    @POST("/verify-password/")
    Call<VerificationResponse> verifyPassword(@Body VerificationRequest verificationRequest);

}

