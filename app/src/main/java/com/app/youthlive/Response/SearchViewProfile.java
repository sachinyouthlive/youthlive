package com.app.youthlive.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 5/1/2017.
 */
public class SearchViewProfile implements Serializable {

    @SerializedName("success")
    @Expose
    int success;
    @SerializedName("data")
    private ArrayList<Roles> data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ArrayList<Roles> getData() {
        return data;
    }

    public void setData(ArrayList<Roles> data) {
        this.data = data;
    }

    public class Roles {

        @SerializedName("user_id")
        String user_id;
        @SerializedName("fname")
        String fname;
        @SerializedName("lname")
        String lname;
        @SerializedName("image")
        String image;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getLname() {
            return lname;
        }

        public void setLname(String lname) {
            this.lname = lname;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }


    }
}
