package com.example.lockscreentest;

import com.example.pedagogijadidaktika2.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LockScreen extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_screen);
		
		Button btnUgasi = (Button) findViewById(R.id.btnGasi);
		
		btnUgasi.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				kill();
			}
		});

		
	}
	
	private void kill(){
		finish();
	}



	/**
	 * A placeholder fragment containing a simple view.
	 */


}
