package com.example.chatapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Model.Group.Member;
import com.example.chatapp.R;

import java.util.List;

public class BlockedMemberAdapter extends RecyclerView.Adapter<BlockedMemberAdapter.BlockedMemberViewHolder> {
    private List<Member> blockedMembers;

    public BlockedMemberAdapter(List<Member> blockedMembers) {
        this.blockedMembers = blockedMembers;
    }

    @NonNull
    @Override
    public BlockedMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blocked_member_item, parent, false);
        return new BlockedMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockedMemberViewHolder holder, int position) {
        Member member = blockedMembers.get(position);
        holder.blockedMemberName.setText(member.getUser().getDisplayName());
        // set up the avatar for the member, customize this to display as you want
        // use Picasso or Glide to load image from URL if you have

        holder.unblockButton.setOnClickListener(v -> {
            // unblock member here
            // after unblocking, remember to update your list and call notifyDataSetChanged()
        });
    }

    @Override
    public int getItemCount() {
        return blockedMembers.size();
    }

    public static class BlockedMemberViewHolder extends RecyclerView.ViewHolder {
        TextView blockedMemberName;
        ImageView blockedMemberAvatar;
        Button unblockButton;

        public BlockedMemberViewHolder(View itemView) {
            super(itemView);
            blockedMemberName = itemView.findViewById(R.id.blocked_member_name);
            blockedMemberAvatar = itemView.findViewById(R.id.blocked_member_avatar);
            unblockButton = itemView.findViewById(R.id.unblock_button);
        }
    }
}