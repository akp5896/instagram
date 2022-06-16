package com.example.parstagram31.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram31.Models.Message;
import com.example.parstagram31.R;
import com.example.parstagram31.databinding.MessageIncomingBinding;
import com.example.parstagram31.databinding.MessageOutgoingBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private List<Message> messages;
    private Context context;

    private static final int MESSAGE_OUTGOING = 666;
    private static final int MESSAGE_INCOMING = 777;

    public ChatAdapter(Context context, List<Message> messages) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == MESSAGE_INCOMING) {
            View contactView = inflater.inflate(R.layout.message_incoming, parent, false);
            return new IncomingMessageViewHolder(contactView);
        } else if (viewType == MESSAGE_OUTGOING) {
            View contactView = inflater.inflate(R.layout.message_outgoing, parent, false);
            return new OutgoingMessageViewHolder(contactView);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bindMessage(messages.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (isMe(position)) {
            return MESSAGE_OUTGOING;
        } else {
            return MESSAGE_INCOMING;
        }
    }

    private boolean isMe(int position) {
        Message message = messages.get(position);
        ParseUser user = message.getUserId();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return message.getUserId() != null && user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public abstract class MessageViewHolder extends RecyclerView.ViewHolder {

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bindMessage(Message message);
    }
    public class IncomingMessageViewHolder extends MessageViewHolder {
            MessageIncomingBinding binding;

        public IncomingMessageViewHolder(View itemView) {
            super(itemView);
            binding = MessageIncomingBinding.bind(itemView);
        }

        @Override
        public void bindMessage(Message message) {
            try {
                message.fetch();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ParseFile parseFile = null;
            try {
                parseFile = message.getUserId().fetch().getParseFile("image");
                if(parseFile != null) {
                    Glide.with(context)
                            .load(parseFile.getUrl())
                            .circleCrop()
                            .into(binding.ivProfileOther);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            binding.tvBody.setText(message.getBody());
            binding.tvName.setText(message.getUserId().getUsername());
        }
    }

    public class OutgoingMessageViewHolder extends MessageViewHolder {
        MessageOutgoingBinding binding;

        public OutgoingMessageViewHolder(View itemView) {
            super(itemView);
            binding = MessageOutgoingBinding.bind(itemView);
        }

        @Override
        public void bindMessage(Message message) {

            ParseFile parseFile = null;
            try {
                parseFile = message.getUserId().fetch().getParseFile("image");
                if(parseFile != null) {
                    Glide.with(context)
                            .load(parseFile.getUrl())
                            .circleCrop()
                            .into(binding.ivProfileMe);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            binding.tvBody.setText(message.getBody());
        }
    }
}
