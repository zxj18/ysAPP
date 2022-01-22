package com.vodbyte.movie.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;

import java.util.LinkedHashSet;
import java.util.Set;

public class PrefsUtils {

    private static final String LENGTH = "_length";
    private static final String DEFAULT_STRING_VALUE = "";
    private static final int DEFAULT_INT_VALUE = -1;
    private static final double DEFAULT_DOUBLE_VALUE = -1d;
    private static final float DEFAULT_FLOAT_VALUE = -1f;
    private static final long DEFAULT_LONG_VALUE = -1L;
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;

    private static SharedPreferences sharedPreferences;
    private static PrefsUtils prefsUtilsInstance;

    private PrefsUtils(@NonNull Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(
                context.getPackageName() + "_preferences",
                Context.MODE_PRIVATE
        );
    }

    private PrefsUtils(@NonNull Context context, @NonNull String preferencesName) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(
                preferencesName,
                Context.MODE_PRIVATE
        );
    }

    /**
     * @param context
     * @return Returns a 'Prefs' instance
     */
    public static PrefsUtils with(@NonNull Context context) {
        if (prefsUtilsInstance == null) {
            prefsUtilsInstance = new PrefsUtils(context);
        }
        return prefsUtilsInstance;
    }

    /**
     * @param context
     * @param forceInstantiation
     * @return Returns a 'Prefs' instance
     */
    public static PrefsUtils with(@NonNull Context context, boolean forceInstantiation) {
        if (forceInstantiation) {
            prefsUtilsInstance = new PrefsUtils(context);
        }
        return prefsUtilsInstance;
    }

    /**
     * @param context
     * @param preferencesName
     * @return Returns a 'Prefs' instance
     */
    public static PrefsUtils with(@NonNull Context context, @NonNull String preferencesName) {
        if (prefsUtilsInstance == null) {
            prefsUtilsInstance = new PrefsUtils(context, preferencesName);
        }
        return prefsUtilsInstance;
    }

    /**
     * @param context
     * @param preferencesName
     * @param forceInstantiation
     * @return Returns a 'Prefs' instance
     */
    public static PrefsUtils with(@NonNull Context context, @NonNull String preferencesName,
                                  boolean forceInstantiation) {
        if (forceInstantiation) {
            prefsUtilsInstance = new PrefsUtils(context, preferencesName);
        }
        return prefsUtilsInstance;
    }

    // String related methods

    /**
     * @param where
     * @return Returns the stored value of 'where'
     */
    public String read(String where) {
        return sharedPreferences.getString(where, DEFAULT_STRING_VALUE);
    }

    /**
     * @param where
     * @param defaultString
     * @return Returns the stored value of 'value'
     */
    public String read(String where, String defaultString) {
        return sharedPreferences.getString(where, defaultString);
    }

    /**
     * @param where
     * @param value
     */
    public void write(String where, String value) {
        sharedPreferences.edit().putString(where, value).apply();
    }

    // int related methods

    /**
     * @param where
     * @return Returns the stored value of 'value'
     */
    public int readInt(String where) {
        return sharedPreferences.getInt(where, DEFAULT_INT_VALUE);
    }

    /**
     * @param where
     * @param defaultInt
     * @return Returns the stored value of 'value'
     */
    public int readInt(String where, int defaultInt) {
        return sharedPreferences.getInt(where, defaultInt);
    }

    /**
     * @param where
     * @param value
     */
    public void writeInt(String where, int value) {
        sharedPreferences.edit().putInt(where, value).apply();
    }

    // double related methods

    /**
     * @param where
     * @return Returns the stored value of 'value'
     */
    public double readDouble(String where) {
        if (!contains(where))
            return DEFAULT_DOUBLE_VALUE;
        return Double.longBitsToDouble(readLong(where));
    }

    /**
     * @param where
     * @param defaultDouble
     * @return Returns the stored value of 'value'
     */
    public double readDouble(String where, double defaultDouble) {
        if (!contains(where))
            return defaultDouble;
        return Double.longBitsToDouble(readLong(where));
    }

    /**
     * @param where
     * @param value
     */
    public void writeDouble(String where, double value) {
        writeLong(where, Double.doubleToRawLongBits(value));
    }

    // float related methods

    /**
     * @param where
     * @return Returns the stored value of 'value'
     */
    public float readFloat(String where) {
        return sharedPreferences.getFloat(where, DEFAULT_FLOAT_VALUE);
    }

    /**
     * @param where
     * @param defaultFloat
     * @return Returns the stored value of 'value'
     */
    public float readFloat(String where, float defaultFloat) {
        return sharedPreferences.getFloat(where, defaultFloat);
    }

    /**
     * @param where
     * @param value
     */
    public void writeFloat(String where, float value) {
        sharedPreferences.edit().putFloat(where, value).apply();
    }

    // long related methods

    /**
     * @param where
     * @return Returns the stored value of 'value'
     */
    public long readLong(String where) {
        return sharedPreferences.getLong(where, DEFAULT_LONG_VALUE);
    }

    /**
     * @param where
     * @param defaultLong
     * @return Returns the stored value of 'value'
     */
    public long readLong(String where, long defaultLong) {
        return sharedPreferences.getLong(where, defaultLong);
    }

    /**
     * @param where
     * @param value
     */
    public void writeLong(String where, long value) {
        sharedPreferences.edit().putLong(where, value).apply();
    }

    // boolean related methods

    /**
     * @param where
     * @return Returns the stored value of 'value'
     */
    public boolean readBoolean(String where) {
        return sharedPreferences.getBoolean(where, DEFAULT_BOOLEAN_VALUE);
    }

    /**
     * @param where
     * @param defaultBoolean
     * @return Returns the stored value of 'value'
     */
    public boolean readBoolean(String where, boolean defaultBoolean) {
        return sharedPreferences.getBoolean(where, defaultBoolean);
    }

    /**
     * @param where
     * @param value
     */
    public void writeBoolean(String where, boolean value) {
        sharedPreferences.edit().putBoolean(where, value).apply();
    }

    // String set methods

    /**
     * @param key
     * @param value
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void putStringSet(final String key, final Set<String> value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            sharedPreferences.edit().putStringSet(key, value).apply();
        } else {
            // Workaround for pre-HC's lack of StringSets
            putOrderedStringSet(key, value);
        }
    }

    /**
     * @param key
     * @param value
     */
    public void putOrderedStringSet(String key, Set<String> value) {
        int stringSetLength = 0;
        if (sharedPreferences.contains(key + LENGTH)) {
            // First read value the value was
            stringSetLength = readInt(key + LENGTH);
        }
        writeInt(key + LENGTH, value.size());
        int i = 0;
        for (String aValue : value) {
            write(key + "[" + i + "]", aValue);
            i++;
        }
        for (; i < stringSetLength; i++) {
            // Remove any remaining values
            remove(key + "[" + i + "]");
        }
    }

    /**
     * @param key
     * @param defValue
     * @return Returns the String Set with HoneyComb compatibility
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(final String key, final Set<String> defValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return sharedPreferences.getStringSet(key, defValue);
        } else {
            // Workaround for pre-HC's missing getStringSet
            return getOrderedStringSet(key, defValue);
        }
    }

    /**
     * @param key
     * @param defValue
     * @return Returns the ordered String Set
     */
    public Set<String> getOrderedStringSet(String key, final Set<String> defValue) {
        if (contains(key + LENGTH)) {
            LinkedHashSet<String> set = new LinkedHashSet<>();
            int stringSetLength = readInt(key + LENGTH);
            if (stringSetLength >= 0) {
                for (int i = 0; i < stringSetLength; i++) {
                    set.add(read(key + "[" + i + "]"));
                }
            }
            return set;
        }
        return defValue;
    }

    // end related methods

    /**
     * @param key
     */
    public void remove(final String key) {
        if (contains(key + LENGTH)) {
            // Workaround for pre-HC's lack of StringSets
            int stringSetLength = readInt(key + LENGTH);
            if (stringSetLength >= 0) {
                sharedPreferences.edit().remove(key + LENGTH).apply();
                for (int i = 0; i < stringSetLength; i++) {
                    sharedPreferences.edit().remove(key + "[" + i + "]").apply();
                }
            }
        }
        sharedPreferences.edit().remove(key).apply();
    }

    /**
     * @param key
     * @return Returns if that key exists
     */
    public boolean contains(final String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * Clear all the preferences
     */
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

}
