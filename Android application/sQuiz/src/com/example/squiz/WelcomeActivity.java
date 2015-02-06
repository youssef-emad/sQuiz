package com.example.squiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class WelcomeActivity extends Activity {
	
	Button signUp;
	Button logIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_welcome);
		
		signUp = (Button) findViewById(R.id.signup);
		logIn = (Button) findViewById(R.id.login);
		
		signUp.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				startActivity(new Intent(WelcomeActivity.this, SignupActivity.class));
			}
		});
		
		logIn.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View v) {
				startActivity(new Intent(WelcomeActivity.this, AfterLoginInstructorActivity.class));
			}
		});
	}
}
