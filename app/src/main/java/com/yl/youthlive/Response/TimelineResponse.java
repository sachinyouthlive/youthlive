package com.yl.youthlive.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 5/2/2017.
 */
public class TimelineResponse implements Serializable {
    @SerializedName("success")
    private int success;
    @SerializedName("data")
    private ArrayList<ModelData> data;
    @SerializedName("user_info")
    private ArrayList<ModelData> user_info;
    @SerializedName("share_post")
    private ArrayList<ModelData> share_post;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ArrayList<ModelData> getData() {
        return data;
    }

    public void setData(ArrayList<ModelData> data) {
        this.data = data;
    }

    public ArrayList<ModelData> getUser_info() {
        return user_info;
    }

    public void setUser_info(ArrayList<ModelData> user_info) {
        this.user_info = user_info;
    }

    public ArrayList<ModelData> getShare_post() {
        return share_post;
    }

    public class ModelData {

        @SerializedName("user_id")
        String user_id;
        @SerializedName("user_image")
        String user_image;
        @SerializedName("fname")
        String fname;
        @SerializedName("lname")
        String lname;
        @SerializedName("post_id")
        String post_id;
        @SerializedName("post_image")
        String post_image;
        @SerializedName("like")
        String like;
        @SerializedName("comment")
        String comment;
        @SerializedName("share")
        String share;
        @SerializedName("time")
        String time;
        @SerializedName("video")
        String video;
        @SerializedName("like_status")
        String like_status;
        @SerializedName("status_share")
        String status_share;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
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

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getPost_image() {
            return post_image;
        }

        public void setPost_image(String post_image) {
            this.post_image = post_image;
        }

        public String getLike() {
            return like;
        }

        public void setLike(String like) {
            this.like = like;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getShare() {
            return share;
        }

        public void setShare(String share) {
            this.share = share;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getVideo() {
            return video;
        }

        public String getLike_status() {
            return like_status;
        }

        public String getStatus_share() {
            return status_share;
        }
    }


}
