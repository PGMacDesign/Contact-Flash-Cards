package pgmacdesign.contactflashcards;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pgmacdesign.pgmactips.adaptersandlisteners.CustomClickCallbackLink;
import com.pgmacdesign.pgmactips.adaptersandlisteners.CustomLongClickCallbackLink;
import com.pgmacdesign.pgmactips.adaptersandlisteners.OnTaskCompleteListener;
import com.pgmacdesign.pgmactips.layoutmanagers.CustomGridLayoutManager;
import com.pgmacdesign.pgmactips.misc.PGMacTipsConstants;
import com.pgmacdesign.pgmactips.utilities.ContactUtilities;
import com.pgmacdesign.pgmactips.utilities.L;
import com.pgmacdesign.pgmactips.utilities.NumberUtilities;
import com.pgmacdesign.pgmactips.utilities.PermissionUtilities;
import com.pgmacdesign.pgmactips.utilities.ProgressBarUtilities;

public class MainActivity extends AppCompatActivity implements CustomClickCallbackLink, CustomLongClickCallbackLink, OnTaskCompleteListener {

    //Vars
    private boolean allPermsSet;
    private ContactUtilities contactUtilities;

    //UI
    private TextView content_main_tv;
    private RecyclerView content_main_recyclerview;
    private SearchView content_main_search_view;
    private RelativeLayout content_main_main_layout;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    //UI Var combos
    private CustomGridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private AdapterContacts adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.getInstance();
        initVariables();
        initUI();
        requestPerms();
    }

    private void initVariables(){
        this.allPermsSet = false;
        this.gridLayoutManager = new CustomGridLayoutManager(this,
                GridLayoutManager.VERTICAL, false,
                TypedValue.COMPLEX_UNIT_PX, (MyApplication.getDMU().getPixelsWidth() / 3));
        this.linearLayoutManager = new LinearLayoutManager(this);
        this.adapter = new AdapterContacts(this, this, this);
        this.contactUtilities = new ContactUtilities.Builder(this, this)
                .removeBlockListContacts().setActivity(this).build();
    }

    private void initUI(){
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        this.content_main_tv = (TextView) this.findViewById(R.id.content_main_tv);
        this.content_main_recyclerview = (RecyclerView) this.findViewById(R.id.content_main_recyclerview);
        this.content_main_search_view = (SearchView) this.findViewById(R.id.content_main_search_view);
        this.content_main_main_layout = (RelativeLayout) this.findViewById(R.id.content_main_main_layout);

        setSupportActionBar(toolbar);
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllContacts();
            }
        });
        this.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        this.content_main_recyclerview.setLayoutManager(gridLayoutManager);
        this.content_main_recyclerview.setAdapter(adapter);

    }

    private void requestPerms(){
        if(Build.VERSION.SDK_INT >= 23){
            allPermsSet = PermissionUtilities.permissionsRequestShortcutReturn(this, new PermissionUtilities
                    .permissionsEnum[]{PermissionUtilities.permissionsEnum.WRITE_EXTERNAL_STORAGE,
                    PermissionUtilities.permissionsEnum.READ_CONTACTS});
        } else {
            allPermsSet = false;
        }
    }

    private void getAllContacts(){
        if(!allPermsSet){
            requestPerms();
            return;
        }
        showProgressBar(true);
        contactUtilities.queryContacts(null, null);
    }

    private void showProgressBar(boolean show){
        try {
            if (show) {
                ProgressBarUtilities.showSVGProgressDialog(this, -1);
            } else {
                ProgressBarUtilities.dismissProgressDialog();
                ;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(@Nullable Object o, @Nullable Integer tag, @Nullable Integer pos) {
        if(NumberUtilities.getInt(tag) > 0){

        }
    }

    @Override
    public void itemLongClicked(@Nullable Object o, @Nullable Integer tag, @Nullable Integer pos) {
        if(NumberUtilities.getInt(tag) > 0){

        }
    }

    @Override
    public void onTaskComplete(Object o, int i) {
        showProgressBar(false);
        switch (i){
            case PGMacTipsConstants.TAG_CONTACT_QUERY_EMAIL:
                L.m("TAG_CONTACT_QUERY_EMAIL");
                break;

            case PGMacTipsConstants.TAG_CONTACT_QUERY_NAME:
                L.m("TAG_CONTACT_QUERY_NAME");
                break;

            case PGMacTipsConstants.TAG_CONTACT_QUERY_PHONE:
                L.m("TAG_CONTACT_QUERY_PHONE");
                break;


            case PGMacTipsConstants.TAG_CONTACT_QUERY_ALL_MERGED_RESULTS:
                L.m("TAG_CONTACT_QUERY_ALL_MERGED_RESULTS");
                break;


            case PGMacTipsConstants.TAG_CONTACT_QUERY_UNKNOWN_ERROR:
                L.m("unknown error...");
                break;


        }
    }
}
