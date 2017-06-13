package com.aziflaj.todolist.sp;

import android.content.Context;

import com.aziflaj.todolist.TaskObject;

/**
 * Created by Jacky on 14/06/2017.
 */

public class TaskSpHelper {
    public static void setTaskList(TaskObject task, Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "object_prefs", 0);
        complexPreferences.putObject("object_value", task);
        complexPreferences.commit();
    }

    public static TaskObject getTaskList(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "object_prefs", 0);
        TaskObject task = complexPreferences.getObject("object_value", TaskObject.class);
        return task;
    }

    public static void clearTaskList( Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "object_prefs", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }
}
