package com.food.trace;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;

import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
class MyException { }

class ReplicatePhoneException extends Exception { }

class PasswordNotEqualException extends Exception { }

class VerifyCodeFailException extends Exception { }

class NullItemException extends Exception {
	public NullItemException(String str) { 
		super(str);
	}
}
public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		// button register
		Button register = (Button) this.findViewById(R.id.register_register);
		register.setOnClickListener(new Button.OnClickListener() {
			
			EditText phone = (EditText) findViewById(R.id.register_phone);  
			EditText pswd1 = (EditText) findViewById(R.id.register_password);
			EditText pswd2 = (EditText) findViewById(R.id.register_confirm);
			EditText code  = (EditText) findViewById(R.id.register_code);
			
			public void onClick(View v) {
				try {
					registerClick(phone.getText().toString(), pswd1.getText().toString(), pswd2.getText().toString(), code.getText().toString());
				} catch (NullItemException e) {
					Toast.makeText(RegisterActivity.super, e.getMessage(), 100).show();
				} catch (PasswordNotEqualException e) {
					Toast.makeText(RegisterActivity.super, "���벻��ͬ��", 100).show();
				} catch (VerifyCodeFailException e) {
					Toast.makeText(RegisterActivity.super, "��֤�벻��ȷ��", 100).show();
				}
			}
		});
		
		// button send code
		Button sendcode = (Button) this.findViewById(R.id.register_sendcode);
		sendcode.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				EditText phone =(EditText) findViewById(R.id.register_phone);  
				try {
					sendCodeClick(phone.getText().toString());
				} catch (NullItemException e) {
					Toast.makeText(RegisterActivity.super, e.getMessage(), 100).show();
				} catch (ReplicatePhoneException e) {
					Toast.makeText(RegisterActivity.super, "�ú����ѱ�ע�ᣡ", 100).show();
				}
			}
		});
	}
	// ����һ�����û������ֻ���֤״̬Ϊfalse
	private void addUser(final String phoneNumber) throws ReplicatePhoneException {
		
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereEqualTo("mobilePhoneNumber", phoneNumber);
		query.setLimit(1);
		
		query.findObjects(this, new FindListener<User>() {
	        @Override
	        public void onSuccess(List<User> object) {
	            // TODO Auto-generated method stub
	        	if (object.size() >= 1) {
	        		Log.i("bmob", "��ѯ���Ѵ��ڵ��ֻ���");
	        		Toast.makeText(RegisterActivity.this, "���ֻ��ѱ�ע�ᣡ", 100).show();
	        		return;
	        	}
	    		
	    		BmobSMS.requestSMSCode(RegisterActivity.this, phoneNumber, "��֤��", new RequestSMSCodeListener() {
	    		    @Override
	    		    public void done(Integer smsId, BmobException ex) {
	    		        if(ex == null){ // ��֤�뷢�ͳɹ�
	    		            Log.i("bmob", "����id��" + smsId); // ���ڲ�ѯ���ζ��ŷ�������
	    		        } else {
	    		            Log.i("bmob", "errorCode = "  + ex.getErrorCode() 
	    		            	+ ",errorMsg = " + ex.getLocalizedMessage());
	    		        }
	    		    }
	    		});
	        }
	        
	        @Override
	        public void onError(int code, String msg) {
	        	Log.i("bmob", "��ѯʧ��");
	        	Toast.makeText(RegisterActivity.this, "������ʧ�ܣ������������ã�", 100).show();
	        }
		});
	}
		
	// ���������֤�밴ť���¼�
	private void sendCodeClick(String phoneNumber) throws NullItemException, ReplicatePhoneException {
		if (phoneNumber == null || phoneNumber.equals(""))
			throw new NullItemException("�ֻ��Ų���Ϊ�գ�");
		addUser(phoneNumber);
	}

	// ���ע�ᰴť���¼�
	private void registerClick(final String phoneNumber, final String pswd1, String pswd2, String code) 
		                                throws NullItemException, PasswordNotEqualException, VerifyCodeFailException {
		if (phoneNumber == null || phoneNumber.equals(""))
			throw new NullItemException("�ֻ��Ų���Ϊ�գ�");
		if (pswd1 == null || pswd1.equals(""))
			throw new NullItemException("���벻��Ϊ�գ�");
		if (pswd2 == null || pswd2.equals(""))
			throw new NullItemException("���벻��Ϊ�գ�");
		if (!pswd1.equals(pswd2))
			throw new PasswordNotEqualException();
		
		// �ж��Ƿ���֤�ɹ�
		BmobSMS.verifySmsCode(this, phoneNumber, code, new VerifySMSCodeListener() {

		    @Override
		    public void done(BmobException ex) {
		        if (ex == null){ // ������֤������֤�ɹ�����ʼע��
		            Log.i("bmob", "��֤ͨ��");
		            Toast.makeText(RegisterActivity.this, "��֤ͨ��", 100).show();
		         
		            // δ��ѯ���ظ������һ���¼�¼
		        	final User user = new User();
		    		user.setUsername(phoneNumber);
		    		user.setMobilePhoneNumber(phoneNumber);
		    		user.setPassword(pswd1);
		    		user.setMobilePhoneNumberVerified(true);
		    		user.signUp(RegisterActivity.this, new SaveListener() {
		    		    @Override
		    		    public void onSuccess() {
		    		    	Log.i("bmob", "ע��ɹ�");
		    		    	
//		                    // ��ʼ�����û��Ļ���·��
//		        			BmobConfiguration config = new BmobConfiguration.Builder(RegisterActivity.this).customExternalCacheDir(user.getUsername()).build();
//		        			BmobPro.getInstance(RegisterActivity.this).initConfig(config);
		        			
		    		    	// ע��ɹ�����ת����Ϣ���ƽ���
				    		Intent intent = new Intent();
			    			intent.setClass(RegisterActivity.this, CompleteInfoActivity.class);
			    			startActivity(intent);
			    			RegisterActivity.this.finish();
		    		    }

		    		    @Override
		    		    public void onFailure(int code, String msg) {
		    		    	Log.i("bmob", "ע��ʧ��");
		    		    	Toast.makeText(RegisterActivity.this, "ע��ʧ�ܣ������������ã�", 100).show();
		    		    }
		    		});
		    		
		        } else { // ��֤ʧ��
		            Log.i("bmob", "��֤ʧ�ܣ�code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
		            Toast.makeText(RegisterActivity.this, "��֤ʧ��", 100).show();
		        }
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
