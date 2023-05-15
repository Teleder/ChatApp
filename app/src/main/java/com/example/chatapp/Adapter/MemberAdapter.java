package com.example.chatapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Model.Group.Member;
import com.example.chatapp.R;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    private List<Member> members;

    public MemberAdapter(List<Member> members) {
        this.members = members;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_request_item, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = members.get(position);
        holder.memberName.setText(member.getUser().getDisplayName());
        // set up the avatar for the member, customize this to display as you want
        // use Picasso or Glide to load image from URL if you have
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView memberName;
        ImageView memberAvatar;

        public MemberViewHolder(View itemView) {
            super(itemView);
            memberName = itemView.findViewById(R.id.member_name);
            memberAvatar = itemView.findViewById(R.id.member_avatar);
        }
    }
}
