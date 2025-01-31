package com.example.luna;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatApi {
    @POST("/chat")
    Call<AiResponse> getAiResponse(@Body UserMessage message);
}

class UserMessage {
    private String message;

    public UserMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

class AiResponse {
    private String response;

    public String getResponse() {
        return response;
    }
}