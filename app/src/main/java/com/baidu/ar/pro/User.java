package com.baidu.ar.pro;

import org.litepal.crud.LitePalSupport;

public class User extends LitePalSupport {
    private int user_ID = 0;
    private String email = "";
    private String nickname = "";
    private int user_image_ID = 0;
    private int user_golds = 0;
    private String password_hash = "";
    private String about_me = "";
    private boolean owner = false;
    private String cookie = "";
    private int trackingCollectionID = 0;

    public User(String email, String nickname, int image_ID, int golds, int user_ID, boolean owner)
    {
        this.email=email;
        this.nickname=nickname;
        this.user_image_ID=image_ID;
        this.user_golds = golds;
        this.user_ID = user_ID;
        this.owner = owner;
        this.password_hash = null;
        this.about_me = null;
    }

    public User(String email, String nickname, int image_ID, int golds, int user_ID, boolean owner, String password)
    {
        this.email=email;
        this.nickname=nickname;
        this.user_image_ID=image_ID;
        this.user_golds = golds;
        this.user_ID = user_ID;
        this.owner = owner;
        this.password_hash = password;
        this.about_me = null;
    }

    public User(){

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUser_golds(int user_golds) {
        this.user_golds = user_golds;
    }

    public void setUser_image_ID(int user_image_ID) {
        this.user_image_ID = user_image_ID;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public void setTrackingCollectionID(int trackingCollectionID) {
        this.trackingCollectionID = trackingCollectionID;
    }

    public String getPassword_hash() {
        return password_hash;
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

    public String getAbout_me() {
        return about_me;
    }

    public int getUser_ID() {
        return user_ID;
    }

    public boolean isOwner() {
        return owner;
    }

    public String getCookie() {
        return cookie;
    }

    public int getTrackingCollectionID() {
        return trackingCollectionID;
    }
}
