package in.scarecrow.oceanicsettings;

import android.content.Context;

import java.util.HashMap;

/**
 * SettingsVariables Created by Ashish on 12/09/2018.
 */

public class Sv {
    private static HashMap<String, String> settings = null;

    public static String getSetting(String key, String defaultValue) {
        checkInit();
        if (settings.containsKey(key))
            try {
                return String.valueOf(settings.get(key));
            } catch (Exception e) {
                return defaultValue;
            }
        return defaultValue;
    }

    public static boolean getBooleanSetting(String key, boolean defaultValue) {
        checkInit();
        if (settings.containsKey(key))
            try {
                return Boolean.parseBoolean(String.valueOf(settings.get(key)));
            } catch (Exception e) {
                return defaultValue;
            }
        return defaultValue;
    }

    public static void setIntSetting(Context context, String key, int value) {
        setSetting(context, key, String.valueOf(value));
    }

    public static int getIntSetting(String key, int defaultValue) {
        checkInit();
        if (settings.containsKey(key))
            try {
                return Integer.parseInt(String.valueOf(settings.get(key)));
            } catch (Exception e) {
                return defaultValue;
            }
        return defaultValue;
    }

    public static void setBooleanSetting(Context context, String key, boolean value) {
        setSetting(context, key, String.valueOf(value));
    }

    public static void setSetting(Context context, String key, String value) {
        SQLiteHandler db = new SQLiteHandler(context);
        db.setPref(key, value);
        db.close();
        if (init(context)) {
            settings.put(key, value);
        }
    }

    private static void checkInit() {
        if (settings == null) {
            throw (new RuntimeException("Oceanic settings are not initialized."));
        }
    }

    private static boolean init(Context context) {
        if (settings == null) {
            SQLiteHandler db = new SQLiteHandler(context);
            try {
                settings = db.getAllSettings();
            } catch (Exception ignored) {
                return false;
            }

            db.close();
            return true;
        } else {
            return true;
        }
    }
}