package com.example.parstagram31;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.parstagram31.Adapter.ChatAdapter;
import com.example.parstagram31.Adapter.CommentAdapter;
import com.example.parstagram31.Models.Message;
import com.example.parstagram31.Models.directs;
import com.example.parstagram31.databinding.ActivityDmactivityBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DMActivity extends AppCompatActivity {

    private static final String TAG = "DM activity";
    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    ActivityDmactivityBinding binding;
    private List<Message> msgs;
    private boolean mFirstLoad = true;
    private ChatAdapter adapter;
    private ParseUser otherUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDmactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupMessagePosting();

        otherUser = Parcels.unwrap(getIntent().getParcelableExtra("other"));

        msgs = new ArrayList<>();

        adapter = new ChatAdapter(DMActivity.this, msgs);
        binding.rvMessages.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        binding.rvMessages.setLayoutManager(linearLayoutManager);

        refreshMessages();
    }

    private void setupMessagePosting() {
        binding.btnSend.setOnClickListener(v -> {
            String message = binding.etInput.getText().toString();
            ParseObject msgObject = ParseObject.create("Message");
            msgObject.put(Message.USER_ID_KEY, ParseUser.getCurrentUser());
            msgObject.put(Message.BODY_KEY, message);
            msgObject.put(Message.RECEIVER_KEY, otherUser);
            msgObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(DMActivity.this, "Successfully created message on Parse",
                                Toast.LENGTH_SHORT).show();
                        if(msgs.size() == 0) {
                            ParseUser user = ParseUser.getCurrentUser();
                            directs myDirects = (directs) user.getParseObject(directs.KEY_DIRECTS);
                            myDirects.getRelation(directs.KEY_DIRECTS).add(otherUser);
                            directs otherDirects = (directs) otherUser.getParseObject(directs.KEY_DIRECTS);
                            otherDirects.getRelation(directs.KEY_DIRECTS).add(user);
                            myDirects.saveInBackground();
                            otherDirects.saveInBackground();
                        }
                        refreshMessages();
                    } else {
                        Log.e(TAG, "Failed to save message", e);
                    }
                }
            });
            binding.etInput.setText(null);
        });
    }

    void refreshMessages() {
        // Construct query to execute
        ParseQuery<Message> other = ParseQuery.getQuery(Message.class);
        other.whereEqualTo(Message.USER_ID_KEY, otherUser);
        other.whereEqualTo(Message.RECEIVER_KEY, ParseUser.getCurrentUser());

        ParseQuery<Message> me = ParseQuery.getQuery(Message.class);
        me.whereEqualTo(Message.USER_ID_KEY, ParseUser.getCurrentUser());
        me.whereEqualTo(Message.RECEIVER_KEY, otherUser);

        List<ParseQuery<Message>> list = new ArrayList<>();
        list.add(me);
        list.add(other);
        ParseQuery<Message> query = ParseQuery.or(list);
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);

        // get the latest 50 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    msgs.clear();
                    msgs.addAll(messages);
                    adapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        binding.rvMessages.scrollToPosition(0);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }
}