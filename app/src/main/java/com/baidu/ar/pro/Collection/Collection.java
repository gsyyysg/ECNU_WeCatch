package com.baidu.ar.pro.Collection;

import java.io.Serializable;

public class Collection implements Serializable {

    private static final long serialVersionUID = -6470574927973900913L;

    private String collection_name;
    private int collection_ID;
    private int collection_imageId;
    private String collection_story;
    private String collection_hint;
    private int collection_gold;
    private int latitude;  //从后端获得的收藏品标准经纬度
    private int longtitude;

    public Collection(int collection_ID, String name,int imageId, String collection_story, String hint, int collection_gold)
    {
        this.collection_ID=collection_ID;
        this.collection_name=name;
        this.collection_imageId=imageId;
        this.collection_story=collection_story;
        this.collection_hint=hint;
        this.collection_gold=collection_gold;
    }

    public Collection(int collection_ID, int imageID)
    {
        this.collection_ID = collection_ID;
        this.collection_imageId = imageID;
    }

    /*public Collection(String name,int imageId,List<String> story)
    {
        this.collection_name=name;
        this.imageId=imageId;
        //this.collection_story=story;
    }*/

    public void Set_Collection_Name(String name){ this.collection_name = name; }

    public String getCollection_name()
    {
        return collection_name;
    }

    public int getCollection_imageId()
    {
        return collection_imageId;
    }

    public String getCollection_story(){return collection_story; }

    public void Set_Collection_story(String story)
    {
        this.collection_story = story;
    }

    public int getCollection_ID(){
        return collection_ID;
    }

    public String getCollection_hint()
    {
        return collection_hint;
    }

    public void Set_Collection_hint(String hint)
    {
        this.collection_hint = hint;
    }

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

    public int getCollection_gold()
    {
        return collection_gold;
    }

    public void setCollection_gold(int gold)
    {
        this.collection_gold = gold;
    }

}
