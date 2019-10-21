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

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * create a todolist item data type to store the todo_content and the creation time of the item
 */
@Entity(tableName = "todoList")
public class TodoItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int itemId;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "time")
    private String mTime;

    /**
     * initialized the time and content of the todolist item
     * @param title
     * @param time
     */
    public TodoItem(String title, String time){
        this.mTime = time;
        this.mTitle = title;
    }

    public String getTitle(){
        return mTitle;
    }

    public int getItemId(){
        return itemId;
    }

    public String getTime(){
        return mTime;
    }

    public void setItemId(int itemId){
        this.itemId = itemId;
    }

    public void setTime(String time){
        this.mTime = time;
    }

    public void setTitle(String title){
        this.mTitle = title;
    }
}
