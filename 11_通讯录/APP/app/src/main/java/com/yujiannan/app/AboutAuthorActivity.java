package com.yujiannan.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class AboutAuthorActivity extends Activity {
    private Button btn_about_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_author);

        btn_about_return = (Button)findViewById(R.id.btn_about_return);
        btn_about_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutAuthorActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
