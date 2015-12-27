package y.Curriculum;

import android.app.Activity;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import y.Curriculum.DBHelper;


public class Insert extends Activity {
    
	public int d;
	public int c_no;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert);//实现布局页面的跳转
        d=getIntent().getIntExtra("d", 0);//获取从Intent传来的int值
        c_no=getIntent().getIntExtra("c", 0);
        String day=null;
        String c=null;
        
        switch(d){
		case 0:
			day=getString(R.string.mon);
			break;
		case 1:
			day=getString(R.string.tue);
			break;
		case 2:
			day=getString(R.string.wed);
			break;
		case 3:
			day=getString(R.string.thu);
			break;
		case 4:
			day=getString(R.string.fri);
			break;
		case 5:
			day=getString(R.string.sat);
			break;
		case 6:
			day=getString(R.string.sun);
			break;
		}
        
        switch(c_no){
		case 0:
			c=getString(R.string.c0);
			break;
		case 1:
			c=getString(R.string.c1);
			break;
		case 2:
			c=getString(R.string.c2);
			break;
		case 3:
			c=getString(R.string.c3);
			break;
		case 4:
			c=getString(R.string.c4);
			break;
		case 5:
			c=getString(R.string.c5);
			break;
		}
        setTitle(day+" "+c);//设置标题
        
        final EditText et1=(EditText)findViewById(R.id.new_class);
        final EditText et2=(EditText)findViewById(R.id.new_address);
        final EditText et3=(EditText)findViewById(R.id.new_week);
        //Android编程中对EditText等控件使用setText()函数要求EditText为final
        Button ok_b=(Button)findViewById(R.id.insert_ok);
        ok_b.setOnClickListener(new OnClickListener(){//设置监听器

			@Override
			public void onClick(View arg0) {
				
				if(et1.length()!=0)//如果课程名字不为空
				{
				DBHelper dbh=new DBHelper(Insert.this);
				SQLiteDatabase db=dbh.getWritableDatabase();
				try{
					db.execSQL("insert into event (day_no,class_no,class_name,class_address,class_week) values ('"+Integer.toString(d)+"','"+Integer.toString(c_no)+"','"+(et1.getText())+"','"+(et2.getText())+"','"+(et3.getText())+"')");
					Toast.makeText(Insert.this,R.string.insert_suc, Toast.LENGTH_SHORT).show();
				}
				
				catch(SQLException e){
					Toast.makeText(Insert.this,R.string.insert_fail, Toast.LENGTH_SHORT).show();
				}
				finally{
					
				}
				finish();
				}
				else
				{
					Toast.makeText(Insert.this,R.string.insert_none, Toast.LENGTH_SHORT).show();
				}
			}
        	
        });
        
        Button cancel_b=(Button)findViewById(R.id.insert_cancel);
        cancel_b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				finish();//点击取消则结束
			}
        	
        });
    }
}