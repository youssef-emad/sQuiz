package com.example.squiz;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.Models.LoginForm;
import com.example.httpRequest.LoginAPI;
import com.example.instructor.AfterLoginInstructorActivity;
import com.example.squiz.R;
import com.example.student.AfterLoginStudentActivity;
import com.google.gson.JsonObject;

public class WelcomeActivity extends Activity {
	
	 Button signUp;
	 Button logIn;
	 EditText Email;
	 EditText Password;
	 LoginForm user;
	 ProgressBar pb ;
	 SharedPreferences settings; 
	SharedPreferences.Editor prefEditor;
	String email;
	
	public static final String ENDPOINT = 
			"https://sQuiz.herokuapp.com/api";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_welcome);
		
		signUp = (Button) findViewById(R.id.signup);
		logIn = (Button) findViewById(R.id.login);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);
		settings = PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this);
		prefEditor = settings.edit();
    	
		signUp.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				startActivity(new Intent(WelcomeActivity.this, SignupActivity.class));
			}
		});

		
		
		logIn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {

				if(isOnline()){	
					Email = (EditText) findViewById(R.id.editTextEmail);
					Password = (EditText) findViewById(R.id.editTextPassword);
					 email = Email.getText().toString();
					String password = Password.getText().toString();
					
					try{
						user=new LoginForm();
						user.populateForm(email, password);
						pb.setVisibility(View.VISIBLE);
						sendData(user);
						
					}catch(Exception e){
						
						Toast.makeText(WelcomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
					}
					
				}
				else{
					Toast.makeText(WelcomeActivity.this, "the network is not available", Toast.LENGTH_SHORT).show();
				}
			}		
		});
	}
	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

	
	private void sendData(LoginForm user){

		RestAdapter restAdapter = new RestAdapter.Builder()
	    .setEndpoint(ENDPOINT)  //call base url
	    .setLogLevel(LogLevel.FULL)
	    .build();


	LoginAPI task = restAdapter.create(LoginAPI.class); //retrofit create api
	task.login(user ,new Callback<JsonObject>() {
	        @Override
	        public void success(JsonObject arg0, Response arg1) {
	        	
	        	pb.setVisibility(View.INVISIBLE);
	        String	authToken=arg0.get("auth_token").toString().replaceAll("\"", "");
	        String  accType  =arg0.get("type").toString().replaceAll("\"", "");
	        SharedPreferences.Editor editor = settings.edit();		
			editor.putString("authToken", authToken);
			editor.putString("email", email);
			editor.commit();
			
			if(accType.equals("instructor"))
	        	startActivity(new Intent(WelcomeActivity.this, AfterLoginInstructorActivity.class));
			else if(accType.equals("student"))
				startActivity(new Intent(WelcomeActivity.this, AfterLoginStudentActivity.class));
			
	        	}

	        @Override
	        public void failure(RetrofitError retrofitError) {
	        	pb.setVisibility(View.INVISIBLE);
	            Toast.makeText(WelcomeActivity.this, "Your Email or Password is incorrect", Toast.LENGTH_SHORT).show();
	        }
	    });
	}
		
		
}