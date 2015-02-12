package com.example.squiz;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Models.Group;
import com.example.Models.Student;
import com.example.adapters.ListAdapter;
import com.example.httpRequest.StudentApi;

public class StudentsInGroupActivity extends ListActivity {
	private List<Student> students;
	private ActionBar actionBar;
	private List<Student> itemsToDelete;
	private ListAdapter<Student> StudentAdapter;
	StudentApi task;
	String email;
	String auth_token_string;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groupdetails);
		
		String StudentName = getIntent().getExtras().getString("Student");
		int groupId=getIntent().getExtras().getInt("groupID");
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(StudentName);
		
		students = new ArrayList<>();
		itemsToDelete = new ArrayList<>();
		RestAdapter restAdapter1= new RestAdapter.Builder()
		.setEndpoint(WelcomeActivity.ENDPOINT)  //call base url
		.setLogLevel(LogLevel.FULL)
		.build();

		task = restAdapter1.create(StudentApi.class);
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(StudentsInGroupActivity.this);
		auth_token_string = settings.getString("authToken", "");
		email=settings.getString("email", "");
		task.requestStudents(email,auth_token_string,groupId,new Callback<List<Student>>() {

			@Override
			public void success(List<Student> arg0, Response arg1) {
				students=arg0;
				StudentAdapter = new ListAdapter<Student>(StudentsInGroupActivity.this, 
						R.layout.custom_list_item, students);
				setListAdapter(StudentAdapter);
			}

			@Override
			public void failure(RetrofitError arg0) {
				Toast.makeText(StudentsInGroupActivity.this, "bad", Toast.LENGTH_LONG).show();

			}
		});
		ListView listView = getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.context_menu, menu);
				return true;
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
	            case R.id.action_delete:
	                deleteSelectedItems();
	                mode.finish(); // Action picked, so close the CAB
	                return true;
	            default:
	                return false;
				}
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {
				if (checked) 
					itemsToDelete.add(students.get(position));
				else
					itemsToDelete.remove(students.get(position));
			}
		});
	}

	
	private void deleteSelectedItems() {
		for (Student s : itemsToDelete)
			students.remove(s);
	}
	
 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_bar_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_add) {
			alertCustom();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void alertCustom() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Enter student E-mail: ");
		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.add_student_alert, null);
		final EditText et = (EditText) v.findViewById(R.id.student_email);
		builder.setView(v);
		builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
}
