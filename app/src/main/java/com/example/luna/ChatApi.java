package com.example.luna;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ChatApi {
    @POST("/chat")
    Call<AiResponse> getAiResponse(@Body UserMessage message);

    @GET("/history")
    Call<List<chatMessage>> getChatHistory();
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

