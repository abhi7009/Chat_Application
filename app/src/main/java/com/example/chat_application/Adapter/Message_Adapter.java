package com.example.chat_application.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat_application.Message_Activity;
import com.example.chat_application.Model.Chats;
import com.example.chat_application.Model.Users;
import com.example.chat_application.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<Chats> chatsList;
    private String image_url;

    FirebaseUser firebaseUser;

    public Message_Adapter(Context context, List<Chats> chatslist, String image_url) {
        this.context = context;
        this.chatsList = chatslist;
        this.image_url = image_url;
    }

    @NonNull
    @Override
    public Message_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right,parent,false);
            return new Message_Adapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left,parent,false);
            return new Message_Adapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull Message_Adapter.ViewHolder holder, final int position) {

        Chats chats = chatsList.get(position);

        holder.show_message.setText(chats.getMessage());

        if(image_url.equals("default")){
            holder.image_view.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(context).load(image_url).into(holder.image_view);
        }
        if(position == chatsList.size()-1) {
            if(chats.isIsseen()){
                holder.seen.setText("Seen");
            }else {
                holder.seen.setText("Delivered");
            }

        }else {
            holder.seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image_view;
        TextView show_message;
        public TextView seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            image_view = itemView.findViewById(R.id.image_view);
            seen = itemView.findViewById(R.id.seen);

        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatsList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}

