package com.example.caretail;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class chats extends Fragment {

    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private MessageAdapter adapter;
    private List<Message> messageList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        messageInput = view.findViewById(R.id.messageInput);
        ImageButton sendButton = view.findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecyclerView.setAdapter(adapter);

        // Hide bottom nav when fragment opens
        View bottomNav = getActivity().findViewById(R.id.btm_nav);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.GONE);
        }

        sendButton.setOnClickListener(v -> {
            String text = messageInput.getText().toString().trim();
            if (!text.isEmpty()) {
                messageList.add(new Message("You", text));
                adapter.notifyItemInserted(messageList.size() - 1);
                chatRecyclerView.scrollToPosition(messageList.size() - 1);
                messageInput.setText("");

                // Show bottom nav after sending message
                if (bottomNav != null) {
                    bottomNav.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }
}
