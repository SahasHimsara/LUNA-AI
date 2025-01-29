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
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private EditText editTextMessage;
    private Button buttonSend;
    private List<String> messageList;
    private ChatApi chatApi;

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

        // Initialize API
        chatApi = ApiClient.getClient().create(ChatApi.class);

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
        // Add the user message to the list and notify the adapter
        messageList.add("You: " + message);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);

        // Clear the input field
        editTextMessage.setText("");

        // Send message to Flask API
        ChatRequest request = new ChatRequest(message);
        Call<ChatResponse> call = chatApi.sendMessage(request);

        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messageList.add("AI: " + response.body().getResponse());
                } else {
                    messageList.add("AI: Error in response!");
                }
                chatAdapter.notifyItemInserted(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                messageList.add("AI: Failed to connect to server!");
                chatAdapter.notifyItemInserted(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);
            }
        });
    }
}
