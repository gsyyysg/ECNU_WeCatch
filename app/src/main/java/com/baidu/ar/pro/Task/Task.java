package com.baidu.ar.pro.Task;

public class Task {
    private String task_name;
    private int task_status;

    public Task(String task_name, int task_status)
    {
        this.task_name=task_name;
        this.task_status=task_status;
    }

    public String getTask_name()
    {
        return task_name;
    }

    public int getTask_status()
    {
        return task_status;
    }
}
