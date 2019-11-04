package com.baidu.ar.pro.Task;

import android.graphics.Bitmap;

import java.util.List;

import com.baidu.ar.pro.Collection.Collection;

import org.litepal.crud.LitePalSupport;

public class Task extends LitePalSupport {
    private String task_name;
    private int task_ID;
    private int task_status;
    private int task_used_person;
    private int task_image_ID;
    private String task_imagePath;
    private int task_ad_image_ID;
    private int task_difficulty;
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

    public Task(String task_name, int task_ID, int task_status,int task_used_person, String image_path)
    {
        this.task_name = task_name;
        this.task_ID = task_ID;
        this.task_used_person = task_used_person;
        this.task_status = task_status;
        this.task_imagePath = image_path;
    }

    public Task(String task_name, int task_ID, int task_status,int task_used_person, int image_id)
    {
        this.task_name = task_name;
        this.task_ID = task_ID;
        this.task_used_person = task_used_person;
        this.task_status = task_status;
        this.task_image_ID = image_id;
    }


   /* public Task(String task_name,String task_background, int task_image_ID, int task_status, List<Collection> collection)
    {
        this.task_name = task_name;
        this.task_background = task_background;
        this.task_image_ID = task_image_ID;
        this.task_status = task_status;
        this.task_collection = collection;
    }*/

    public void setTask_ad_image_ID(int task_ad_image_ID)
    {
        this.task_ad_image_ID = task_ad_image_ID;
    }

    public void setTask_background(String task_background)
    {
        this.task_background = task_background;
    }

    public void setTask_collection(List<Collection> collectionList)
    {
        this.task_collection = collectionList;
    }

    public void setTask_name(String task_name)
    {
        this.task_name = task_name;
    }

    public void setTask_ID(int task_ID)
    {
        this.task_ID = task_ID;
    }

    public void setTask_status(int task_status)
    {
        this.task_status = task_status;
    }

    public void setTask_used_person(int task_used_person)
    {
        this.task_used_person = task_used_person;
    }

    public void setTask_image_ID(int task_image_ID)
    {
        this.task_image_ID = task_image_ID;
    }

    public void setTask_collected_ID(List<Integer> task_collected_ID)
    {
        this.task_collected_ID = task_collected_ID;
    }

    public void setTask_difficulty(int task_difficulty) {
        this.task_difficulty = task_difficulty;
    }

    public String getTask_imagePath() {
        return task_imagePath;
    }

    public void setTask_imagePath(String task_imagePath) {
        this.task_imagePath = task_imagePath;
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

    public String getTask_background()
    {
        return task_background;
    }

    public List<Collection> getTask_collection()
    {
        return task_collection;
    }

    public List<Integer> getTask_collected_ID()
    {
        return task_collected_ID;
    }

    public int getTask_ad_image_ID()
    {
        return task_ad_image_ID;
    }

    public int getTask_difficulty() {
        return task_difficulty;
    }
}
