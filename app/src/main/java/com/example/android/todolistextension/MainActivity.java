/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.todolistextension;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Read all the todolist item in the database, and display them.
 * Respond to user when click addnew button, click the existing todolist item.
 * Receive the updated information from EditToDoItemActivity, and update the database.
 */
public class MainActivity extends AppCompatActivity {
    private ArrayList<TodoItem> mItems;
    private ListView mListView;
    private ItemsAdapter mItemsAdapter;
    public final int EDIT_ITEM_REQUEST_CODE = 647;
    public final int ADD_ITEM_REQUEST_CODE = 648;
    private ToDoItemDB mDB;
    private ItemDao mToDoItemDao;
    private SimpleDateFormat msdf;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mItems = new ArrayList<TodoItem>();
        mItems.add(new TodoItem("item one",getCurrentTime()));
        mItems.add(new TodoItem("item two",getCurrentTime()));
        mListView = findViewById(R.id.itemsListView);
        mDB = ToDoItemDB.getDatabase(this.getApplication().getApplicationContext());
        mToDoItemDao = mDB.toDoItemDao();
        readItemsFromDatabase();
        sortByTime();
        mItemsAdapter = new ItemsAdapter(this, mItems);
        mListView.setAdapter(mItemsAdapter);
        setListViewListener();
    }

    /**
     * return current time in sting type
     */
    private String getCurrentTime(){
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String current = sdf.format(date);
        return current;
    }


    /**
     * sort the todolist by creation time
     */
    private void sortByTime(){
        Collections.sort(mItems, new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem s1, TodoItem s2) {
                try {
                    return -msdf.parse(s1.getTime()).compareTo(msdf.parse(s2.getTime()));
                } catch (ParseException e) {
                    Log.e("sortByTime", e.getStackTrace().toString());
                }
                return 0;
            }
        });
    }

    /**
     * respond to user when click the addNew Button by bring the user to the EditToDoItemActivity page,
     * then update the items list if user enter a new todo_item
     * @param view
     */
    public void onAddNewClick(View view){
        Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);
        if(intent != null){
            startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
            mItemsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * handle the result passed from EditToDoItemActivity.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // handle the result of modification of todolist and update the timestamp of the item
        if(requestCode == EDIT_ITEM_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                String editedItem = data.getExtras().getString("item");
                int position = data.getIntExtra("position", -1);
                mItems.set(position, new TodoItem(editedItem, getCurrentTime()));
                Toast.makeText(this,"updated" + editedItem, Toast.LENGTH_SHORT).show();
                sortByTime();
                mItemsAdapter.notifyDataSetChanged();
                saveItemsToDatabase();
            }
        }

        // handle the result of adding new item to the todolist, and initialize the creation time.
        if(requestCode == ADD_ITEM_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                String content = data.getExtras().getString("item");
                if(content != null && content.length() >0){
                    TodoItem newItem = new TodoItem(content, getCurrentTime());
                    mItemsAdapter.add(newItem);
                    sortByTime();
                    saveItemsToDatabase();
                }
            }
        }
    }


    private void setListViewListener(){
        // responsible for sending the before-edited content to the EditToDoItemActivity, then update the change of the todolist.
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TodoItem updateItem = mItemsAdapter.getItem(position);
                Log.i("MainActivity", "Clicked item " + position + ": " + updateItem);

                Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);
                if (intent != null) {
                    // put "extras" into the bundle for access in the edit activity
                    intent.putExtra("item", updateItem.getTitle());
                    intent.putExtra("time",updateItem.getTime());
                    Log.i("setListViewListener", "the content been passed to EditToDoItem");
                    intent.putExtra("position", position);
                    // brings up the second activity
                    startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
                    mItemsAdapter.notifyDataSetChanged();
                }
            }
        });


        // set the longClick to be delete of the item, set up positive bottom to confirm deleteion, set up negative button to cancel deletion
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.long_click_warning_title)
                        .setMessage(R.string.long_click_msg)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mItems.remove(position);
                                mItemsAdapter.notifyDataSetChanged();
                                saveItemsToDatabase();
                                sortByTime();
                            }
                        })
                        .setNegativeButton(R.string.negate, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // user decided to cancel the deletion
                            }
                        });
                builder.create().show();
                return true;
            }
        });
    }

    private void readItemsFromDatabase(){
        try{
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                    List<TodoItem> itemsFromDB = mToDoItemDao.listAll();
                    mItems = new ArrayList<TodoItem>();
                    if(itemsFromDB != null & itemsFromDB.size() > 0){
                        for(TodoItem item : itemsFromDB){
                            mItems.add(item);
                        }
                    }
                    return null;
                }
            }.execute().get();
        } catch (Exception e){
            Log.e("readItemsFromDatabase", e.getStackTrace().toString());
        }
    }

    private void saveItemsToDatabase(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                mToDoItemDao.deleteAll();
                for(int i = 0; i < mItems.size(); i++){
                    mToDoItemDao.insert(mItems.get(i));
                }
                return null;
            }
        }.execute();
    }
}
