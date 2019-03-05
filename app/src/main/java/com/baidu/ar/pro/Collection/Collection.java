package com.baidu.ar.pro.Collection;



public class Collection {
    private String collection_name;
    private int collection_ID;
    private int collection_imageId;
    private String collection_story;
    private String collection_hint;

    public Collection(int collection_ID, String name,int imageId, String collection_story, String hint)
    {
        this.collection_ID=collection_ID;
        this.collection_name=name;
        this.collection_imageId=imageId;
        this.collection_story=collection_story;
        this.collection_hint=hint;
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

}
