package com.example.chatapp.Dtos;

import com.example.chatapp.Model.Group.Member;

import java.util.List;
import java.util.Set;

public class UpdateGroupDto extends CreateGroupDto {

    public UpdateGroupDto(List<Member> member, boolean isPublic, String avatarGroup, String bio, String name) {
        super(member, isPublic, avatarGroup, bio, name);
    }
}
