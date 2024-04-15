package com.about.kby;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.about.kby.Activity.HomeActivity;


public class Asession {
    public Asession() {

    }

    public static Asession bSession;

    public static Asession getInstance() {

        if (bSession == null) {
            synchronized (Asession.class) {
                bSession = new Asession();
            }
        }
        return bSession;
    }

    private static String TAG = Asession.class.getSimpleName();
    SharedPreferences.Editor editor;
    Context _context;

    SharedPreferences prefs;

    private String user_id;
    private String user_name;
    private String user_mobile;
    private String user_email;
    private String member_id;
    private String group_id;


    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public void initialize(Context context,
                           String user_id,
                           String user_name,
                           String user_mobile,
                           String user_email,
                           String member_id,
                           String group_id) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("user_id", user_id);
        editor.putString("user_name", user_name);
        editor.putString("user_mobile", user_mobile);
        editor.putString("user_email", user_email);
        editor.putString("member_id", member_id);
        editor.putString("group_id", group_id);

        editor.apply();

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_mobile = user_mobile;
        this.user_email=user_email;
        this.member_id=member_id;
        this.group_id = group_id;

    }

    public void destroy(Context context) {

        SharedPreferences sharedpreferences = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit().clear();
        editor.apply();
        this.user_id = null;
        this.user_name = null;
        this.user_mobile = null;
        this.user_email = null;
        this.member_id = null;
        this.group_id = null;
    }

    public String[] getSession(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        String[] sharedValues = new String[7];

        sharedValues[0] = this.user_id = prefs.getString("user_id", "");
        sharedValues[1] = this.user_name = prefs.getString("user_name", "");
        sharedValues[2] = this.user_mobile = prefs.getString("user_mobile", "");
        sharedValues[4] = this.user_email = prefs.getString("user_email", "");
        sharedValues[5] = this.member_id = prefs.getString("member_id", "");
        sharedValues[6] = this.group_id = prefs.getString("group_id", "");

        return sharedValues;
    }

    public String getKey(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        String key = sharedPreferences.getString("key", "noKey");
        return key;

    }

    public Boolean isAuthenticated(Context context) {

        if (this.group_id == null || this.user_name == null || this.group_id.isEmpty() || this.user_name.isEmpty() || this.group_id.equals("NosessionId") || this.user_name.equals("Nouser_name")) {
            Intent intent = null;
            intent = new Intent(context.getApplicationContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return false;
        }
        return true;
    }

    public Boolean isApplicationExit(Context context) {

        getSession(context);

        System.out.println("====" + this.group_id + "UN" + this.user_name);
        if (this.user_name == null || this.user_name.isEmpty() || this.user_name.equals("Nouser_name")) {
            return false;

        }
        System.out.println("====" + this.group_id + "UN--true" + this.user_name);
        return true;
    }


    public String getGroup_id(Context context) {
        getSession(context);
        return group_id;
    }

    public void setGroup_id(String sessionId) {
        this.group_id = group_id;
    }

    public String getUser_id(Context context) {
        getSession(context);
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name(Context context) {
        getSession(context);
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_mobile(Context context) {
        getSession(context);
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getMember_id(Context context) {
        getSession(context);
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.user_email = member_id;
    }

    public String getUser_email(Context context) {
        getSession(context);
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }


}



