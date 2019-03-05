package com.baidu.ar.pro.Task;

import java.util.Collection;
import java.util.List;

public class Task {
    private String task_name;
    private int task_status;
    private int task_used_person;
    private int task_image_ID;
    private String task_background;
    private int task_ID;
    private List<Collection> task_collection;
    private List<Integer> task_collected_ID;

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
}
