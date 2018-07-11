package com.yl.youthlive;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface Queries {

    @Query("SELECT * FROM OfflineBean")
    List<OfflineBean> getAll();

    @Query("SELECT * FROM OfflineBean WHERE liveId IN (:liveIds)")
    List<OfflineBean> getByIds(String[] liveIds);

    @Insert
    void insert(OfflineBean users);

    @Delete
    void delete(OfflineBean user);

}
