package com.yl.youthlive.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by admin on 5/18/2017.
 */
public class FriendResponseList {

    @SerializedName("success")
    private int success;

    public int getSuccess() {
        return success;
    }

    @SerializedName("data")
    ArrayList<ModelFriend> data;

    public ArrayList<ModelFriend> getData() {
        return data;
    }

    public class ModelFriend{




        @SerializedName("id")
        private String id;

        public String getId() {
            return id;
        }

        @SerializedName("fname")
        private String fname;

        public String getFname() {
            return fname;
        }

        @SerializedName("lname")
        private String lname;

        public String getLname() {
            return lname;
        }

        @SerializedName("image")
        private String image;

        public String getImage() {
            return image;
        }
    }

}
