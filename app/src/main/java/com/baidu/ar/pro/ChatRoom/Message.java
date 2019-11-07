package com.baidu.ar.pro.ChatRoom;

import org.litepal.crud.LitePalSupport;

import java.sql.Time;

public class Message extends LitePalSupport {

    private int message_id;

    private String content;

    private int sender_id;

    private int receiver_id;

    private Time time;

    public Message(String content, int sender_id, int receiver_id){
        this.content = content;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
    }

    public Message(int message_id, String content, int sender_id, int receiver_id){
        this.message_id = message_id;
        this.content = content;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
    }

    public String getContent() {
        return content;
    }

    public int getMessage_id() {
        return message_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public Time getTime() {
        return time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
