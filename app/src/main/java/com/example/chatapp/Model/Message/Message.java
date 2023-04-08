package com.example.chatapp.Model.Message;

import com.example.chatapp.Model.BaseModel;
import com.example.chatapp.Model.Group.Group;
import com.example.chatapp.Model.User.User;
import com.example.chatapp.Utils.CONSTS;

import java.util.ArrayList;
import java.util.List;

public class Message extends BaseModel {
    private String id;
    private String content;
    private String code;
    private String TYPE = CONSTS.MESSAGE_PRIVATE;
    private User user_send;
    private User user_receive;
    private Group group;
    private List<Emotion> list_emotion = new ArrayList<>();
    private List<HistoryChange> historyChanges = new ArrayList<>();

    public Message(String content, String code, String TYPE){
        this.code =code;
        this.content = content;
        this.TYPE = TYPE;
    }

}
