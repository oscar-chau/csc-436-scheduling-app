package com.zybooks.csc436_scheduling_app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.zybooks.csc436_scheduling_app.data.model.Assignment
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDao {
    @Upsert
    suspend fun upsertAssignment(assignment: Assignment)

    @Delete
    suspend fun deleteAssignment(assignment: Assignment)

    @Query("SELECT * FROM assignment")
    fun getAllAssignments(): Flow<List<Assignment>>

    @Query("DELETE FROM assignment")
    suspend fun deleteAllAssignments()
}