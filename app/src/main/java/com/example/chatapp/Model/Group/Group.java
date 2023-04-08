package com.example.chatapp.Model.Group;

import com.example.chatapp.Model.BaseModel;
import com.example.chatapp.Model.File.File;
import com.example.chatapp.Model.Message.Message;
import com.example.chatapp.Model.User.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
public class Group extends BaseModel {
    String name;
    String bio;
    String QR;
    Set<Role> roles = new HashSet<>();
    Set<Member> members = new HashSet<>();
    boolean isPublic;
    Set<Block> block_list = new HashSet<>();
    private String id;
    private String code = UUID.randomUUID().toString();
    private List<Message> pinMessage;
    private User user_own;
    private File avatarGroup;
}
