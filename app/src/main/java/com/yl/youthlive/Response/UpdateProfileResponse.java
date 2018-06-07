package com.yl.youthlive.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 4/18/2017.
 */
public class UpdateProfileResponse implements Serializable {
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

    @SerializedName("data")
    private DataUpdate data;

    public DataUpdate getData() {
        return data;
    }
    public void setData(DataUpdate data) {
        this.data = data;
    }

   public class DataUpdate{

        @SerializedName("fname")
        private String fname;

        public String getFname() {
            return fname;
        }
        public void setFname(String fname) {
            this.fname = fname;
        }

        @SerializedName("education")
        private String education;

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        @SerializedName("dob")
        private String dob;

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }


        @SerializedName("image")
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }


        @SerializedName("gender")
        private String gender;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }


        @SerializedName("location")
        private String location;

        public String getLocation() {
            return location;
        }
        public void setLocation(String location) {
            this.location = location;
        }

       @SerializedName("about_me")
       private String about_me;

       public String getAbout_me() {
           return about_me;
       }

       public void setAbout_me(String about_me) {
           this.about_me = about_me;
       }
   }


}
