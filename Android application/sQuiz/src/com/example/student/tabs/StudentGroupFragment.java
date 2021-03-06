package com.example.student.tabs;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Models.Group;
import com.example.adapters.ListAdapter;
import com.example.httpRequest.GroupApi;
import com.example.squiz.R;
import com.example.squiz.WelcomeActivity;

public class StudentGroupFragment extends ListFragment {
	private List<Group> groups;
	private ListAdapter<Group> GroupAdapter;
	//private List<Group> itemsToDelete;
	GroupApi task;
	String email;
	String auth_token_string;
	Intent intent;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setHasOptionsMenu(true);
		groups = new ArrayList<Group>();
		//	itemsToDelete = new ArrayList<Group>();

		RestAdapter restAdapter1= new RestAdapter.Builder()
		.setEndpoint(WelcomeActivity.ENDPOINT)  //call base url
		.setLogLevel(LogLevel.FULL)
		.build();

		task = restAdapter1.create(GroupApi.class);
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
		auth_token_string = settings.getString("authToken", "");
		email=settings.getString("email", "");
		task.studentRequestGroups(email,auth_token_string,"student", new Callback<List<Group>>() {

			@Override
			public void success(List<Group> arg0, Response arg1) {
				groups=arg0;
				GroupAdapter = new ListAdapter<Group>(getActivity(), 
						R.layout.custom_list_item, groups);
				setListAdapter(GroupAdapter);
				ListView listView = getListView();
				listView.setSelector(R.drawable.list_selector);
			}

			@Override
			public void failure(RetrofitError arg0) {
				Toast.makeText(getActivity(), "Failed for a reason", Toast.LENGTH_SHORT).show();
			}
		});
		return inflater.inflate(R.layout.fragment_quizzes, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		//	ListView listView = getListView();
		//	listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL) ;
		/* listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

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
					task.deleteGroups(email, auth_token_string, itemsToDelete, new Callback<JsonObject>() {
						@Override
						public void failure(RetrofitError arg0) {
							JsonObject obj=(JsonObject) arg0.getBody();
							String text=obj.get("error").toString() ;
									text=text.replace(':', ' ').replaceAll("\"", "");
							Toast.makeText(getActivity(),text, Toast.LENGTH_SHORT).show();
						}

						@Override
						public void success(JsonObject arg0, Response arg1) {
							String text=arg0.get("info").toString().replaceAll("\"", "") ;
						if(text.equals("deleted"))
							deleteSelectedItems();							
						}
					});
					mode.finish(); // Action picked, so close the CAB
					return true;
				default:
					return false;
				}
			}  

			@Override
			public void onDestroyActionMode(ActionMode mode) {

			}

		/*	@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {
				if (checked) 
					itemsToDelete.add(groups.get(position));
				else
					itemsToDelete.remove(groups.get(position));
			}  

		}); */
		super.onActivityCreated(savedInstanceState);
	}

	/*	private void deleteSelectedItems() {
		for (Group s : itemsToDelete)
		GroupAdapter.notifyDataSetChanged();
	} */

	/* @Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		 intent = new Intent();
		 intent.putExtra("groupID",groups.get(position).getId());

		alert(groups.get(position).toString());
	} 

	private void alert(final String selectedGroup) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(R.string.dialog_title)
		.setItems(R.array.items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				intent.putExtra("Group", selectedGroup);
				if (which == 1) {
					intent.setClass(getActivity(), StudentsInGroupActivity.class);
					startActivity(intent);
				}
				else {
					intent.setClass(getActivity(), QuizzesInGroupActivity.class);
					startActivity(intent);
				}
			}
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	} */

	/*private void alertCustom() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Enter group name: ");
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View v = inflater.inflate(R.layout.create_group_alert, null);
		final EditText et = (EditText) v.findViewById(R.id.group_name);
		builder.setView(v);
		builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String groupName=et.getText().toString();
				Group grp =new Group();
				grp.setName(groupName);
				task.addGroup(email, auth_token_string,grp, new Callback<Group>() {

					@Override
					public void failure(RetrofitError arg0) {
						JsonObject type=new JsonObject() ;
						JsonObject obj=(JsonObject) arg0.getBodyAs(type.getClass());
						String text=obj.get("error").toString();
						text=text.replace(':', ' ').replaceAll("\"", "");
						Toast.makeText(getActivity(),text, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void success(Group arg0, Response arg1) {
						groups.add(arg0);
						GroupAdapter.notifyDataSetChanged();
					}
				});

			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}  */

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.action_bar_quizzes, menu);
		getActivity().getActionBar().setTitle("Groups");	
		super.onCreateOptionsMenu(menu, inflater);

	}

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_add) {
			alertCustom();
			GroupAdapter.notifyDataSetChanged();
			return true;
		}
		return super.onOptionsItemSelected(item);
	} */
}
