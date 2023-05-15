package com.example.chatapp.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Model.Group.Role;
import com.example.chatapp.Model.Permission.Permission;
import com.example.chatapp.R;

import java.util.List;
public class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.RoleViewHolder> {
    private List<Role> roles;

    public RoleAdapter(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public RoleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.role_item, parent, false);
        return new RoleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoleViewHolder holder, int position) {
        Role role = roles.get(position);
        holder.roleName.setText(role.getName());
        // set up the permissions for the role, customize this to display as you want
        StringBuilder permissions = new StringBuilder();
        for(Permission permission : role.getPermissions()) {
            permissions.append(permission.getAction()).append(", ");
        }
        //remove the last comma and space
        if(permissions.length() > 0) {
            permissions.setLength(permissions.length() - 2);
        }
        holder.rolePermissions.setText(permissions.toString());

        holder.deleteButton.setOnClickListener(v -> {
            roles.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return roles.size();
    }

    public static class RoleViewHolder extends RecyclerView.ViewHolder {
        TextView roleName;
        TextView rolePermissions;
        Button deleteButton;

        public RoleViewHolder(View itemView) {
            super(itemView);
            roleName = itemView.findViewById(R.id.role_name);
            rolePermissions = itemView.findViewById(R.id.role_permissions);
            deleteButton = itemView.findViewById(R.id.delete_role);
        }
    }
}
