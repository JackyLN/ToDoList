package com.aziflaj.todolist;

/**
 * Created by Jacky on 06/06/2017.
 */

public class TaskObject {
    private int id;
    private String title;
    private String notice;

    public TaskObject(int id, String title, String notice){
        this.setId(id);
        this.setTitle(title);
        this.setNotice(notice);
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getNotice(){
        return notice;
    }
    public void setNotice(String notice){
        this.notice= notice;
    }
}
