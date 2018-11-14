package com.app.youthlive.Response;

import com.google.gson.annotations.SerializedName;


/**
 * Created by admin on 4/5/2017.
 */
public class ProfileDetails {

    @SerializedName("data")
    private DataModel data;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private int status;

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class DataModel {

        @SerializedName("userImage")
        private String userImage;
        @SerializedName("youthLiveId")
        private String youthLiveId;
        @SerializedName("userName")
        private String userName;
        @SerializedName("dob")
        private String dob;
        @SerializedName("about_me")
        private String about_me;
        @SerializedName("education")
        private String education;
        @SerializedName("location")
        private String location;
        @SerializedName("gender")
        private String gender;
        @SerializedName("followers")
        private String followers;
        @SerializedName("following")
        private String following;
        @SerializedName("cover_image")
        private String cover_image;
        @SerializedName("privacy_status")
        private String privacy_status;
        @SerializedName("follow_status")
        private String follow_status;

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getYouthLiveId() {
            return youthLiveId;
        }

        public void setYouthLiveId(String youthLiveId) {
            this.youthLiveId = youthLiveId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getAbout_me() {
            return about_me;
        }

        public void setAbout_me(String about_me) {
            this.about_me = about_me;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getFollowers() {
            return followers;
        }

        public void setFollowers(String followers) {
            this.followers = followers;
        }

        public String getFollowing() {
            return following;
        }

        public void setFollowing(String following) {
            this.following = following;
        }

        public String getCover_image() {
            return cover_image;
        }

        public void setCover_image(String cover_image) {
            this.cover_image = cover_image;
        }

        public String getPrivacy_status() {
            return privacy_status;
        }

        public void setPrivacy_status(String privacy_status) {
            this.privacy_status = privacy_status;
        }

        public String getFollow_status() {
            return follow_status;
        }

        public void setFollow_status(String follow_status) {
            this.follow_status = follow_status;
        }
    }
}
