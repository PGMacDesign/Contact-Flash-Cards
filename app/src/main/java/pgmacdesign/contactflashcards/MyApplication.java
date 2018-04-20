package pgmacdesign.contactflashcards;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.pgmacdesign.pgmactips.utilities.DatabaseUtilities;
import com.pgmacdesign.pgmactips.utilities.DisplayManagerUtilities;
import com.pgmacdesign.pgmactips.utilities.SharedPrefs;

import io.realm.RealmConfiguration;

/**
 * Created by pmacdowell on 2018-04-20.
 */

public class MyApplication extends MultiDexApplication {
    private static MyApplication sInstance;
    private static Context context;
    private static SharedPrefs sp;
    private static DatabaseUtilities dbUtilities;
    private static DisplayManagerUtilities dmu;

    public MyApplication() {
    }

    public void onCreate() {
        super.onCreate();
        sInstance = this;
        context = getContext();
        dbUtilities = getDatabaseInstance();
        sp = getSharedPrefsInstance();
    }

    public static synchronized Context getContext() {
        if(context == null) {
            context = getInstance().getApplicationContext();
        }

        return context;
    }

    public static synchronized DatabaseUtilities getDatabaseInstance() {
        if(dbUtilities == null) {
            RealmConfiguration config = DatabaseUtilities.buildRealmConfig(getContext(), "ContactFlashCards.DB", Integer.valueOf(1), Boolean.valueOf(true));
            dbUtilities = new DatabaseUtilities(getContext(), config);
        }

        return dbUtilities;
    }

    public static synchronized SharedPrefs getSharedPrefsInstance() {
        if(sp == null) {
            sp = SharedPrefs.getSharedPrefsInstance(getContext(), "PGMacUtilities_SharedPrefs");
        }

        return sp;
    }

    public static synchronized DisplayManagerUtilities getDMU() {
        if(dmu == null) {
            dmu = new DisplayManagerUtilities(getContext());
        }

        return dmu;
    }

    public static synchronized MyApplication getInstance() {
        if(sInstance == null) {
            sInstance = new MyApplication();
        }

        return sInstance;
    }

    public static boolean haveLoadedContacts (){
        return getSharedPrefsInstance().getBoolean(Constants.HAVE_LOADED_CONTACTS, false);
    }

    public static void setHaveLoadedContacts(boolean bool){
        getSharedPrefsInstance().save(Constants.HAVE_LOADED_CONTACTS, bool);
    }
    
}
