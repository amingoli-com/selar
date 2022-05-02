package amingoli.com.selar.helper;

import static amingoli.com.selar.helper.Config.MONEY_TYPE_DEFAULT;

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
        extraEditor.putInt(Config.BUSINESS_ID, branch);
        extraEditor.commit();
    }
    public int getBranch() {
        return ExtrasPref.getInt(Config.BUSINESS_ID, -1);
    }

    public void setMoneyType(String moneyType) {
        extraEditor.putString(Config.MONEY_TYPE, moneyType);
        extraEditor.commit();
    }
    public String getMoneyType() {
        return ExtrasPref.getString(Config.MONEY_TYPE, MONEY_TYPE_DEFAULT);
    }

    public void setShippingPrice(Double shippingPrice) {
        extraEditor.putString(Config.SHIPPING_PRICE, String.valueOf(shippingPrice));
        extraEditor.commit();
    }
    public Double getShippingPrice() {
        return Double.parseDouble(ExtrasPref.getString(Config.SHIPPING_PRICE, "0.0"));
    }

    public void setFreeShippingPrice(Double price) {
        extraEditor.putString(Config.SHIPPING_FREE_PRICE, String.valueOf(price));
        extraEditor.commit();
    }
    public Double getFreeShippingPrice() {
        return Double.parseDouble(ExtrasPref.getString(Config.SHIPPING_FREE_PRICE, "0.0"));
    }

    public void setMinOrder(Double price) {
        extraEditor.putString(Config.MIN_ORDER, String.valueOf(price));
        extraEditor.commit();
    }
    public Double getMinOrder() {
        return Double.parseDouble(ExtrasPref.getString(Config.MIN_ORDER, "0.0"));
    }

    public void setTaxPercent(int taxPercent) {
        extraEditor.putInt(Config.TAX_PERCENT, taxPercent);
        extraEditor.commit();
    }
    public int getTaxPercent() {
        return ExtrasPref.getInt(Config.TAX_PERCENT, 0);
    }

    public void setCheckBox(boolean money, boolean card, boolean debit, boolean discount){
        extraEditor.putBoolean(Config.CHECK_BOX_MONEY,money);
        extraEditor.putBoolean(Config.CHECK_BOX_CARD,card);
        extraEditor.putBoolean(Config.CHECK_BOX_DEBIT,debit);
        extraEditor.putBoolean(Config.CHECK_BOX_DISCOUNT,discount);
        extraEditor.commit();
    }

    public void setCheckBoxMoney(boolean checkBox) {
        extraEditor.putBoolean(Config.CHECK_BOX_MONEY,checkBox);
        extraEditor.commit();
    }
    public boolean getCheckBoxMoney() {
        return ExtrasPref.getBoolean(Config.CHECK_BOX_MONEY, true);
    }

    public void setCheckBoxCard(boolean checkBox) {
        extraEditor.putBoolean(Config.CHECK_BOX_CARD,checkBox);
        extraEditor.commit();
    }
    public boolean getCheckBoxCard() {
        return ExtrasPref.getBoolean(Config.CHECK_BOX_CARD, true);
    }

    public void setCheckBoxDebit(boolean checkBox) {
        extraEditor.putBoolean(Config.CHECK_BOX_DEBIT,checkBox);
        extraEditor.commit();
    }
    public boolean getCheckBoxDebit() {
        return ExtrasPref.getBoolean(Config.CHECK_BOX_DEBIT, true);
    }

    public void setCheckBoxDiscount(boolean checkBox) {
        extraEditor.putBoolean(Config.CHECK_BOX_DISCOUNT,checkBox);
        extraEditor.commit();
    }
    public boolean getCheckBoxDiscount() {
        return ExtrasPref.getBoolean(Config.CHECK_BOX_DISCOUNT, true);
    }
}