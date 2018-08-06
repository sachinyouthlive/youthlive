package com.yl.youthlive.DBHandler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


import com.yl.youthlive.HomeActivity;
import com.yl.youthlive.Login;
import com.yl.youthlive.Signin;

import java.util.HashMap;

/**
 * Created by admin on 4/12/2017.
 */
public class SessionManager {

    public static final String FOLLOWER = "FOLLOWER";
    public static final String YOUTHLIVEID = "YOUTHID";
    public static final String FOLLOWING = "FOLLOWING";
    public static final String USER_ID = "USER_ID";
    public static final String COVER_PROFILE = "COVER_PROFILE";
    public static final String USER_PROFILE = "USER_PROFILE";
    public static final String USER_NAME = "USER_NAME";
    public static final String EDUCATION = "EDUCATION";
    public static final String GENDER = "GENDER";
    public static final String DOB = "DOB";
    public static final String LOCATION = "LOCATION";
    public static final String ABOUT = "ABOUT";
    public static final String FOLLOW_STATUS = "FOLLOW_STATUS";
    public static final String PRO_STATUS = "PRO_STATUS";
    public static final String FOLLOW_UNFOLLOW = "FOLLOW_UNFOLLOW";
    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context context;
    // Shared pref mode
    int PRIVATE_MODE = 0;


//    public static final String KEY_CONTACT_NO = "contact_no";
//    public static final String KEY_USER_EMAIL = "email";
//    public static final String KEY_PROFILE_PIC="profile_pic";
    // User name (make variable public to access from outside)
//    public static final String KEY_NAME = "name";
//
//    // Email address (make variable public to access from outside)
//    public static final String KEY_EMAIL = "email";

    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(Signin signin, String id) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USER_ID, id);
        editor.commit();
    }

    public void createCoverImage(String coverProfile) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(COVER_PROFILE, coverProfile);
        editor.commit();
    }

    public void createProfile(String follower, String following, String Youthidd) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(FOLLOWER, follower);
        editor.putString(FOLLOWING, following);
        editor.putString(YOUTHLIVEID, Youthidd);
        editor.commit();
    }

    public void followUnfollow(String yes) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(FOLLOW_UNFOLLOW, yes);
        editor.commit();
    }

    public void updateSession(String profile, String name, String highEdu, String gender, String brithDay, String loc, String about) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USER_PROFILE, profile);
        editor.putString(USER_NAME, name);
        editor.putString(EDUCATION, highEdu);
        editor.putString(GENDER, gender);
        editor.putString(DOB, brithDay);
        editor.putString(LOCATION, loc);
        editor.putString(ABOUT, about);
        editor.commit();
    }

    public void followerSession(String follower) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(FOLLOW_STATUS, follower);
        Log.e("share check", follower);
        editor.commit();
    }

    public void profileStatusSession(String proStatus) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(PRO_STATUS, proStatus);
        Log.e("share check", proStatus);
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, HomeActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            context.startActivity(i);
        }
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(USER_ID, pref.getString(USER_ID, null));
        // return user
        return user;
    }

    public HashMap<String, String> getUserProfile() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(USER_PROFILE, pref.getString(USER_PROFILE, null));
        user.put(USER_NAME, pref.getString(USER_NAME, null));
        user.put(EDUCATION, pref.getString(EDUCATION, null));
        user.put(GENDER, pref.getString(GENDER, null));
        user.put(DOB, pref.getString(DOB, null));
        user.put(LOCATION, pref.getString(LOCATION, null));
        user.put(ABOUT, pref.getString(ABOUT, null));

        user.put(FOLLOWER, pref.getString(FOLLOWER, null));
        user.put(FOLLOWING, pref.getString(FOLLOWING, null));
        user.put(COVER_PROFILE, pref.getString(COVER_PROFILE, null));
        user.put(FOLLOW_STATUS, pref.getString(FOLLOW_STATUS, null));
        user.put(PRO_STATUS, pref.getString(PRO_STATUS, null));
        user.put(YOUTHLIVEID, pref.getString(YOUTHLIVEID, null));

        user.put(FOLLOW_UNFOLLOW, pref.getString(FOLLOW_UNFOLLOW, null));
        // return user
        return user;
    }


    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void createLoginSession(String userId) {
    }
}
