package com.example.myapplication;

public class UserChat {
    public String id;
    public String chat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserChat(String id, String chat) {
        this.id = id;
        this.chat = chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getChat() {
        return chat;
    }
}
