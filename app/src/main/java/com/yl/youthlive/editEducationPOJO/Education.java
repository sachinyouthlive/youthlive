package com.yl.youthlive.editEducationPOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TBX on 11/23/2017.
 */

public class Education {

    @SerializedName("educationId")
    @Expose
    private String educationId;
    @SerializedName("schoolTitle")
    @Expose
    private Object schoolTitle;
    @SerializedName("time")
    @Expose
    private Object time;

    public String getEducationId() {
        return educationId;
    }

    public void setEducationId(String educationId) {
        this.educationId = educationId;
    }

    public Object getSchoolTitle() {
        return schoolTitle;
    }

    public void setSchoolTitle(Object schoolTitle) {
        this.schoolTitle = schoolTitle;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

}
