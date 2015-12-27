package y.Curriculum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import y.Curriculum.DBHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;

public class Day extends Activity {
    private static final String NAME = "NAME";
    private static final String IS_EVEN = "IS_EVEN";
    public static final String TABLE_NAME="event";
    public int d;
	public String day = null;
	public String c=null;
	public int c_no;
	private int selectedIndex = -1;
    private ExpandableListAdapter mAdapter;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day);
        showView();
        

        
    }
    public void showView(){	
        ExpandableListView elv=(ExpandableListView)findViewById(R.id.elv);
        elv.setGroupIndicator(null);//�趨ÿ��Group֮ǰ���Ǹ�ͼ��,����û������
        d=getIntent().getIntExtra("d", 0);
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
        
        TextView tv=(TextView)findViewById(R.id.day_tv);
        tv.setText(day);
       
        
        DBHelper dbh=new DBHelper(this);     
        SQLiteDatabase db=dbh.getWritableDatabase();
        Cursor cursor;
        int a;
        
      
        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        //����Ľṹ����
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
        //�ڶ���Ľṹ����,������Կ����Ǳȶ������˸�List�����ڶ���list�е�list������Ҳ�Ǽ�ֵ�ԣ����Զ����list<list<map>>>
        for (int i = 0; i < 6; i++) {
            Map<String, String> curGroupMap = new HashMap<String, String>();
          //����curGroupMap��Map�ṹ���洢��Ҫ�õ��ļ�ֵ��
            groupData.add(curGroupMap);
          //��ӵ�List�У�map�ṹ��Ϊ����������list�еĵ�Ԫ��map�ṹ�洢�����ݣ���ʾ����������б�
            cursor=db.query("event", new String[]{"class_name","class_address","class_week"}, "day_no="+Integer.toString(d)+" and class_no="+Integer.toString(i), null, null, null, null);
            a=cursor.getCount();
            cursor.moveToFirst();//ÿ�ζ�ȡ�����ݿ�ʱ��cusorָ��Ĳ��ǵ�һ����¼,����ÿ�ζ���Ҫmovetofirst
           
            
            switch(i){
            case 0:
            	if(a==0)
            		curGroupMap.put(NAME, getString(R.string.c0)+getString(R.string.none));
            	else
            		curGroupMap.put(NAME, getString(R.string.c0));
            	break;
            case 1:
            	if(a==0)
            		curGroupMap.put(NAME, getString(R.string.c1)+getString(R.string.none));
            	else
            		curGroupMap.put(NAME, getString(R.string.c1));
            	break;
            case 2:
            	if(a==0)
            		curGroupMap.put(NAME, getString(R.string.c2)+getString(R.string.none));
            	else
            		curGroupMap.put(NAME, getString(R.string.c2));
            	break;
            case 3:
            	if(a==0)
            		curGroupMap.put(NAME, getString(R.string.c3)+getString(R.string.none));
            	else
            		curGroupMap.put(NAME, getString(R.string.c3));
            	break;
            case 4:
            	if(a==0)
            		curGroupMap.put(NAME, getString(R.string.c4)+getString(R.string.none));
            	else
            		curGroupMap.put(NAME, getString(R.string.c4));
            	break;
            case 5:
            	if(a==0)
            		curGroupMap.put(NAME, getString(R.string.c5)+getString(R.string.none));
            	else
            		curGroupMap.put(NAME, getString(R.string.c5));
            	break;
            }

            
            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for (int j = 0; j < a; j++) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(NAME, cursor.getString(0));
               String detail= getText(R.string.dialog_address)+cursor.getString(1)+"  "+getText(R.string.week)+cursor.getString(2);
               curChildMap.put(IS_EVEN, detail);
                cursor.moveToNext();

            }
            childData.add(children);
        }
       
        mAdapter = new SimpleExpandableListAdapter(
                this,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { NAME, IS_EVEN },
                new int[] { android.R.id.text1, android.R.id.text2},
                childData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] { NAME, IS_EVEN },
                new int[] { android.R.id.text1, android.R.id.text2}
                );
        elv.setAdapter(mAdapter);
        this.registerForContextMenu(elv);
        int groupCount = mAdapter.getGroupCount();
        for(int i = 0; i < groupCount; i ++)
            elv.expandGroup(i);
        /*
         * ʵ��չ����ExpandableListView�����ַ�ʽ֮SimpleExpandableListAdapter
         */
    }
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		ExpandableListContextMenuInfo info=(ExpandableListContextMenuInfo)menuInfo;
		
		int type=ExpandableListView.getPackedPositionType(info.packedPosition);
		if(type==ExpandableListView.PACKED_POSITION_TYPE_GROUP){
          
			switch(ExpandableListView.getPackedPositionGroup(info.packedPosition)){
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
			menu.setHeaderTitle(day+"_"+c);
			menu.add(0, 1, 0, R.string.group_insert);//��������
		}
		
		else{
			ExpandableListView elv= (ExpandableListView)findViewById(R.id.elv);
			String s=(String)((Map<String,String>)elv.getExpandableListAdapter().getChild(ExpandableListView.getPackedPositionGroup(info.packedPosition), ExpandableListView.getPackedPositionChild(info.packedPosition))).get(NAME);
			menu.setHeaderTitle(s);
			menu.add(0, 2, 0, R.string.del);
			
		}
	}
    
    @Override
    public boolean onContextItemSelected(MenuItem item){//�����˵���Ӧ����
    	
		switch(item.getItemId()){
		case 1:
			c_no=ExpandableListView.getPackedPositionGroup(((ExpandableListContextMenuInfo)item.getMenuInfo()).packedPosition);
			Intent i=new Intent(Day.this,Insert.class);
			i.putExtra("d", d);
			i.putExtra("c", c_no);
			startActivity(i);//��ת��insert
			
			break;
		case 2:
			
			ExpandableListContextMenuInfo info=(ExpandableListContextMenuInfo)item.getMenuInfo();
			ExpandableListView elv= (ExpandableListView)findViewById(R.id.elv);
			String s=(String)((Map<String,String>)elv.getExpandableListAdapter().getChild(ExpandableListView.getPackedPositionGroup(info.packedPosition), ExpandableListView.getPackedPositionChild(info.packedPosition))).get(NAME);
			
			int l=ExpandableListView.getPackedPositionGroup(((ExpandableListContextMenuInfo)item.getMenuInfo()).packedPosition);
			DBHelper dbh=new DBHelper(this);
			SQLiteDatabase db=dbh.getWritableDatabase();
			
			try{
				db.execSQL("delete from event where day_no="+Integer.toString(d)+" and class_no="+Integer.toString(l)+" and class_name='"+s+"'");
				Toast.makeText(this,R.string.del_suc, Toast.LENGTH_SHORT).show();
			}
			catch(Exception e){
				Toast.makeText(this,R.string.del_fail, Toast.LENGTH_SHORT).show();
			}
			finally{
				
			}
		
			
			return true;
		}
		
		return false;
		
	}
   
    
}
