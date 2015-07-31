package com.placelook;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings;

import com.placelook.utils.Security;
import com.placelook.Language;
import java.util.Date;

/**
 * Created by victor on 16.06.15.
 */
public class Account {
    private SharedPreferences sh;
    private Context context;
    private String login;
    private String password;
    private boolean isLogined;
    private boolean isOuted;
    private Language language;
    private int userID;
    private int accountID;
    private int idStreamingFormat;
    private boolean type;
    private int balance;
    private String email;

    public Account(Context context){
        this.context = context;
        isLogined = false;
        userID = -1;
        language = new Language("en","English");
        load();
    }
    public void login(boolean b){
        isLogined = b;
    }
    public boolean isLogin(){
        return isLogined;
    }
    public void setUserID(int userID){
        this.userID = userID;
    }
    public void setFirst(boolean b){
        Editor ed = sh.edit();
        ed.putBoolean("first", b);
        ed.commit();
    }
    public boolean isFirst(){
        sh = context.getSharedPreferences(Constants.appName, context.MODE_PRIVATE);
        return sh.getBoolean("first", true);
    }
    public void save(){
        sh = context.getSharedPreferences(Constants.appName, context.MODE_PRIVATE);
        Editor ed = sh.edit();
        ed.putString("login", login);
        ed.putString("password", password);
        ed.putString("shortLanguage", language.getShortLanguage());
        ed.putString("longLanguage", language.getLongLanguage());
        ed.commit();
    }
    public void saveAsTemp(){
        sh = context.getSharedPreferences(Constants.appName, context.MODE_PRIVATE);
        Editor ed = sh.edit();
        ed.putString("tlogin", login);
        ed.putString("tpassword", password);
        ed.putString("tshortLanguage", language.getShortLanguage());
        ed.putString("tlongLanguage", language.getLongLanguage());
        ed.commit();
    }
    public void load(){
        sh = context.getSharedPreferences(Constants.appName, context.MODE_PRIVATE);
        login = sh.getString("login", generateLogin());
        password = sh.getString("password", generatePassword());
        isLogined = sh.getBoolean("isLogin", false);
        isOuted = sh.getBoolean("outed", true);
        language.setLanguage(new Language(sh.getString("shortLanguage", "en"), sh.getString("longLanguage", "English")));
    }
    public void loadTemp(){
        sh = context.getSharedPreferences(Constants.appName, context.MODE_PRIVATE);
        login = sh.getString("tlogin", generateLogin());
        password = sh.getString("tpassword", generatePassword());
        //isLogined = sh.getBoolean("isLogin", false);
        //isOuted = sh.getBoolean("outed", true);
        language.setLanguage(new Language(sh.getString("tshortLanguage", "en"), sh.getString("tlongLanguage", "English")));
    }
    public void setLogin(String sLogin){
        this.login = sLogin;
    }
    public String getLogin(){
        return login;
    }
    public void setPassword(String sPassowrd){
        this.password = sPassowrd;
    }
    public String getPassword(){
        return password;
    }
    public void setTypeAccount(boolean b){
        this.type = b;
    }
    public boolean isTemporary(){
        return !type;
    }
    private String generateLogin(){
        Date d = new Date();
        String IMEI = Security.md5(Settings.Secure.ANDROID_ID);
        long  unixtime = d.getTime() / 1000L;
        StringBuilder builder = new StringBuilder();
        builder.append(IMEI);
        builder.append("-");
        builder.append(String.valueOf(unixtime));
        builder.append("@anonymous");
        return builder.toString();
    }
    private String generatePassword(){
        String IMEI = Security.md5(Settings.Secure.ANDROID_ID);
        return Security.md5(Constants.Platforms + IMEI);
    }
    public void setAccountID(int id){
        this.accountID = id;
    }
    public int getAccountID(){
        return accountID;
    }
    public void setStreamingFormat(int id){
        idStreamingFormat = id;
    }
    public int getStreamingFormat(){
        return idStreamingFormat;
    }
    public void setBalance(int balance){
        this.balance = balance;
    }
    public int getBalance(){
        return balance;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
}
