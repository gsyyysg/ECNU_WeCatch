package com.baidu.ar.pro;

public class User {
    private String email;
    private String nickname;
    private int user_image_ID;
    private int user_golds;

    public User(String email, String nickname, int image_ID, int golds)
    {
        this.email=email;
        this.nickname=nickname;
        this.user_image_ID=image_ID;
        this.user_golds = golds;
    }

    public String getEmail()
    {
        return email;
    }

    public String getNickname()
    {
        return nickname;
    }

    public int getUser_image_ID()
    {
        return user_image_ID;
    }

    public int getUser_golds()
    {
        return user_golds;
    }
}
