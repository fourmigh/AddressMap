package org.caojun.addressmap.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Created by CaoJun on 2017/8/31.
 */
@Database(entities = [Site::class], version = 1, exportSchema = false)
abstract class SiteDatabase : RoomDatabase() {

    abstract fun getSiteDao(): SiteDao

    companion object {
        private var INSTANCE: SiteDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): SiteDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, SiteDatabase::class.java, "site_database")
                    .build()
            }
            return INSTANCE!!
        }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}