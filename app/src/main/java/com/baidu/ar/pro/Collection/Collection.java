package com.baidu.ar.pro.Collection;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Collection extends LitePalSupport implements Serializable{

    private static final long serialVersionUID = -6470574927973900913L;

    private String collection_name;
    private int collection_ID;
    private int collection_imageId;
    private String collection_story;
    private String collection_hint;
    private String collection_image_name;
    private int collection_gold;
    private int status = 0;
    private double latitude;  //从后端获得的收藏品标准经纬度
    private double longitude;
    private int AR_ID;

    public Collection(int collection_ID, String name,int imageId, String collection_story, String hint, int collection_gold, int status)
    {
        this.collection_ID=collection_ID;
        this.collection_name=name;
        this.collection_imageId=imageId;
        this.collection_story=collection_story;
        this.collection_hint=hint;
        this.collection_gold=collection_gold;
        this.status = status;
    }

    public Collection(int collection_ID, int imageID)
    {
        this.collection_ID = collection_ID;
        this.collection_imageId = imageID;
    }

    public Collection(int collection_ID, String image_name)
    {
        this.collection_ID = collection_ID;
        this.collection_image_name = image_name;
    }

    public Collection(int collection_ID, int imageID, int status)
    {
        this.collection_ID = collection_ID;
        this.collection_imageId = imageID;
        this.status = status;
    }

    /*public Collection(String name,int imageId,List<String> story)
    {
        this.collection_name=name;
        this.imageId=imageId;
        //this.collection_story=story;
    }*/



    public Collection()
    {

    }

    public Collection(String name, String story, String hint, int imageID,int gold)
    {
        this.collection_name = name;
        this.collection_story = story;
        this.collection_hint = hint;
        this.collection_imageId = imageID;
        this.collection_gold = gold;
    }

    public void setCollection_name(String name)
    {
        this.collection_name = name;
    }

    public void setCollection_story(String story)
    {
        this.collection_story = story;
    }

    public void setCollection_hint(String hint)
    {
        this.collection_hint = hint;
    }

    public void setCollection_imageId(int imageid)
    {
        this.collection_imageId = imageid;
    }

    public void setCollection_ID(int collection_ID) {
        this.collection_ID = collection_ID;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setCollection_gold(int gold)
    {
        this.collection_gold = gold;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCollection_image_name(String collection_image_name) {
        this.collection_image_name = collection_image_name;
    }

    public void setAR_ID(int AR_ID) {
        this.AR_ID = AR_ID;
    }

    public int getCollection_gold()
    {
        return collection_gold;
    }

    public String getCollection_name()
    {
        return collection_name;
    }

    public int getCollection_imageId()
    {
        return collection_imageId;
    }

    public String getCollection_story(){return collection_story; }

    public int getCollection_ID(){
        return collection_ID;
    }

    public String getCollection_hint()
    {
        return collection_hint;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getStatus() {
        return status;
    }

    public String getCollection_image_name() {
        return collection_image_name;
    }

    public int getAR_ID() {
        return AR_ID;
    }
}
