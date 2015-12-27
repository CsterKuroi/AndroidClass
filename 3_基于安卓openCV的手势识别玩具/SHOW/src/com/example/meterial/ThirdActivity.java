package com.example.meterial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ThirdActivity extends Activity{
	
	private TasksCompletedView mTasksView;
	private int mTotalProgress;
	private int mCurrentProgress;
	
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_third);
		 
		 View fab = findViewById(R.id.fab_button);
		 fab.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                Intent intent = new Intent(ThirdActivity.this,ColorActivity.class);
	                startActivity(intent);
	            }
	        });
		
		initVariable();
		initView();	
		new Thread(new ProgressRunable()).start();
	}
		
		private void initVariable() {
			mTotalProgress = 100;
			mCurrentProgress = 0;
		}
		
		private void initView() {
			mTasksView = (TasksCompletedView) findViewById(R.id.tasks_view);
		}
		
		class ProgressRunable implements Runnable {

			@Override
			public void run() {
				while (mCurrentProgress < mTotalProgress) {
					mCurrentProgress += 1;
					mTasksView.setProgress(mCurrentProgress);
					try {
						Thread.sleep(20);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} 
		}	
}