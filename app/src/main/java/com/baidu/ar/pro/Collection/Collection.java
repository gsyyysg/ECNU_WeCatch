package com.baidu.ar.pro.Collection;


import java.util.List;

public class Collection {
    private String collection_name;
    private int imageId;
    private List<String> collection_story;

    public Collection(String name, int imageId)
    {
        this.collection_name=name;
        this.imageId=imageId;
    }

    public Collection(String name, int imageId, List<String> story)
    {
        this.collection_name=name;
        this.imageId=imageId;
        this.collection_story=story;
    }

    public String getname()
    {
        return collection_name;
    }

    public int getImageId()
    {
        return imageId;
    }

    public List<String> getCollection_story(){return collection_story; }
}
