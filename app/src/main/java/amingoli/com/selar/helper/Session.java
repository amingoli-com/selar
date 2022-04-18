package amingoli.com.selar.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.util.Map;
import java.util.Set;

import amingoli.com.selar.model.Business;

public class Session {

    private SharedPreferences ExtrasPref;
    private Editor extraEditor;

    private static final String PREF_EXTRAS = "SSP";

    Session() {
        ExtrasPref = App.context.getSharedPreferences(PREF_EXTRAS, Context.MODE_PRIVATE);
        extraEditor = ExtrasPref.edit();
    }

    public static Session getInstance() {
        return new Session();
    }

    public void putExtra(String key, String value) {
        extraEditor.putString(key, value);
        extraEditor.commit();
    }

    public void putExtra(String key, Boolean value) {
        extraEditor.putBoolean(key, value);
        extraEditor.commit();
    }

    public void putExtra(String key, Integer value) {
        extraEditor.putInt(key, value);
        extraEditor.commit();
    }

    public void putExtra(String key, Long value) {
        extraEditor.putLong(key, value);
        extraEditor.commit();
    }

    public void putExtra(String key, Set<String> list) {
        extraEditor.putStringSet(key, list);
        extraEditor.commit();
    }

    public String getString(String key) {
        return ExtrasPref.getString(key, "");
    }

    public Set<String> getSet(String key) {
        return ExtrasPref.getStringSet(key, null);
    }

    public int getInt(String key) {
        return ExtrasPref.getInt(key, 0);
    }

    public boolean getBoolean(String key) {
        return ExtrasPref.getBoolean(key, false);
    }

    public Long getLong(String key) {
        return ExtrasPref.getLong(key, 0);
    }

    public Map<String, ?> getExtras() {
        return ExtrasPref.getAll();
    }

    public boolean clearExtras() {
        extraEditor.clear();
        return extraEditor.commit();
    }

    public void remove(String key) {
        extraEditor.remove(key);
        extraEditor.apply();
    }

    public void setBusiness(Business business) {
        extraEditor.putString(Config.BUSINESS_OWNER_NAME, business.getOwner_name());
        extraEditor.putString(Config.BUSINESS_NAME, business.getBusiness_name());
        extraEditor.putInt(Config.BUSINESS_ID, business.getId());
        extraEditor.commit();
    }

    public void setBusiness(String name, String businessName, int businessID) {
        extraEditor.putString(Config.BUSINESS_OWNER_NAME, name);
        extraEditor.putString(Config.BUSINESS_NAME, businessName);
        extraEditor.putInt(Config.BUSINESS_ID, businessID);
        extraEditor.commit();
    }

    public String getBusinessOwnerName() {
        return ExtrasPref.getString(Config.BUSINESS_OWNER_NAME, null);
    }

    public String getBusinessName() {
        return ExtrasPref.getString(Config.BUSINESS_NAME, null);
    }

    public int getBusinessID() {
        return ExtrasPref.getInt(Config.BUSINESS_ID, -1);
    }

    public void setSessionKey(String key) {
        extraEditor.putString(Config.SESSION_KEY, key);
        extraEditor.commit();
    }

    public String getSessionKey() {
        return ExtrasPref.getString(Config.SESSION_KEY, null);
    }

    public int getUser() {
        return ExtrasPref.getInt(Config.USER, -1);
    }

    public void setBranch(int branch) {
        extraEditor.putInt(Config.BRANCH, branch);
        extraEditor.commit();
    }
    public int getBranch() {
        return ExtrasPref.getInt(Config.BRANCH, -1);
    }

}