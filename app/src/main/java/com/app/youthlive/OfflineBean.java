package com.app.youthlive;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class OfflineBean {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "liveId")
    private String liveId;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "status")
    private String status;


    public int getId() {
        return id;
    }

    public String getLiveId() {
        return liveId;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
