package in.scarecrow.oceanicsettings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // Database Version
    private static final int DATABASE_VERSION = 0;

    // Database Name
    private static final String DATABASE_NAME = "oceanic-settings";

    // Prefs
    private static final String sTABLE_PREFS = "table_prefs";
    private static final String sPREF_KEY = "pref_key";
    private static final String sPREF_VALUE = "pref_value";

    SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PREF_TABLE = "create table " + sTABLE_PREFS + "(" +
                sPREF_KEY + " text primary key, " + sPREF_VALUE + " text)";
        db.execSQL(CREATE_PREF_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //provide backward compatibility
        switch (oldVersion) {
            // for further versions
            case 0: Log.i(TAG, "Initial OceanicSettings version.");
        }
    }

    private long createPref(String prefKey, String prefValue) {
        ContentValues values = new ContentValues();
        values.put(sPREF_KEY, prefKey);
        values.put(sPREF_VALUE, prefValue);

        SQLiteDatabase db = this.getWritableDatabase();
        long insertId = db.insert(sTABLE_PREFS, null, values);
        close();
        Log.i(TAG, "ID : " + insertId + "[ Added pref with key : " + prefKey + ", value : " + prefValue + " ]");
        return insertId;
    }

    long setPref(String prefKey, String prefValue) {
        ContentValues values = new ContentValues();
        values.put(sPREF_VALUE, prefValue);

        SQLiteDatabase db = this.getWritableDatabase();
        long valuesChanged = db.update(sTABLE_PREFS, values, sPREF_KEY + " = ?", new String[]{String.valueOf(prefKey)});
        if (valuesChanged == 0) {
            createPref(prefKey, prefValue);
        }
        db.close();
        Log.i(TAG, "Values Updated : " + valuesChanged + "[ Updated pref with key : " + prefKey + ", value : " + prefValue + " ]");
        return valuesChanged;
    }

    String getPref(String prefKey) {
        String prefValue = null;
        String selectQuery = "SELECT  * FROM " + sTABLE_PREFS + " where " + sPREF_KEY + " = " + prefKey;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            prefValue = cursor.getString(1);
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Fetching pref : (" + prefKey + " , " + prefValue + ")");
        return prefValue;
    }

    HashMap<String, String> getAllSettings() {
        HashMap<String, String> hashMap = new HashMap<>();

        String selectQuery = "SELECT  * FROM " + sTABLE_PREFS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String key;
            String value;

            while (!cursor.isAfterLast()) {
                key = cursor.getString(0);
                value = cursor.getString(1);

                cursor.moveToNext();
                hashMap.put(key, value);
            }
        }
        cursor.close();
        db.close();

        return hashMap;
    }
}