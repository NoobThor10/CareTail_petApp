package com.example.caretail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chats extends Fragment {

    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private ImageButton sendButton;
    private MessageAdapter adapter;
    private List<Message> messageList;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Send button click
        sendButton.setOnClickListener(v -> {
            String text = messageInput.getText().toString().trim();
            if (!text.isEmpty()) {
                Map<String, Object> chatMsg = new HashMap<>();
                chatMsg.put("sender", "You");
                chatMsg.put("message", text);
                chatMsg.put("timestamp", new Timestamp(new java.util.Date()));  // FIXED

                db.collection("PawTalkMessages")
                        .add(chatMsg)
                        .addOnSuccessListener(documentReference ->
                                Log.d("Firestore", "Message sent with ID: " + documentReference.getId()))
                        .addOnFailureListener(e ->
                                Log.e("Firestore", "Error sending message", e));

                messageInput.setText("");
            }
        });

        // Load messages from Firestore
        db.collection("PawTalkMessages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("Firestore", "Listen failed.", error);
                        return;
                    }
                    if (value == null) {
                        Log.e("Firestore", "Snapshot is null.");
                        return;
                    }

                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            String sender = dc.getDocument().getString("sender");
                            String message = dc.getDocument().getString("message");
                            Timestamp timestamp = dc.getDocument().getTimestamp("timestamp");

                            if (sender == null || message == null || timestamp == null) {
                                Log.e("Firestore", "Null value in message data.");
                                continue;
                            }

                            long timeInMillis = timestamp.toDate().getTime();
                            messageList.add(new Message(sender, message, timeInMillis));
                            adapter.notifyItemInserted(messageList.size() - 1);
                            chatRecyclerView.scrollToPosition(messageList.size() - 1);
                        }
                    }
                });


        return view;
    }
}
