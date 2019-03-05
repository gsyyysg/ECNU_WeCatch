package com.baidu.ar.pro;

public class User {
    private String email;
    private String nickname;
    int user_image_ID;

    public User(String email, String nickname, int image_ID)
    {
        this.email=email;
        this.nickname=nickname;
        this.user_image_ID=image_ID;
    }

    String getEmail()
    {
        return email;
    }

    String getNickname()
    {
        return nickname;
    }

    int getUser_image_ID()
    {
        return user_image_ID;
    }
}
