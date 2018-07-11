package com.yl.youthlive;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {OfflineBean.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract Queries queries();
}
