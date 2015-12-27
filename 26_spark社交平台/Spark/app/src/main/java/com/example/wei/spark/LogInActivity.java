package com.example.wei.spark;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {

    private EditText etUserName = null;
    private EditText etPassword = null;
    private Button btnReg = null;
    private Button btnLogIn = null;
    private TextView tvInfo = null;

    private String user_name = null;
    private String password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_activity);

        etUserName = (EditText)super.findViewById(R.id.etUserName);
        etPassword = (EditText)super.findViewById(R.id.etPassword);
        btnReg = (Button)super.findViewById(R.id.btnReg);
        btnLogIn = (Button)super.findViewById(R.id.btnLogIn);
        tvInfo = (TextView)super.findViewById(R.id.tvInfo);


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name = etUserName.getText().toString();
                password = etPassword.getText().toString();
                if (reg())
                    tvInfo.setText("注册成功");
                else
                    tvInfo.setText("注册失败");
            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name = etUserName.getText().toString();
                password = etPassword.getText().toString();
                if (logIn()) {
                    tvInfo.setText("登录成功");
                    Intent intent = new Intent(LogInActivity.this, ProfileActivity.class);
                    intent.putExtra("user_name", user_name); //将用户名作为参数传递
                    startActivity(intent); //打开新的界面
                }
                else
                    tvInfo.setText("登录失败");
            }
        });
    }

    private boolean reg(){
        if (user_name.length() <= 0){
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() <= 0){
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        LogInDBHelper db = new LogInDBHelper(LogInActivity.this, "LogInInfo", null, 1);
        String sql = "select * from login_table where user_name = ?";
        Cursor cursor = db.getWritableDatabase().rawQuery(sql, new String[] {user_name});
        if (cursor.getCount() > 0){ //找到了同样的用户名记录
            Toast.makeText(getApplicationContext(), "用户名已存在", Toast.LENGTH_SHORT).show();
            db.close();
            return false;
        }

        if (!db.addUser(user_name, password)){ //在数据库中添加用户数据
            Toast.makeText(getApplicationContext(), "注册失败，出现了奇怪的错误", Toast.LENGTH_SHORT).show();
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    private boolean logIn(){
        if (user_name.length() <= 0){
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() <= 0){
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        LogInDBHelper db = new LogInDBHelper(LogInActivity.this, "LogInInfo", null, 1);
        String sql = "select * from login_table where user_name = ?";
        Cursor cursor = db.getWritableDatabase().rawQuery(sql, new String[] {user_name});
        if (cursor.getCount() <= 0){ //找不到用户
            Toast.makeText(getApplicationContext(), "用户名不存在", Toast.LENGTH_SHORT).show();
            db.close();
            return false;
        }
        cursor.moveToFirst();
        if (!cursor.getString(cursor.getColumnIndex("password")).equals(password)) { //密码不正确
            Toast.makeText(getApplicationContext(), "密码不正确", Toast.LENGTH_SHORT).show();
            db.close();
            return false;
        }
        db.close();
        return true;
    }
}
