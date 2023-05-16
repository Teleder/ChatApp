package com.example.chatapp.Dtos;

import com.example.chatapp.Model.Group.Member;


import java.util.*;

public class CreateGroupDto {
    public List<Member> getMember() {
        return member;
    }

    public void setMember(List<Member> member) {
        this.member = member;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getAvatarGroup() {
        return avatarGroup;
    }

    public void setAvatarGroup(String avatarGroup) {
        this.avatarGroup = avatarGroup;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    List<Member> member = new ArrayList<>();

    public CreateGroupDto(List<Member> member, boolean isPublic, String avatarGroup, String bio, String name) {
        this.member = member;
        this.isPublic = isPublic;
        this.avatarGroup = avatarGroup;
        this.bio = bio;
        this.name = name;
    }

    boolean isPublic;
    private String avatarGroup;
    private String bio;
    private String name;
}
