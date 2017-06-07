package com.aziflaj.todolist;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.aziflaj.todolist.db.TaskContract;
import com.aziflaj.todolist.db.TaskDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayList<TaskObject> dataModels;
    //private ArrayAdapter<TaskObject> mAdapter;
    private static CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);

        dataModels = updateData();
        mAdapter = new CustomAdapter(dataModels, MainActivity.this);

        mTaskListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                final EditText taskEditTextTitle = new EditText(this);
                final EditText taskEditTextNotice = new EditText(this);

                final LayoutInflater inflater = this.getLayoutInflater();
                final View view = inflater.inflate(R.layout.dialog_newtask, null);

                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setMessage("What do you want to do next?")
                        .setView(view)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText title = (EditText) view.findViewById(R.id.taskTitle);
                                EditText notice = (EditText) view.findViewById(R.id.taskNotice);

                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, title.getText().toString());
                                values.put(TaskContract.TaskEntry.COL_TASK_NOTICE, notice.getText().toString());
                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                mAdapter.updateAdapter();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<TaskObject> updateData() {
        ArrayList<TaskObject> list = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_NOTICE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            TaskObject taskObject = new TaskObject(
                    cursor.getInt(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COL_TASK_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COL_TASK_NOTICE)));
            list.add(taskObject);
        }

        cursor.close();
        db.close();

        return list;
    }
}
