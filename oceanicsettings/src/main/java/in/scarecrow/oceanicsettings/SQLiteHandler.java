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
    private static final int DATABASE_VERSION = 0;
    private static final String DATABASE_NAME = "oceanic-settings";

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
        switch (oldVersion) {
            // for further versions
            case 0:
                break;
        }
    }

    private void createPref(String prefKey, String prefValue, boolean isLogging) {
        ContentValues values = new ContentValues();
        values.put(sPREF_KEY, prefKey);
        values.put(sPREF_VALUE, prefValue);

        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(sTABLE_PREFS, null, values);
        this.close();
        if (isLogging)
            Log.i(TAG, "new pref created: {" + id + "} (" + prefKey + ", " + prefValue + ")");
    }

    void setPref(String prefKey, String prefValue, boolean isLogging) {
        ContentValues values = new ContentValues();
        values.put(sPREF_VALUE, prefValue);

        SQLiteDatabase db = this.getWritableDatabase();
        long changes = db.update(sTABLE_PREFS, values, sPREF_KEY + " = ?", new String[]{String.valueOf(prefKey)});
        if (changes == 0) {
            createPref(prefKey, prefValue, isLogging);
        } else {
            Log.i(TAG, "pref set: {changes: " + changes + "(" + prefKey + ", " + prefValue + ")");
        }
        db.close();
    }

    String getPref(String prefKey, boolean isLogging) {
        String prefValue = null;
        String selectQuery = "SELECT  * FROM " + sTABLE_PREFS + " where " + sPREF_KEY + " = " + prefKey;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            prefValue = cursor.getString(cursor.getColumnIndex(sPREF_VALUE));
        }
        cursor.close();
        db.close();

        Log.i(TAG, "getting pref: (" + prefKey + ", " + prefValue + ")");
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
                key = cursor.getString(cursor.getColumnIndex(sPREF_KEY));
                value = cursor.getString(cursor.getColumnIndex(sPREF_VALUE));

                cursor.moveToNext();
                hashMap.put(key, value);
            }
        }
        cursor.close();
        db.close();

        return hashMap;
    }
}