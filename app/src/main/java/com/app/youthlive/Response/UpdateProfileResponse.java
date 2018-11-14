package com.app.youthlive.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 4/18/2017.
 */
public class UpdateProfileResponse implements Serializable {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private int status;
    @SerializedName("data")
    private DataUpdate data;

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

    public DataUpdate getData() {
        return data;
    }

    public void setData(DataUpdate data) {
        this.data = data;
    }

    public class DataUpdate {

        @SerializedName("fname")
        private String fname;
        @SerializedName("education")
        private String education;
        @SerializedName("dob")
        private String dob;
        @SerializedName("image")
        private String image;
        @SerializedName("gender")
        private String gender;
        @SerializedName("location")
        private String location;
        @SerializedName("about_me")
        private String about_me;

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getAbout_me() {
            return about_me;
        }

        public void setAbout_me(String about_me) {
            this.about_me = about_me;
        }
    }


}
