package com.aziflaj.todolist.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.aziflaj.todolist.TaskObject;
import com.google.gson.Gson;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Jacky on 14/06/2017.
 */

public class TaskSpHelper {
    private static Gson GSON = new Gson();
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    String pre_key = "task_";
    int maxId = 1;
    String spKey;

    public TaskSpHelper(Context context, String spKey, int mode) {
        if (spKey.equals("") || spKey == null) {
            this.spKey = "Task_shared_preferences";
        }
        this.context = context;
        preferences = context.getSharedPreferences(spKey, mode);
        editor = preferences.edit();
    }

    public ArrayList<TaskObject> getTaskList() {
        ArrayList<TaskObject> list = new ArrayList<TaskObject>();
        Map<String, ?> allEntries = preferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            TaskObject object = getTask(entry.getKey());
            list.add(object);
            //entry.getValue();
        }
        return list;
    }

    public TaskObject getTask(int Id) {
        String taskKey = pre_key + Id;
        String jsonTask = preferences.getString(taskKey, "");
        return GSON.fromJson(jsonTask, TaskObject.class);
    }

    public TaskObject getTask(String keySP) {
        String jsonTask = preferences.getString(keySP, "");
        return GSON.fromJson(jsonTask, TaskObject.class);
    }

    public void insertTask(String title, String notice) {
        String taskKey = pre_key + maxId;
        TaskObject object = new TaskObject(maxId, title, notice);
        editor.putString(taskKey, GSON.toJson(object));
        editor.commit();

        maxId++;
    }

    public void updateTask(TaskObject object) {
        if (object == null) {
            throw new IllegalArgumentException("object is null");
        }

        TaskObject spObject = getTask(object.getId());
        spObject.setTitle(object.getTitle());
        spObject.setNotice(object.getNotice());

        String taskKey = pre_key + spObject.getId();
        editor.putString(taskKey, GSON.toJson(object));
        editor.commit();
    }

    public void removeTask(int Id) {
        String taskKey = pre_key + Id;
        editor.remove(taskKey);
        editor.commit();
    }

    public void clearTaskList(){
        editor.clear();
        editor.commit();
    }

    private int getIdFromKey(String keyId) {
        try {
            return Integer.parseInt(keyId.replace(keyId, ""));
        } catch (Exception e) {
            return 0;
        }
    }

    // Not in use, as it will cause complicated
    /*
    public void setTaskList() {
        editor.putString(key, GSON.toJson(list));
        editor.commit();
    }

    public ArrayList<TaskObject> getTaskList() {
        String jsonList = preferences.getString(key, "");

        ArrayList<TaskObject> returnList = GSON.fromJson(jsonList,
                new TypeToken<ArrayList<TaskObject>>() {
                }.getType()
        );

        return returnList;
    }

    public TaskObject getTask(int objectId) {
        TaskObject object = list.get(objectId);
        return object;
    }
    */
}