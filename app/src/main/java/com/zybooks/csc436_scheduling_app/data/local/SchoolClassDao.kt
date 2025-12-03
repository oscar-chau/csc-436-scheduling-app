package com.zybooks.csc436_scheduling_app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolClassDao {

    @Query("SELECT * FROM class")
     fun getClasses(): Flow<List<SchoolClass>>

    @Upsert
    suspend fun upsertClass(schoolClass: SchoolClass)

    @Delete
    suspend fun deleteClass(schoolClass: SchoolClass)

    @Query("DELETE FROM class")
    suspend fun deleteAllClasses()
}