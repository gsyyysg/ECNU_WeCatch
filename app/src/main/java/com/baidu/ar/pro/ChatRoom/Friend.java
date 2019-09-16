package com.baidu.ar.pro.ChatRoom;

public class Friend {

    private String id;

    private String name;

    private String image;

    public Friend(String id) {
        this.id = id;
        name = id;
        image = null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

}
