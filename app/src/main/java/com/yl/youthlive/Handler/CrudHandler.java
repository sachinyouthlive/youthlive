package com.yl.youthlive.Handler;


import com.yl.youthlive.Constant;
import com.yl.youthlive.Response.AddPostImageResponse;
import com.yl.youthlive.Response.ChangeCoverImageResponse;
import com.yl.youthlive.Response.ChangePasswordResponse;
import com.yl.youthlive.Response.CreateProResponse;
import com.yl.youthlive.Response.CreateSignUp;
import com.yl.youthlive.Response.FacebookResponse;
import com.yl.youthlive.Response.FollowProfileResponse;
import com.yl.youthlive.Response.FollowUserProfileResponse;
import com.yl.youthlive.Response.FollowerResponse;
import com.yl.youthlive.Response.FriendResponseList;
import com.yl.youthlive.Response.LikeResponse;
import com.yl.youthlive.Response.LoginResponse;
import com.yl.youthlive.Response.PasswordForgetResponse;
import com.yl.youthlive.Response.PostWallDeleteResponse;
import com.yl.youthlive.Response.ProfileDetails;
import com.yl.youthlive.Response.ProfileViewResponse;
import com.yl.youthlive.Response.SearchViewProfile;
import com.yl.youthlive.Response.ShareResponse;
import com.yl.youthlive.Response.TimelineResponse;
import com.yl.youthlive.Response.TwiterResponse;
import com.yl.youthlive.Response.UpdateProfileResponse;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.mime.MultipartTypedOutput;

/**
 * Created by admin on 4/3/2017.
 */
public class CrudHandler {
    public interface SignUp{
        @POST(Constant.SIGN_UP)
        void createSignUp(@Body MultipartTypedOutput attachments, Callback<CreateSignUp> clResponse);
    }
    public interface Login{
        @POST(Constant.LOGIN)
        void createLogin(@Body MultipartTypedOutput attachments, Callback<LoginResponse> clResponse);
    }
    public interface PasswordForget{
        @POST(Constant.FORGET_PASSWORD)
        void passwordForget(@Body MultipartTypedOutput attachments, Callback<PasswordForgetResponse> clResponse);
    }

    public interface CreateUser{
        @POST(Constant.CREATE_PROFILE)
        void createUser(@Body MultipartTypedOutput attachments, Callback<CreateProResponse> clResponse);
    }

    public interface ProfileView{
        @POST(Constant.VIEW_PROFILE)
        void createViewProfile(@Body MultipartTypedOutput attachments, Callback<ProfileDetails> clResponse);
    }

    public interface UpdateProfile{
        @POST(Constant.UPDATE_PROFILE)
        void updateViewProfile(@Body MultipartTypedOutput attachments, Callback<UpdateProfileResponse> clResponse);
    }
    public interface ChangePasswordUser{
        @POST(Constant.CHANGE_PASSWORD)
        void changePassword(@Body MultipartTypedOutput attachments, Callback<ChangePasswordResponse> clResponse);
    }
    public interface CoverImageUpdate{
        @POST(Constant.COVER_IMAGE)
        void changeCoverImage(@Body MultipartTypedOutput attachments, Callback<ChangeCoverImageResponse> clResponse);
    }
    public interface FacebookIntegration{
        @POST(Constant.FACEBOOK_INTEGRATION)
        void fbCreate(@Body MultipartTypedOutput attachment, Callback<FacebookResponse> clResponse);
    }
    public interface TwiterIntegration{
        @POST(Constant.TWITER_INTEGRATION)
        void twiterCreate(@Body MultipartTypedOutput attachment, Callback<TwiterResponse> clResponse);
    }
    public interface FollowerResponseGet{
        @POST(Constant.FOLLOWER_RESPONSE)
        void followResponse(@Body MultipartTypedOutput attachment, Callback<FollowerResponse> clResponse);
    }
    public interface ProfileStatusGet{
        @POST(Constant.PROFILE_STATUS)
        void profileStatus(@Body MultipartTypedOutput attachment, Callback<ProfileViewResponse> clResponse);
    }
    public interface SearchProfileResponse{
        @POST(Constant.SEARCH_PROFILE_VIEW)
        void searchView(@Body MultipartTypedOutput attachment, Callback<SearchViewProfile> clResponse);
    }
    public interface AddPostImage{
        @POST(Constant.ADD_POST_IMAGE)
        void addPost(@Body MultipartTypedOutput attachment, Callback<AddPostImageResponse> clResponse);
    }
    public interface TimeLinePost{
        @POST(Constant.TIMELINE_POST)
        void timelinePost(@Body MultipartTypedOutput attachment, Callback<TimelineResponse> clResponse);
    }
    public interface FollowUserProfile{
        @POST(Constant.FOLLOW_USER_PROFILE)
        void followUser(@Body MultipartTypedOutput attachment, Callback<FollowUserProfileResponse> clResponse);
    }
    public interface DeleteWallPost{
        @POST(Constant.WALL_POST_DELETE)
        void deleteWallPost(@Body MultipartTypedOutput attachment, Callback<PostWallDeleteResponse> clResponse);
    }
    public interface PostDeleted{
        @POST(Constant.DELETED_POST_WALL)
        void postDlt(@Body MultipartTypedOutput attachment, Callback<PostWallDeleteResponse> clResponse);
    }
    public interface FriendListGet{
        @POST(Constant.FRIEND_LIST_GET)
        void friendList(@Query("user_id") String userid, Callback<FriendResponseList> clResponse);
    }

    public interface LikeGetTimeline{
        @POST(Constant.FRIEND_LIST_GET)
        void likeTimeline(@Query("user_id") String userid,
                          @Query("post_id") String postID, Callback<LikeResponse> clResponse);
    }

    public interface FollowProfileGet{
        @POST(Constant.FRIEND_LIST_GET)
        void followPro(@Query("user_id") String userid,
                       @Query("other_id") String otherID, Callback<FollowProfileResponse> clResponse);
    }

    public interface ShareTimelinePost{
        @POST(Constant.SHARE_POST_TIMELINE)
        void sharePost(@Query("post_id") String postID,
                       @Query("user_id") String userID,
                       @Query("share_user_id") String shareUserID,
                       @Query("dateTime") String dateTime,
                       Callback<ShareResponse> clResponse);
    }
}
