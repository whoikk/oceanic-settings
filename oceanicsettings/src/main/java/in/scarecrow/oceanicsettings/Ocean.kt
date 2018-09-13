package `in`.scarecrow.oceanicsettings

import android.content.Context
import android.util.Log
import java.util.*

/**
 * Ocean Created by Ashish on 12/09/2018.
 */

@Suppress("unused")

object Ocean {
    private var settings: HashMap<String, String> = HashMap()

    private var isInit = false
    private var isDebuggable = false

    private const val TAG = "Ocean.kt"

    @JvmStatic
    private fun init(context: Context, debug: Boolean = false): Boolean {
        isDebuggable = debug

        if (isInit) {
            val db = SQLiteHandler(context)

            try {
                settings = db.allSettings
            } catch (exp: Exception) {
                if (isDebuggable) Log.e(TAG, exp.printStackTrace().toString())
                return false
            }

            db.close()
            return true
        } else {
            return true
        }
    }

    @JvmStatic
    private fun checkInit() {
        if (!isInit) {
            throw RuntimeException("Ocean accessed before initialization.")
        }
    }

    @JvmStatic
    fun put(context: Context, key: String, value: String) {
        val db = SQLiteHandler(context)
        db.setPref(key, value, isDebuggable)
        db.close()
        if (init(context)) {
            settings[key] = value
        }
    }

    @JvmStatic
    fun fish(key: String, defValue: String): String {
        checkInit()
        return if (settings.containsKey(key))
            settings[key].toString()
        else
            defValue
    }

    @JvmStatic
    fun putBoolean(context: Context, key: String, value: Boolean) {
        put(context, key, value.toString())
    }

    @JvmStatic
    fun fishBoolean(key: String, defValue: Boolean): Boolean {
        checkInit()
        if (settings.containsKey(key))
            return try {
                settings[key]!!.toBoolean()
            } catch (exp: Exception) {
                if (isDebuggable) Log.e(TAG, exp.printStackTrace().toString())
                defValue
            }

        return defValue
    }

    @JvmStatic
    fun putFloat(context: Context, key: String, value: Float) {
        put(context, key, value.toString())
    }

    @JvmStatic
    fun fishFloat(key: String, defValue: Float): Float {
        checkInit()
        if (settings.containsKey(key))
            return try {
                settings[key]!!.toFloat()
            } catch (exp: Exception) {
                if (isDebuggable) Log.e(TAG, exp.printStackTrace().toString())
                defValue
            }

        return defValue
    }

    @JvmStatic
    fun putDouble(context: Context, key: String, value: Double) {
        put(context, key, value.toString())
    }

    @JvmStatic
    fun fishDouble(key: String, defValue: Double): Double {
        checkInit()
        if (settings.containsKey(key))
            return try {
                settings[key]!!.toDouble()
            } catch (exp: Exception) {
                if (isDebuggable) Log.e(TAG, exp.printStackTrace().toString())
                defValue
            }

        return defValue
    }

    @JvmStatic
    fun putInt(context: Context, key: String, value: Int) {
        put(context, key, value.toString())
    }

    @JvmStatic
    fun fishInt(key: String, defValue: Int): Int {
        checkInit()
        return if (settings.containsKey(key))
            try {
                settings[key]!!.toInt()
            } catch (exp: Exception) {
                if (isDebuggable) Log.e(TAG, exp.printStackTrace().toString())
                defValue
            }
        else
            defValue
    }

    @JvmStatic
    fun putLong(context: Context, key: String, value: Long) {
        put(context, key, value.toString())
    }

    @JvmStatic
    fun fishLong(key: String, defValue: Long): Long {
        checkInit()
        if (settings.containsKey(key))
            return try {
                settings[key]!!.toLong()
            } catch (exp: Exception) {
                if (isDebuggable) Log.e(TAG, exp.printStackTrace().toString())
                defValue
            }

        return defValue
    }
}