package ir.trano.keeper.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.util.Map;
import java.util.Set;

import ir.trano.keeper.model.User;
import ir.trano.user.helper.Config;

public class Session {

    private SharedPreferences ExtrasPref;
    private Editor extraEditor;

    private static final String PREF_EXTRAS = "Trano";

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

    public void setRegister(boolean register) {
        extraEditor.putBoolean(Config.REGISTER, register);
        extraEditor.commit();
    }
    public Boolean getRegister() {
        return ExtrasPref.getBoolean(Config.REGISTER, true);
    }


    public void setUser(User user) {
        extraEditor.putInt(Config.ID, user.getId());
        extraEditor.putString(Config.NAME, user.getName());
        extraEditor.putString(Config.FAMILY, user.getFamily());
        extraEditor.putBoolean(Config.HAS_LOGIN, user.isActive());
        extraEditor.commit();
    }

    public void setUserNameAndFamily(String name, String family) {
        extraEditor.putString(Config.NAME, name);
        extraEditor.putString(Config.FAMILY, family);
        extraEditor.commit();
    }

    public void setToken(String token) {
        extraEditor.putString(Config.TOKEN, token);
        extraEditor.commit();
    }
    public String getToken() {
        return ExtrasPref.getString(Config.TOKEN, "");
    }

    public String getMobile() {
        return ExtrasPref.getString(Config.MOBILE, "");
    }

    public int getUserId() {
        return ExtrasPref.getInt(Config.ID, 0);
    }

    public boolean getHasLogin() {
        return !TextUtils.isEmpty(getToken());
    }


    public void setRegToken(String regToken) {
        extraEditor.putString(Config.REG_TOKEN, regToken);
        extraEditor.commit();
    }

    public String getRegToken() {
        return ExtrasPref.getString(Config.REG_TOKEN, "");
    }

    public boolean hasNewRegToken() {
        return ExtrasPref.getBoolean(Config.HAS_NEW_REG_TOKEN, false);
    }

    public void hasNewRegToken(boolean status) {
        extraEditor.putBoolean(Config.HAS_NEW_REG_TOKEN, true);
        extraEditor.commit();
    }

    /*JSON*/
    public void setJson(String json) {
        extraEditor.putString(Config.JSON_PACKINGS, json);
        extraEditor.commit();
    }
    public String getJson() {
        return ExtrasPref.getString(Config.JSON_PACKINGS, "");
    }

}