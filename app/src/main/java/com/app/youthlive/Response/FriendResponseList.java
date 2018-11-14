package com.app.youthlive.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by admin on 5/18/2017.
 */
public class FriendResponseList {

    @SerializedName("data")
    ArrayList<ModelFriend> data;
    @SerializedName("success")
    private int success;

    public int getSuccess() {
        return success;
    }

    public ArrayList<ModelFriend> getData() {
        return data;
    }

    public class ModelFriend {


        @SerializedName("id")
        private String id;
        @SerializedName("fname")
        private String fname;
        @SerializedName("lname")
        private String lname;
        @SerializedName("image")
        private String image;

        public String getId() {
            return id;
        }

        public String getFname() {
            return fname;
        }

        public String getLname() {
            return lname;
        }

        public String getImage() {
            return image;
        }
    }

}
