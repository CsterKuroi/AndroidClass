package com.example.wei.spark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PostActivity extends AppCompatActivity {

    private String user_name = null;

    private EditText etContent = null;
    private Button btnPost = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity);

        etContent = (EditText)super.findViewById(R.id.etContent);
        btnPost = (Button)super.findViewById(R.id.btnPost);

        Intent intent = super.getIntent();
        user_name = intent.getStringExtra("user_name");

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //将某段文字写入数据库
                PostDBHelper db = new PostDBHelper(PostActivity.this, "PostTable", null, 1);
                if (!db.addPost(user_name, etContent.getText().toString())){
                    Toast.makeText(getApplicationContext(), "发布失败，出现了奇怪的错误", Toast.LENGTH_SHORT).show();
                }
                db.close();
                PostActivity.this.finish();
            }
        });
    }
}
