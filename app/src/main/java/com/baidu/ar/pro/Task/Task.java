package com.baidu.ar.pro.Task;

import java.util.List;

import com.baidu.ar.pro.Collection.Collection;

public class Task {
    private String task_name;
    private int task_ID;
    private int task_status;
    private int task_used_person;
    private int task_image_ID;
    private int task_ad_image_ID;
    private String task_background;
    private List<Collection> task_collection;
    private List<Integer> task_collected_ID;

    public Task()
    {

    }

    public Task(String task_name, int task_ID, int task_status, int task_used_person, int task_image_ID, String task_background, List<Collection> collection, List<Integer> collected)
    {
        this.task_name=task_name;
        this.task_ID=task_ID;
        this.task_status=task_status;
        this.task_used_person=task_used_person;
        this.task_image_ID=task_image_ID;
        this.task_background=task_background;
        this.task_collection=collection;
        this.task_collected_ID=collected;
    }

    public Task(String task_name, int task_ID, int task_status,int task_used_person, int task_image_ID)
    {
        this.task_name = task_name;
        this.task_ID = task_ID;
        this.task_used_person = task_used_person;
        this.task_status = task_status;
        this.task_image_ID = task_image_ID;
    }

   /* public Task(String task_name,String task_background, int task_image_ID, int task_status, List<Collection> collection)
    {
        this.task_name = task_name;
        this.task_background = task_background;
        this.task_image_ID = task_image_ID;
        this.task_status = task_status;
        this.task_collection = collection;
    }*/


    public void Set_Task_ad_imageID(int ad_image_ID)
    {
        this.task_ad_image_ID = ad_image_ID;
    }

    public void Set_Task_Background(String story)
    {
        this.task_background = story;
    }

    public void Set_Task_Collection(List<Collection> collectionlist)
    {
        this.task_collection = collectionlist;
    }

    public void Set_Task_Name(String name)
    {
        this.task_name = name;
    }

    public void Set_Task_ID(int id)
    {
        this.task_ID = id;
    }

    public void Set_Task_status(int status)
    {
        this.task_status = status;
    }

    public void Set_Task_UsedPerson(int number)
    {
        this.task_used_person = number;
    }


    public void Set_Task_ImageID(int imageID)
    {
        this.task_image_ID = imageID;
    }

    public void Set_Task_CollectedId(List<Integer> collectedID)
    {
        this.task_collected_ID = collectedID;
    }



    public String getTask_name()
    {
        return task_name;
    }

    public int getTask_status()
    {
        return task_status;
    }

    public int getTask_used_person(){ return task_used_person; }

    public int getTask_ID() {
        return task_ID;
    }

    public int getTask_image_ID() {
        return task_image_ID;
    }

    public String getTask_background(){
        return task_background;
    }

    public List<Collection> getTask_collection(){ return task_collection; }
    public List<Integer> getCollected_ID(){ return task_collected_ID; }

    public int getTask_ad_image_ID()
    {
        return task_ad_image_ID;
    }

}
