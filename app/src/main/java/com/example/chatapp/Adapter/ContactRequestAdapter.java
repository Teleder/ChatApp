package com.example.chatapp.Adapter;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Dtos.UserBasicDto;
import com.example.chatapp.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class ContactRequestAdapter extends RecyclerView.Adapter<ContactRequestAdapter.ContactViewHolder> {
    private final List<UserBasicDto> contacts;
    private final SparseBooleanArray selectedItems;

    public ContactRequestAdapter(List<UserBasicDto> contacts) {
        this.contacts = contacts;
        this.selectedItems = new SparseBooleanArray();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_request_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        UserBasicDto contact = contacts.get(position);
        holder.contactName.setText(contact.getDisplayName());
        // load avatar using Picasso or Glide
        // Example with Picasso:
        // Picasso.get().load(contact.getAvatarUrl()).into(holder.contactAvatar);
        holder.itemView.setSelected(selectedItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;
        CheckBox checkBox;
        RoundedImageView contactAvatar;

        public ContactViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            checkBox = itemView.findViewById(R.id.checkbox);
            contactAvatar = itemView.findViewById(R.id.contact_avatar);
            checkBox.setClickable(false);
            itemView.setOnClickListener(v -> {
                if (selectedItems.get(getAdapterPosition(), false)) {
                    selectedItems.delete(getAdapterPosition());
                    checkBox.setChecked(false);
                } else {
                    Log.d("ContactRequestAdapter", "onClick: " + getAdapterPosition());
                    selectedItems.put(getAdapterPosition(), true);
                    checkBox.setChecked(true);
                }
            });
        }
    }

        public List<UserBasicDto> getSelectedContacts() {
            List<UserBasicDto> selectedContacts = new ArrayList<>();
            for (int i = 0; i < selectedItems.size(); i++) {
                selectedContacts.add(contacts.get(selectedItems.keyAt(i)));
            }
            return selectedContacts;
        }
}
