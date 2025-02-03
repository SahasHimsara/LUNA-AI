package com.example.luna;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private EditText editTextMessage;
    private Button buttonSend;
    private List<String> messageList;
    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        // Initialize message list and adapter
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);


        // Initialize Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("https://6d92-81-101-99-13.ngrok-free.app")  // Use your Flask server URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Handle send button click
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();

                // Error handling for null or empty messages
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(MainActivity.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(message);
                }
            }
        });
    }

    private void sendMessage(String message) {
        // Add the message to the list and notify the adapter
        messageList.add("👤 : " + message);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);

        // Clear the input field
        editTextMessage.setText("");

        // Call the AI API
        ChatApi chatApi = retrofit.create(ChatApi.class);
        Call<AiResponse> call = chatApi.getAiResponse(new UserMessage(message));

        call.enqueue(new Callback<AiResponse>() {
            @Override
            public void onResponse(Call<AiResponse> call, Response<AiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String aiResponse = response.body().getResponse();
                    messageList.add("🤖 : " + aiResponse);
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<AiResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to get AI response", Toast.LENGTH_SHORT).show();
            }
        });

        // Simulate a response from AI
        //simulateAIResponse();
    }

    private void simulateAIResponse() {
        String aiResponse = "🤖 : Thanks for your message!";
        messageList.add(aiResponse);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }
}
