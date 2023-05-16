package com.example.chatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Dtos.UserBasicDto;
import com.example.chatapp.R;
import com.example.chatapp.Utils.CONSTS;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SelectedMemberAdapter extends RecyclerView.Adapter<SelectedMemberAdapter.ViewHolder> {
    private List<UserBasicDto> selectedMembers;
    Context context;

    public SelectedMemberAdapter(Context context,List<UserBasicDto> selectedMembers) {
        this.selectedMembers = selectedMembers;
         this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return selectedMembers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView memberName;
        RoundedImageView contact_avatar;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            memberName = itemView.findViewById(R.id.contact_name);
            contact_avatar = itemView.findViewById(R.id.contact_avatar);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserBasicDto member = selectedMembers.get(position);
        holder.memberName.setText(member.getDisplayName());
        Glide.with(context).load(
                selectedMembers.get(position).getAvatar() == null ? R.drawable.ic_people_24 :
                        selectedMembers.get(position).getAvatar().getUrl().replace("localhost:8080", "http://" + CONSTS.BASEURL)
        ).into( holder.contact_avatar);

    }
}