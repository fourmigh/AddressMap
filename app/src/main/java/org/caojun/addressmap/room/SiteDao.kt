package org.caojun.addressmap.room

import android.arch.persistence.room.*

@Dao
interface SiteDao {

    @Query("SELECT * FROM site")
    fun queryAll(): List<Site>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg site: Site)

    @Delete
    fun delete(vararg site: Site): Int

    @Delete
    fun delete(sites: List<Site>): Int

    @Update
    fun update(vararg site: Site): Int

    @Update
    fun update(sites: List<Site>): Int
}