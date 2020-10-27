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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<Users> usersList;
    private boolean ischat;

    String thelastMessage;

    public UserAdapter(Context context, List<Users> usersList,boolean ischat) {
        this.context = context;
        this.usersList = usersList;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Users users = usersList.get(position);
        holder.username.setText(usersList.get(position).getUsername());
        if(usersList.get(position).getImage_url().equals("default")){
            holder.image_view.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(context).load(usersList.get(position).getImage_url()).into(holder.image_view);
        }

        if(ischat){
            lastMessage(users.getId(),holder.last_msg);
        }else {
            holder.last_msg.setVisibility(View.GONE);
        }


        if(ischat){
            if(users.getStatus().equals("online")){
                holder.image_on.setVisibility(View.VISIBLE);
                holder.image_off.setVisibility(View.GONE);
            }else {
                holder.image_on.setVisibility(View.GONE);
                holder.image_off.setVisibility(View.VISIBLE);
            }

        }else {
            holder.image_on.setVisibility(View.GONE);
            holder.image_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Message_Activity.class);
                Users users = usersList.get(position);
                intent.putExtra("userid",users.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image_view;
        TextView username;
        CircleImageView image_on,image_off;
        private TextView last_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            image_view = itemView.findViewById(R.id.image_view);
            image_on = itemView.findViewById(R.id.image_on);
            image_off = itemView.findViewById(R.id.image_off);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }

    private void lastMessage(final String userid, final TextView last_msg){
      thelastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chats chats = dataSnapshot.getValue(Chats.class);
                    if(chats.getReciver().equals(firebaseUser.getUid()) && chats.getSender().equals(userid) ||
                    chats.getReciver().equals(userid) && chats.getSender().equals(firebaseUser.getUid())){
                        thelastMessage = chats.getMessage();
                    }
                }

                switch (thelastMessage){
                    case "default":
                        last_msg.setText("No Message");
                        break;
                    default:
                        last_msg.setText(thelastMessage);
                        break;

                }
                thelastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
