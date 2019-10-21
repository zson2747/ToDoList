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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * create a customized ArrayAdapter for todolist items.
 */
public class ItemsAdapter extends ArrayAdapter<TodoItem> {
    /**
     * initialized the Adapter
     * @param context
     * @param todoList
     */
    public ItemsAdapter(Context context, ArrayList<TodoItem> todoList) {
        super(context, 0, todoList);
    }

    /**
     * Convert the content and timestamp of the each item in the todolist to a view.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        TodoItem item = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list,parent, false);
        }
        TextView tvContent = (TextView)convertView.findViewById(R.id.contentTextView);
        TextView tvTimeStamp = (TextView)convertView.findViewById(R.id.timeStampTextView);
        tvContent.setText(item.getTitle());
        tvTimeStamp.setText(item.getTime());
        return convertView;
    }
}
