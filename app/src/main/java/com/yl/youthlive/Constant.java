package com.yl.youthlive;

/**
 * Created by admin on 4/3/2017.
 */
public class Constant {

    public static final String ROOT_URL="http://nationproducts.in/youthlive/api/";
    public static final String USER_DATA = "USER_DATA";
    //sub url
//    public static final String SIGN_UP="/User_api/register";
//    public static final String CREATE_PROFILE="/User_profile/create_profile.php";
//    public  static final String LOGIN="/user_jump/";
//    public static final String VIEW_PROFILE="/user_jump/view_user_profile";
    public static final String SIGN_UP="/methods.php?action=signup";
    public static final String LOGIN="/methods.php?action=login";
    public static final String FORGET_PASSWORD="/methods.php?action=forget_password";
    public static final String CREATE_PROFILE="/methods.php?action=create_proflie";
    public static final String VIEW_PROFILE="/get_profile.php";
    public static final String UPDATE_PROFILE="/add_user_image.php";
    public static final String CHANGE_PASSWORD="/methods.php?action=change_password";
    public static final String COVER_IMAGE="/add_cover_image.php";
    public static final String FACEBOOK_INTEGRATION="/socialsign_up.php";
    public static final String TWITER_INTEGRATION="/methods.php?action=twitter_login";
    public static final String FOLLOWER_RESPONSE="/methods.php?action=follow_status";
    public static final String PROFILE_STATUS="/methods.php?action=privacy_status";
    public static final String SEARCH_PROFILE_VIEW="/methods.php?action=search_users";
    public static final String ADD_POST_IMAGE="/add_video.php";
    public static final String TIMELINE_POST="/methods.php?action=get_timeline";
    public static final String FOLLOW_USER_PROFILE="/methods.php?action=follow";
    public static final String WALL_POST_DELETE="/methods.php?action=delete_post";
    public static final String DELETED_POST_WALL="/methods.php?action=delete_post";
    public static final String FRIEND_LIST_GET="/methods.php?action=get_follower_list";
    public static final String FOLLOWER_LIST_GET="/methods.php?action=my_follow_list";
    public static final String SHARE_POST_TIMELINE="/methods.php?action=share_post";
    // id gor create user
    public static final String USER_ID="USER_ID";
    public static final String DEVICES_NAME="android";
}
