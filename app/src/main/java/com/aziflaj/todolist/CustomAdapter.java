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

import com.aziflaj.todolist.sp.TaskSpHelper;

import java.util.ArrayList;

/**
 * Created by Jacky on 06/06/2017.
 */

public class CustomAdapter extends ArrayAdapter<TaskObject> implements View.OnClickListener {

    private ArrayList<TaskObject> dataSet;
    Context mContext;
    private TaskSpHelper mHelper;

    // View lookup cache
    private static class ViewHolder {
        TextView txtTitle;
        Button btnDelete;
    }

    public CustomAdapter(ArrayList<TaskObject> data,TaskSpHelper mHelper, Context context) {
        super(context, R.layout.item_todo, data);

        this.dataSet = data;
        this.mContext = context;
        this.mHelper = mHelper;
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
                mHelper.removeTask(taskObject.getId());
                updateAdapter();
            }
        });

        return convertView;
    }

    public void updateAdapter() {
        ArrayList<TaskObject> newlist = mHelper.getTaskList();
        dataSet.clear();
        dataSet.addAll(newlist);
        this.notifyDataSetChanged();
    }
}