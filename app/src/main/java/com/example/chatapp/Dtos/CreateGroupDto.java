package com.example.chatapp.Dtos;

import com.example.chatapp.Model.Group.Member;


import java.util.*;

public class CreateGroupDto {
    public Set<Member> getMember() {
        return member;
    }

    public void setMember(Set<Member> member) {
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

    Set<Member> member = new HashSet<>();
    boolean isPublic;
    private String avatarGroup;
    private String bio;
    private String name;
}
