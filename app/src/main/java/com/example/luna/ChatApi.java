package com.example.luna;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatApi {
    @POST("/chat")  // Endpoint for chat messages
    Call<ChatResponse> sendMessage(@Body ChatRequest request);
}

