package com.example.chatapp.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.RoleAdapter;
import com.example.chatapp.Model.Group.Role;
import com.example.chatapp.Model.Permission.Action;
import com.example.chatapp.Model.Permission.Permission;
import com.example.chatapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleDialog extends DialogFragment {
    private List<Role> roles;
    private Map<String, String> permissions = new HashMap<String, String>() {{
        put("ADD_MEMBER_TO_GROUP", "Add Member To Group");
        put("BLOCK_MEMBER_GROUP", "Block Member Group");
        put("REQUEST_MEMBER_TO_GROUP", "Request Member To Group");
        put("REMOVE_MEMBER", "Remove Member");
        put("REMOVE_BLOCK_MEMBER_GROUP", "Remove Block Member Group");
        put("BLOCK_MEMBER_GROUP", "Block Member Group");
    }};

    public RoleDialog(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Manage Roles");

        // create the view for the dialog
        View view = getActivity().getLayoutInflater().inflate(R.layout.role_dialog, null);
        RecyclerView roleRecyclerView = view.findViewById(R.id.role_recycler_view);
        RoleAdapter roleAdapter = new RoleAdapter(roles);
        roleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        roleRecyclerView.setAdapter(roleAdapter);

        // when the add role button is clicked
        Button addRoleButton = view.findViewById(R.id.add_role);
        addRoleButton.setOnClickListener(v -> {
            AlertDialog.Builder addRoleDialog = new AlertDialog.Builder(getActivity());
                addRoleDialog.setTitle("Add Role");
            View addRoleView = getActivity().getLayoutInflater().inflate(R.layout.add_role_dialog, null);

            EditText roleNameInput = addRoleView.findViewById(R.id.role_name_input);
            TextView permissionsPreview = addRoleView.findViewById(R.id.permissions_preview);
            addRoleDialog.setView(addRoleView);

            String[] permissionValues = permissions.values().toArray(new String[0]);
            boolean[] checkedPermissions = new boolean[permissionValues.length];
            addRoleDialog.setMultiChoiceItems(permissionValues, checkedPermissions, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    checkedPermissions[which] = isChecked;

                    // Update permissions preview
                    StringBuilder selectedPermissions = new StringBuilder();
                    for (int i = 0; i < checkedPermissions.length; i++) {
                        if (checkedPermissions[i]) {
                            selectedPermissions.append(permissionValues[i]).append(", ");
                        }
                    }
                    permissionsPreview.setText(selectedPermissions.toString());
                }
            });

            addRoleDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String roleName = roleNameInput.getText().toString();
                    List<Permission> selectedPermissions = new ArrayList<>();
                    int i = 0;
                    for (Map.Entry<String, String> entry : permissions.entrySet()) {
                        if (checkedPermissions[i]) {
                            Permission permission = new Permission();
                            permission.setAction(Action.valueOf(entry.getKey()));
                            selectedPermissions.add(permission);
                        }
                        i++;
                    }
                    Role newRole = new Role();
                    newRole.setName(roleName);
                    newRole.setPermissions(selectedPermissions);
                    roles.add(newRole);
                    roleAdapter.notifyDataSetChanged();
                }
            });
            addRoleDialog.setNegativeButton("Cancel", null);
            addRoleDialog.create().show();
        });

        builder.setView(view);
        return builder.create();
    }
}
