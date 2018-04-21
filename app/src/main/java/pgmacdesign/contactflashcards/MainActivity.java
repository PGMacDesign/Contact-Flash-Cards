package pgmacdesign.contactflashcards;

import android.annotation.SuppressLint;
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
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.pgmacdesign.pgmactips.adaptersandlisteners.CustomClickCallbackLink;
import com.pgmacdesign.pgmactips.adaptersandlisteners.CustomLongClickCallbackLink;
import com.pgmacdesign.pgmactips.adaptersandlisteners.OnTaskCompleteListener;
import com.pgmacdesign.pgmactips.layoutmanagers.CustomGridLayoutManager;
import com.pgmacdesign.pgmactips.misc.PGMacTipsConstants;
import com.pgmacdesign.pgmactips.utilities.ContactUtilities;
import com.pgmacdesign.pgmactips.utilities.L;
import com.pgmacdesign.pgmactips.utilities.MiscUtilities;
import com.pgmacdesign.pgmactips.utilities.NumberUtilities;
import com.pgmacdesign.pgmactips.utilities.PermissionUtilities;
import com.pgmacdesign.pgmactips.utilities.ProgressBarUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements CustomClickCallbackLink, CustomLongClickCallbackLink, OnTaskCompleteListener {

    //Vars
    private boolean allPermsSet, useContactsWithPhotosOnly;
    private ContactUtilities contactUtilities;
    private List<MyPojo> emailContacts, phoneContacts, nameContacts;
    private AtomicInteger contactQueryAtomicInt;

    //UI
    private TextView content_main_tv, activity_main_spinner_tv, activity_main_switch_tv;
    private SwitchCompat activity_main_switch;
    private Spinner activity_main_spinner;
    private RecyclerView content_main_recyclerview;
    private SearchView content_main_search_view;
    private RelativeLayout content_main_main_layout;
    private LinearLayout activity_main_spinner_layout, activity_main_switch_layout;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    //UI Var combos
    private CustomGridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private AdapterContacts adapter;
    private ArrayAdapter<String> spinnerAdapter;

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
        this.allPermsSet = this.useContactsWithPhotosOnly = false;
        this.emailContacts = phoneContacts = nameContacts = new ArrayList<>();
        this.gridLayoutManager = new CustomGridLayoutManager(this,
                GridLayoutManager.VERTICAL, false,
                TypedValue.COMPLEX_UNIT_PX, (MyApplication.getDMU().getPixelsWidth() / 2));
        this.linearLayoutManager = new LinearLayoutManager(this);
        this.adapter = new AdapterContacts(this, this, this);
        String[] strs = {"Pictures", "Emails", "Phone Numbers"};
        this.spinnerAdapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1, strs);
    }

    private void initUI(){
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        this.content_main_tv = (TextView) this.findViewById(R.id.content_main_tv);
        this.content_main_recyclerview = (RecyclerView) this.findViewById(R.id.content_main_recyclerview);
        this.content_main_search_view = (SearchView) this.findViewById(R.id.content_main_search_view);
        this.content_main_main_layout = (RelativeLayout) this.findViewById(R.id.content_main_main_layout);
        this.activity_main_spinner_layout = (LinearLayout) this.findViewById(R.id.activity_main_spinner_layout);
        this.activity_main_switch_layout = (LinearLayout) this.findViewById(R.id.activity_main_switch_layout);
        this.activity_main_spinner_tv = (TextView) this.findViewById(R.id.activity_main_spinner_tv);
		this.activity_main_spinner = (Spinner) this.findViewById(R.id.activity_main_spinner);
		this.activity_main_switch = (SwitchCompat) this.findViewById(R.id.activity_main_switch);
		
        setSupportActionBar(toolbar);
        this.content_main_search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
	        @Override
	        public boolean onQueryTextSubmit(String query) {
	        	if(query.length() > 1) {
			        getAllContacts(query);
		        }
		        return false;
	        }
	
	        @Override
	        public boolean onQueryTextChange(String newText) {
		        if(newText.length() > 1) {
			        getAllContacts(newText);
		        }
		        return false;
	        }
        });
	    this.activity_main_spinner.setAdapter(spinnerAdapter);
	    this.activity_main_spinner.setSelection(0);
	    this.activity_main_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			    MainActivity.this.loadContactData();
		    }
		    @Override
		    public void onNothingSelected(AdapterView<?> parent) {}
	    });
	    this.useContactsWithPhotosOnly = MyApplication.getSharedPrefsInstance().getBoolean(
			    Constants.ONLY_SHOW_CONTACTS_WITH_PICS, false);
	    this.activity_main_switch.setChecked(useContactsWithPhotosOnly);
	    this.activity_main_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    @Override
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				MyApplication.getSharedPrefsInstance().save(Constants.ONLY_SHOW_CONTACTS_WITH_PICS, isChecked);
			    useContactsWithPhotosOnly = isChecked;
			    getAllContacts();
		    }
	    });
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
	    getAllContacts("");
    }
	
	@SuppressLint("MissingPermission")
	private void getAllContacts(String query){
		if(!allPermsSet){
			requestPerms();
			return;
		}
		this.contactQueryAtomicInt = new AtomicInteger(4);
		showProgressBar(true);
		ContactUtilities.Builder builder = new ContactUtilities.Builder(this, this);
		builder.removeBlockListContacts();
		if(this.useContactsWithPhotosOnly){
			builder.onlyIncludeContactsWithPhotos();
		}
		this.contactUtilities = builder.build();
		this.contactUtilities.queryContacts(null, null, query);
	}
	
    private void triggerAtomicDecrement(){
        if(this.contactQueryAtomicInt.decrementAndGet() <= 0){
        	this.contactQueryAtomicInt = new AtomicInteger(0);
        	loadContactData();
        }
    }
    
    private void loadContactData(){
    	L.m("loadContactData");
	    int type = activity_main_spinner.getSelectedItemPosition();
	    if(type == 0){ //Pictures
	    	adapter.setData(nameContacts);
	    	if(!MiscUtilities.isListNullOrEmpty(this.nameContacts)){
			    this.content_main_main_layout.setVisibility(View.VISIBLE);
		    }
	    } else if (type == 1){ //Emails
		    adapter.setData(emailContacts);
		    if(!MiscUtilities.isListNullOrEmpty(this.emailContacts)){
			    this.content_main_main_layout.setVisibility(View.VISIBLE);
		    }
	    } else if (type == 2){ //Phone Numbers
		    adapter.setData(phoneContacts);
		    if(!MiscUtilities.isListNullOrEmpty(this.phoneContacts)){
			    this.content_main_main_layout.setVisibility(View.VISIBLE);
		    }
	    }
    }
    
    private void showProgressBar(boolean show){
        try {
            if (show) {
                ProgressBarUtilities.showSVGProgressDialog(this, -1);
            } else {
                ProgressBarUtilities.dismissProgressDialog();
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
        if(NumberUtilities.getInt(tag) > 0 && NumberUtilities.getInt(pos) >= 0){
	        int type = activity_main_spinner.getSelectedItemPosition();
	        MyPojo p = null;
	        if(type == 0){ //Pictures
		        p = nameContacts.get(pos);
		        if(p.isSelected()){
			        p.setSelected(false);
			        p.setType(AdapterContacts.AdapterContactsTypes.Picture);
		        } else {
			        p.setSelected(true);
			        p.setType(AdapterContacts.AdapterContactsTypes.Full);
		        }
		        nameContacts.set(pos, p);
		        adapter.updateOneObject(p, pos);
		        
	        } else if (type == 1){ //Emails
		        p = emailContacts.get(pos);
		        if(p.isSelected()){
			        p.setSelected(false);
			        p.setType(AdapterContacts.AdapterContactsTypes.Email);
		        } else {
			        p.setSelected(true);
			        p.setType(AdapterContacts.AdapterContactsTypes.Full);
		        }
		        emailContacts.set(pos, p);
		        adapter.updateOneObject(p, pos);
		        
	        } else if (type == 2){ //Phone Numbers
		        p = phoneContacts.get(pos);
		        if(p.isSelected()){
			        p.setSelected(false);
			        p.setType(AdapterContacts.AdapterContactsTypes.Phone);
		        } else {
			        p.setSelected(true);
			        p.setType(AdapterContacts.AdapterContactsTypes.Full);
		        }
		        phoneContacts.set(pos, p);
		        adapter.updateOneObject(p, pos);
		        
	        }
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
	            this.emailContacts = SimpleUtils.convertContactToPojoList(
	            		(List<ContactUtilities.Contact>) o,
			            AdapterContacts.AdapterContactsTypes.Email);
	            this.triggerAtomicDecrement();
	            break;

            case PGMacTipsConstants.TAG_CONTACT_QUERY_NAME:
	            this.nameContacts = SimpleUtils.convertContactToPojoList(
			            (List<ContactUtilities.Contact>) o,
			            AdapterContacts.AdapterContactsTypes.Picture);
	            this.triggerAtomicDecrement();
	            break;

            case PGMacTipsConstants.TAG_CONTACT_QUERY_PHONE:
	            this.phoneContacts = SimpleUtils.convertContactToPojoList(
			            (List<ContactUtilities.Contact>) o,
			            AdapterContacts.AdapterContactsTypes.Phone);
	            this.triggerAtomicDecrement();
	            break;


            case PGMacTipsConstants.TAG_CONTACT_QUERY_ADDRESS:
	            this.triggerAtomicDecrement();
                break;


        }
    }
}
