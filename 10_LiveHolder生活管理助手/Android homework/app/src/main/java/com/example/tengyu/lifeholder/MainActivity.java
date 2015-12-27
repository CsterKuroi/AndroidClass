package com.example.tengyu.lifeholder;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.tengyu.lifeholder.tomato.tomatoIO;
import com.example.tengyu.lifeholder.tomato.tomatoTask;
import com.example.tengyu.lifeholder.tomato.tomatoTaskAdapter;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public final static String PAR_KEY = "com.andy.par";
    private boolean ifIflate;
    private boolean ifCount;
    private String countTitle;
    private int selectPosition;
    private GoogleApiClient client;
    private tomatoIO tomatoMod;
    private SwipeMenuListView listView;
    private ScaleInAnimationAdapter animationAdapter;
    private NiftyDialogBuilder deleteWarning;
    private com.melnykov.fab.FloatingActionButton fab;
    private com.melnykov.fab.FloatingActionButton count_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tomatoMod = new tomatoIO(getSharedPreferences("tomato_data",MODE_PRIVATE));
        tomatoMod.flush();

        ifCount = false;
        ifIflate = true;
        countTitle = "";

        listView = (SwipeMenuListView) findViewById(R.id.tomatotask_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                view.setSelected(true);
                if(!ifCount){
                    if(tomatoMod.get(position).repOK()){
                        Intent intent = new Intent("com.example.tengyu.lifeholder.ACTION_COUNT");
                        countTitle =tomatoMod.get(position).getTitle();
                        intent.putExtra("TITLE",countTitle);
                        if(tomatoMod.get(position).IfRemind()){
                            intent.putExtra("REMIND", "TRUE");
                        }
                        else
                            intent.putExtra("REMIND", "FALSE");
                        //tomatoMod.start();
                        ifCount = true;
                        startActivity(intent);
                    }
                }
                else{
                    if(tomatoMod.get(position).getTitle().equals(countTitle)){
                        Intent intent = new Intent("com.example.tengyu.lifeholder.ACTION_COUNT");
                        startActivity(intent);
                    }
                }
            }
        });

        deleteWarning = NiftyDialogBuilder.getInstance(this);
        deleteWarning.setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWarning.dismiss();
                tomatoMod.remove(selectPosition);
                flush();
                Toast.makeText(getApplicationContext(), "Task deleted!", Toast.LENGTH_SHORT).show();
            }
        });

        deleteWarning.setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWarning.dismiss();//DO NOTHING
            }
        });
        deleteWarning.setCancelable(true);

        SwipeMenuCreator creator = new SwipeMenuCreator(){
            @Override
            public void create(SwipeMenu menu){
                // create "edit" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xFE, 0xAE,
                        0x6E)));
                // set item width
                openItem.setWidth(dp2px(75));
                // set item title
                openItem.setIcon(R.drawable.ic_action_edit);
                // add to menu
                menu.addMenuItem(openItem);

                //create delete option
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xE9,
                        0x3F, 0x35)));
                // set item width
                deleteItem.setWidth(dp2px(75));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Log.d("ListSelect", String.valueOf(position));
                Log.d("MenuClick", String.valueOf(index));
                switch (index) {
                    case 0:
                        Intent intent = new Intent("com.example.tengyu.lifeholder.ACTION_EDIT");
                        Bundle bundle = new Bundle();
                        tomatoTask tomatoTp = tomatoMod.get(position);
                        bundle.putParcelable(PAR_KEY, tomatoTp);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, position + 1);
                        break;
                    case 1:
                        deleteWarning.withTitle(getResources().getString(R.string.message_title_warning))
                                .withMessage(" " + getResources().getString(R.string.message_content_remove))             //.withMessage(null)  no Msg
                                .withDialogColor(getResources().getColor(R.color.themeLight))
                                .withDialogColor(getResources().getColor(R.color.theme))
                                .withIcon(getResources().getDrawable(R.drawable.ic_action_warning))
                                .withDuration(500)                                          //def
                                .withEffect(Effectstype.RotateBottom)                               //def Effectstype.Slidetop
                                .withButton1Text(getResources().getString(R.string.message_ok))                                      //def gone
                                .withButton2Text(getResources().getString(R.string.message_cancel))                                  //def gone
                                .isCancelableOnTouchOutside(true).show();
                        selectPosition = position;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


        fab = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent("com.example.tengyu.lifeholder.ACTION_EDIT");
                Bundle bundle = new Bundle();
                tomatoTask tomatoTp = new tomatoTask();
                bundle.putParcelable(PAR_KEY, tomatoTp);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
        count_fab = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.count_fab);
        count_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ifCount){
                    Intent intent = new Intent("com.example.tengyu.lifeholder.ACTION_COUNT");
                    startActivity(intent);
                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        setIntent(intent);//must store the new intent unless getIntent() will return the old one
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        String request;
        if((request = getIntent().getStringExtra("RESULT"))!=null){
            if(!request.equals("")){
                tomatoMod.tomato(request);
                tomatoMod.save();
                ifIflate = true;
                Toast.makeText(this, "Task finished!", Toast.LENGTH_SHORT).show();
            }
            getIntent().removeExtra("RESULT");
            ifCount = false;
        }

        if(ifIflate)
            flush();

        if(ifCount){
            fab.setVisibility(View.GONE);
            count_fab.setVisibility(View.VISIBLE);
            count_fab.attachToListView(listView);
        }
        else{
            count_fab.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
            fab.attachToListView(listView);
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.tengyu.lifeholder/http/host/path")
        );

        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        tomatoMod.save();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.tengyu.lifeholder/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private void flush(){
        animationAdapter = new ScaleInAnimationAdapter(new tomatoTaskAdapter(MainActivity.this,  R.layout.tomatotask_item, tomatoMod.getList()));
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);
        ifIflate = false;
    }


    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case 0:
                if(resultCode == RESULT_OK){
                    tomatoTask tomatoTp = data.getParcelableExtra(MainActivity.PAR_KEY);
                    tomatoMod.insert(tomatoTp);
                    Toast.makeText(this, "New task joined!", Toast.LENGTH_SHORT).show();
                    ifIflate = true;
                }
                break;
            default:
                if(resultCode == RESULT_OK) {
                    tomatoTask tomatoTp = data.getParcelableExtra(MainActivity.PAR_KEY);
                    tomatoTp.setDate(tomatoMod.get(requestCode - 1).getDate());
                    tomatoMod.set(requestCode - 1, tomatoTp);
                    Toast.makeText(this,"Task list updated!",Toast.LENGTH_SHORT).show();
                    ifIflate = true;
                }
                break;
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
