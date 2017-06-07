package com.aziflaj.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aziflaj.todolist.db.TaskContract;
import com.aziflaj.todolist.db.TaskDbHelper;

import java.util.ArrayList;

/**
 * Created by Jacky on 06/06/2017.
 */

public class CustomAdapter extends ArrayAdapter<TaskObject> implements View.OnClickListener {

    private ArrayList<TaskObject> dataSet;
    Context mContext;
    private TaskDbHelper mHelper;

    // View lookup cache
    private static class ViewHolder {
        TextView txtTitle;
        Button btnDelete;
    }

    public CustomAdapter(ArrayList<TaskObject> data, Context context) {
        super(context, R.layout.item_todo, data);

        this.dataSet = data;
        this.mContext = context;
        this.mHelper = new TaskDbHelper(context);
    }

    @Override
    public void onClick(View v) {
    }

    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final TaskObject taskObject = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_todo, parent, false);

            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.task_title);
            viewHolder.btnDelete = (Button) convertView.findViewById(R.id.task_delete);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.txtTitle.setText(taskObject.getTitle());
        viewHolder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView noticeEditText = new TextView(mContext);
                noticeEditText.setText("Todo Notice: " + taskObject.getNotice());

                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Todo Title: " + taskObject.getTitle())
                        .setMessage("Todo Id: " + taskObject.getId())
                        .setView(noticeEditText)
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //xyz
                SQLiteDatabase db = mHelper.getWritableDatabase();
                db.delete(TaskContract.TaskEntry.TABLE,
                        TaskContract.TaskEntry._ID + " = ?",
                        new String[]{Integer.toString(taskObject.getId())});

                db.close();
                updateAdapter();
            }
        });

        return convertView;
    }

    public ArrayList<TaskObject> getTaskList() {
        ArrayList<TaskObject> newlist = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_NOTICE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            TaskObject taskObject = new TaskObject(
                    cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COL_TASK_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COL_TASK_NOTICE)));
            newlist.add(taskObject);
        }

        cursor.close();
        db.close();

        return newlist;
    }

    public void updateAdapter() {
        //TODO
        ArrayList<TaskObject> newlist = getTaskList();
        dataSet.clear();
        dataSet.addAll(newlist);
        this.notifyDataSetChanged();
    }
}