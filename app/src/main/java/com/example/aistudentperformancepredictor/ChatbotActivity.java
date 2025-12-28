package com.example.aistudentperformancepredictor;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatbotActivity extends AppCompatActivity {

    private RecyclerView rvChat;
    private EditText etMessage;
    private ImageButton btnSend;
    private List<MessageModel> messageList;
    private ChatAdapter chatAdapter;
    private GenerativeModelFutures model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        // 1. Gemini AI Setup (Yahan apni key lagayein)
        // "models/" hata kar sirf model ka naam likhein
        // Use this exact string without "models/" prefix
        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", "AIzaSyDNQFeWcDqluEThg5OUguRuPx6wre0qmF0");
        model = GenerativeModelFutures.from(gm);

        rvChat = findViewById(R.id.rvChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        messageList = new ArrayList<>();

        chatAdapter = new ChatAdapter(messageList);
        rvChat.setAdapter(chatAdapter);
        rvChat.setLayoutManager(new LinearLayoutManager(this));

        btnSend.setOnClickListener(v -> {
            String question = etMessage.getText().toString().trim();
            if (!question.isEmpty()) {
                addToChat(question, MessageModel.SENT_BY_ME);
                etMessage.setText("");
                askGeminiAI(question); // Real AI Call
            }
        });

        addToChat("Hello! I am your advanced AI Assistant. Ask me anything about your studies.", MessageModel.SENT_BY_BOT);
    }

    private void askGeminiAI(String question) {
        // 1. Typing message add karein
        addToChat("Typing...", MessageModel.SENT_BY_BOT);

        try {
            Content content = new Content.Builder().addText(question).build();
            ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    // Hamesha UI thread par update karne ke liye runOnUiThread use karein
                    runOnUiThread(() -> {
                        String resultText = result.getText();

                        // Safety Check: List khali na ho aur "Typing..." mojood ho
                        if (messageList != null && !messageList.isEmpty()) {
                            messageList.remove(messageList.size() - 1);
                            chatAdapter.notifyItemRemoved(messageList.size());
                        }

                        addToChat(resultText, MessageModel.SENT_BY_BOT);
                    });
                }

                @Override
                public void onFailure(Throwable t) {
                    runOnUiThread(() -> {
                        if (messageList != null && !messageList.isEmpty()) {
                            messageList.remove(messageList.size() - 1);
                            chatAdapter.notifyItemRemoved(messageList.size());
                        }
                        addToChat("AI Error: " + t.getMessage(), MessageModel.SENT_BY_BOT);
                        Log.e("GeminiError", "AI Error: ", t);
                    });
                }
            }, ContextCompat.getMainExecutor(this));

        } catch (Exception e) {
            Log.e("GeminiError", "Fatal Error: " + e.getMessage());
            addToChat("Something went wrong. Please restart the chat.", MessageModel.SENT_BY_BOT);
        }
    }

    private void addToChat(String message, String sentBy) {
        runOnUiThread(() -> {
            messageList.add(new MessageModel(message, sentBy));
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            rvChat.smoothScrollToPosition(messageList.size() - 1);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}