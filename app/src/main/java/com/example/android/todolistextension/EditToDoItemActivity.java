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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * for user to enter text to modify the existing todolist item, or enter text of new item to the todolist.
 * Package all the information entered by user and send back to MainActivity;
 */
public class EditToDoItemActivity extends Activity
{
	public int position=0;
	private EditText mEtItem;
	private TextView mTvTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//populate the screen using the layout
		setContentView(R.layout.activity_edit_item);
		//Get the data from the main screen
		String editItem = getIntent().getStringExtra("item");
		String time = getIntent().getStringExtra("time");
		position = getIntent().getIntExtra("position",-1);
		// show original content in the text field
		mEtItem = findViewById(R.id.etEditItem);
		mTvTime = findViewById(R.id.tvTime);
		// if the user is adding the new todoList, time will be none. TextView will not show any content when user is adding new item.
		if(time != null){
			mTvTime.setText(time);
			mEtItem.setText(editItem);
		}
	}

	/**
	 * handle the activity when uer click cancel button.
	 * set up the positive button to undo all the text been entered
	 * set up the negative button to go back to text-entering-process;
	 * @param v
	 */
	public void onCancel(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(EditToDoItemActivity.this);
		builder.setTitle(R.string.cancel_warning_title)
				.setMessage(R.string.cancel_warning_msg)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						finish();
					}
				})
				.setNegativeButton(R.string.negate, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						// user choose to continue modifying the text
					}
				});
		builder.create().show();
	}

	/**
	 * package all the information entered by the user and send back to the MainActivity
	 * @param v
	 */
	public void onSubmit(View v) {
		mEtItem = findViewById(R.id.etEditItem);
		// Prepare data intent for sending it back
		Intent data = new Intent();
		// Pass relevant data back as a result
		data.putExtra("item", mEtItem.getText().toString());
		data.putExtra("position", position);
		// Activity finished ok, return the data
		setResult(RESULT_OK, data); // set result code and bundle data for response
		finish(); // closes the activity, pass data to parent
	} 
}
