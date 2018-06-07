package com.yl.youthlive.Response;

import com.google.gson.annotations.SerializedName;


/**
 * Created by admin on 4/5/2017.
 */
public class ProfileDetails{

    @SerializedName("data")
    private DataModel data;

    public DataModel getData() {
        return data;
    }
    public void setData(DataModel data) {
        this.data = data;
    }

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("status")
    private int status;

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public class  DataModel{

        @SerializedName("userImage")
        private String userImage;

        public String getUserImage() {
            return userImage;
        }
        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        @SerializedName("youthLiveId")
        private String youthLiveId;

        public String getYouthLiveId() {
            return youthLiveId;
        }
        public void setYouthLiveId(String youthLiveId) {
            this.youthLiveId = youthLiveId;
        }

        @SerializedName("userName")
        private String userName;

        public String getUserName() {
            return userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }

        @SerializedName("dob")
        private String dob;

        public String getDob() {
            return dob;
        }
        public void setDob(String dob) {
            this.dob = dob;
        }


        @SerializedName("about_me")
        private String about_me;

        public String getAbout_me() {
            return about_me;
        }
        public void setAbout_me(String about_me) {
            this.about_me = about_me;
        }

        @SerializedName("education")
        private String education;

        public String getEducation() {
            return education;
        }
        public void setEducation(String education) {
            this.education = education;
        }


        @SerializedName("location")
        private String location;

        public String getLocation() {
            return location;
        }
        public void setLocation(String location) {
            this.location = location;
        }

        @SerializedName("gender")
        private String gender;

        public String getGender() {
            return gender;
        }
        public void setGender(String gender) {
            this.gender = gender;
        }

        @SerializedName("followers")
        private String followers;

        public String getFollowers() {
            return followers;
        }
        public void setFollowers(String followers) {
            this.followers = followers;
        }


        @SerializedName("following")
        private String following;

        public String getFollowing() {
            return following;
        }
        public void setFollowing(String following) {
            this.following = following;
        }


        @SerializedName("cover_image")
        private String cover_image;

        public String getCover_image() {
            return cover_image;
        }
        public void setCover_image(String cover_image) {
            this.cover_image = cover_image;
        }


        @SerializedName("privacy_status")
        private String privacy_status;

        public String getPrivacy_status() {
            return privacy_status;
        }
        public void setPrivacy_status(String privacy_status) {
            this.privacy_status = privacy_status;
        }

        @SerializedName("follow_status")
        private String follow_status;

        public String getFollow_status() {
            return follow_status;
        }
        public void setFollow_status(String follow_status) {
            this.follow_status = follow_status;
        }
    }
}
