package com.example.wei.spark;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private String user_name;

    private TextView tvName;
    private Button btnPost;
    private Button btnContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        tvName = (TextView)super.findViewById(R.id.tvName);
        btnPost = (Button)super.findViewById(R.id.btnPost);

        Intent intent = super.getIntent();
        user_name = intent.getStringExtra("user_name");
        tvName.setText("您好，" + user_name); //读取传入的参数
        btnContent = (Button)super.findViewById(R.id.btnContent);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //提交文字
                Intent intent = new Intent(ProfileActivity.this, PostActivity.class);
                intent.putExtra("user_name", user_name);
                startActivity(intent);
            }
        });
        btnContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //查看文字
                Intent intent = new Intent(ProfileActivity.this, ContentActivity.class);
                intent.putExtra("user_name", user_name);
                startActivity(intent);
            }
        });
    }
}
